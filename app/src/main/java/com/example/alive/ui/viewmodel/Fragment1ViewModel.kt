package com.example.alive.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.alive.data.entity.AliveImage
import com.example.alive.data.entity.FrameData
import com.example.alive.data.repository.AliveRepository
import com.example.alive.util.VideoUtils
import kotlinx.coroutines.viewModelScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

/**
 * Fragment1ViewModel - 图片选择界面的ViewModel
 *
 * 职责：
 * - 处理用户选择的Alive图像
 * - 本地提取视频/图片的8帧
 * - 获取图片上传地址
 * - 上传图片到服务器
 * - 管理整个流程的加载状态和错误信息
 * - **完整的错误处理和流程控制**
 *
 * 工作流程：
 * 1. saveSelectedImage() - 保存选中的图片到数据库
 * 2. extractFramesFromLocalImage() - 本地提取8帧（无需API）
 * 3. getUploadUrl() - 获取上传地址
 * 4. uploadImageToServer() - 上传图片
 * 5. executeCompleteWorkflow() - 一键执行完整流程（带错误恢复）
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - launchAsync: 异步操作框架
 *
 * @param repository AliveRepository实例
 */
class Fragment1ViewModel(
    private val repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 当前选择的Alive图像对象
     */
    val selectedImage = MutableLiveData<AliveImage?>()

    /**
     * 本地提取的8个帧列表
     * 用户选择图片后自动提取
     */
    val extractedFrames = MutableLiveData<List<FrameData>>(emptyList())

    /**
     * 图片上传地址
     * 从API获取的临时URL
     */
    val uploadUrl = MutableLiveData<String>("")

    /**
     * 上传后的图片URL
     * 上传成功后返回
     */
    val uploadedImageUrl = MutableLiveData<String>("")

    /**
     * 工作流程的当前步骤
     * 用于UI显示进度或调试
     * 0: 未开始
     * 1: 保存到数据库
     * 2: 本地提取8帧
     * 3: 获取上传地址
     * 4: 上传图片
     * 5: 完成
     */
    val currentWorkflowStep = MutableLiveData<Int>(0)

    /**
     * 工作流程的步骤描述
     * 用于显示给用户
     */
    val workflowStepDescription = MutableLiveData<String>("")

    /**
     * 保存用户选择的Alive图像到数据库
     *
     * @param aliveImage 要保存的AliveImage对象
     */
    fun saveSelectedImage(aliveImage: AliveImage) {
        launchAsync(
            operation = {
                repository.insertAliveImage(aliveImage)
            },
            onSuccess = { id ->
                // 插入成功，更新selectedImage
                selectedImage.value = aliveImage.copy(id = id)
            }
        )
    }

    /**
     * 本地提取视频/图片的8帧
     *
     * 抽帧规则（与之前一样）：
     * - 首帧：0ms（视频开头）
     * - 末帧：duration（视频结尾）
     * - 中间6帧：均匀分布
     *
     * @param videoPath 视频或图片的文件路径
     * @param context Android上下文
     */
    fun extractFramesFromLocalImage(videoPath: String, context: Context) {
        launchAsync(
            operation = {
                // 存储提取的帧
                val frames = mutableListOf<FrameData>()

                // 调用VideoUtils进行本地抽帧
                VideoUtils.extract8FramesFromVideo(
                    videoPath = videoPath,
                    context = context,
                    onFrameExtracted = { frameData ->
                        frames.add(frameData)
                    }
                )

                // 返回帧列表
                frames
            },
            onSuccess = { frames ->
                // 抽帧成功，更新UI
                extractedFrames.value = frames
            }
        )
    }

    /**
     * 获取图片上传地址
     *
     * 调用API获取临时的上传URL
     */
    fun fetchUploadUrl() {
        launchAsync(
            operation = {
                repository.getUploadUrl()
            },
            onSuccess = { response ->
                // 保存上传地址
                uploadUrl.value = response.uploadUrl
            }
        )
    }

    /**
     * 上传图片到服务器
     *
     * @param imagePath 本地图片文件路径
     */
    fun uploadImageToServer(imagePath: String) {
        launchAsync(
            operation = {
                // 创建图片文件
                val imageFile = File(imagePath)
                if (!imageFile.exists()) {
                    throw IllegalArgumentException("Image file not found: $imagePath")
                }

                // 创建RequestBody
                val imageBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())

                // 创建MultipartBody
                val multipartBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", imageFile.name, imageBody)
                    .build()

                // 调用API上传
                repository.uploadImage(multipartBody)
            },
            onSuccess = { response ->
                // 上传成功
                if (response.success) {
                    uploadedImageUrl.value = response.imageUrl
                } else {
                    setError(response.message ?: "Upload failed")
                }
            }
        )
    }

    /**
     * 一键执行完整流程：选择 → 抽帧 → 获取上传地址 → 上传
     *
     * **使用 Job、runCatching、ensureActive 进行优雅的错误处理**：
     *
     * runCatching: 捕获异常并返回 Result<T>
     * ensureActive: 检查协程是否被取消（用户退出或返回时自动取消）
     * Job.cancel: 用户返回时自动取消流程
     *
     * 流程特点：
     * 1. 更简洁的异常处理（runCatching）
     * 2. 自动检查协程状态（ensureActive）
     * 3. 支持中途取消（Job）
     * 4. 每步都有详细的进度和错误信息
     *
     * @param aliveImage 选中的图片
     * @param context Android上下文
     */
    fun executeCompleteWorkflow(aliveImage: AliveImage, context: Context) {
        viewModelScope.launch {
            try {
                setLoading(true)

                // 步骤1：保存图片到数据库
                if (!executeStep(1, "Saving image to database...") { saveImageToDatabase(aliveImage) }) {
                    return@launch
                }
                val savedImage = selectedImage.value ?: return@launch

                // 步骤2：本地提取8帧
                if (!executeStep(2, "Extracting frames locally...") { extractFrames(aliveImage, context) }) {
                    return@launch
                }

                // 步骤3：获取上传地址
                if (!executeStep(3, "Getting upload URL...") { getUploadUrlStep() }) {
                    return@launch
                }

                // 步骤4：上传图片
                if (!executeStep(4, "Uploading image...") { uploadImageStep(aliveImage) }) {
                    return@launch
                }

                // 所有步骤完成
                currentWorkflowStep.value = 5
                workflowStepDescription.value = "All steps completed successfully!"

            } catch (e: Exception) {
                setError("Unexpected error: ${e.message}")
                currentWorkflowStep.value = 0
            } finally {
                setLoading(false)
            }
        }
    }

    /**
     * 执行单个工作流步骤
     * 使用 runCatching 进行优雅的异常处理
     * 使用 ensureActive 检查协程状态
     *
     * @param stepNumber 步骤编号 (1-4)
     * @param description 步骤描述
     * @param operation 步骤操作（suspend lambda）
     * @return 步骤是否成功执行
     */
    private suspend fun executeStep(
        stepNumber: Int,
        description: String,
        operation: suspend () -> Unit
    ): Boolean {
        // 更新步骤状态
        currentWorkflowStep.value = stepNumber
        workflowStepDescription.value = description

        // 检查协程是否被取消（用户返回或其他取消操作）
        try {
            ensureActive()
        } catch (e: Exception) {
            // 协程已被取消，停止执行
            currentWorkflowStep.value = 0
            return false
        }

        // 使用 runCatching 包装操作，自动捕获异常
        val result = runCatching {
            operation()
        }

        // 检查结果
        return if (result.isSuccess) {
            // 步骤成功
            true
        } else {
            // 步骤失败，获取异常信息
            val exception = result.exceptionOrNull()
            val errorMsg = when {
                exception is IllegalArgumentException -> exception.message ?: "Invalid argument"
                exception is SecurityException -> "Permission denied"
                exception?.message?.contains("timeout", ignoreCase = true) == true -> "Network timeout"
                exception?.message?.contains("not found", ignoreCase = true) == true -> "File not found"
                else -> exception?.message ?: "Unknown error"
            }
            setError("Step $stepNumber Failed: $errorMsg")
            currentWorkflowStep.value = 0
            false
        }
    }

    /**
     * 步骤1：保存图片到数据库
     */
    private suspend fun saveImageToDatabase(aliveImage: AliveImage) {
        val imageId = repository.insertAliveImage(aliveImage)
        require(imageId > 0) { "Failed to save image: invalid ID returned" }
        selectedImage.value = aliveImage.copy(id = imageId)
    }

    /**
     * 步骤2：本地提取8帧
     */
    private suspend fun extractFrames(aliveImage: AliveImage, context: Context) {
        val frames = mutableListOf<FrameData>()
        VideoUtils.extract8FramesFromVideo(
            videoPath = aliveImage.uri,
            context = context,
            onFrameExtracted = { frameData ->
                frames.add(frameData)
            }
        )
        require(frames.isNotEmpty()) { "No frames extracted from video" }
        extractedFrames.value = frames
    }

    /**
     * 步骤3：获取上传地址
     */
    private suspend fun getUploadUrlStep() {
        val uploadUrlResponse = repository.getUploadUrl()
        require(uploadUrlResponse.uploadUrl.isNotEmpty()) { "Invalid upload URL received from server" }
        uploadUrl.value = uploadUrlResponse.uploadUrl
    }

    /**
     * 步骤4：上传图片
     */
    private suspend fun uploadImageStep(aliveImage: AliveImage) {
        // 验证文件
        val imageFile = File(aliveImage.uri)
        require(imageFile.exists()) { "Image file not found: ${aliveImage.uri}" }

        val maxFileSizeInBytes = 50 * 1024 * 1024  // 50MB
        require(imageFile.length() <= maxFileSizeInBytes) {
            "Image file too large: ${imageFile.length()} bytes (max 50MB)"
        }

        // 创建上传请求
        val imageBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", imageFile.name, imageBody)
            .build()

        // 上传图片
        val uploadResponse = repository.uploadImage(multipartBody)
        require(uploadResponse.success) { uploadResponse.message ?: "Upload failed" }
        require(uploadResponse.imageUrl.isNotEmpty()) { "No image URL returned from server" }

        uploadedImageUrl.value = uploadResponse.imageUrl
    }

    /**
     * 重试执行完整流程
     * 用于用户在出错后点击重试
     *
     * @param aliveImage 选中的图片
     * @param context Android上下文
     */
    fun retryCompleteWorkflow(aliveImage: AliveImage, context: Context) {
        // 清除之前的错误消息
        clearError()
        // 重置步骤
        currentWorkflowStep.value = 0
        workflowStepDescription.value = ""
        // 重新执行完整流程
        executeCompleteWorkflow(aliveImage, context)
    }
}


