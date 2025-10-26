package com.example.alive.ui.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.example.alive.data.repository.AliveRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * Fragment5ViewModel - 结果展示界面的ViewModel
 *
 * 职责：
 * - 管理任务的收藏状态
 * - 处理结果图的下载保存功能
 * - 提供结果图的分享功能
 * - 通知系统媒体库更新
 * - 处理相关操作的错误情况
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 *
 * @param repository AliveRepository实例，用于网络请求和数据库操作
 */
class Fragment5ViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 收藏状态标志
     * true表示当前任务已被收藏
     */
    val isFavorite = MutableLiveData(false)

    /**
     * 下载成功标志
     * true表示结果图已成功保存到相册
     */
    val downloadSuccess = MutableLiveData(false)

    /**
     * 更新任务的收藏状态
     * @param taskId 要更新的任务ID
     * @param isFavorite 新的收藏状态，true为收藏，false为取消收藏
     *
     * 使用BaseViewModel提供的launchAsync方法处理异步操作
     */
    fun updateFavoriteStatus(taskId: Long, isFavorite: Boolean) {
        launchAsync(
            operation = {
                // 调用后端API更新收藏状态
                repository.updateFavorite(taskId, isFavorite)
            },
            onSuccess = { response ->
                this.isFavorite.value = response.isFavorite == 1

                // 同时更新本地数据库
                viewModelScope.launch {
                    val task = repository.getTaskById(taskId)
                    if (task != null) {
                        repository.updateTask(task.copy(isFavorite = response.isFavorite == 1))
                    }
                }
            }
        )
    }

    /**
     * 下载结果图到本地相册
     * @param context Android上下文，用于访问文件系统和媒体库
     * @param resultImagePath 结果图的源文件路径
     * @param fileName 保存的文件名，默认为"result_alive.jpg"
     *
     * 操作流程：
     * 1. 检查源文件是否存在
     * 2. 在Pictures/Alive目录下创建目标文件夹
     * 3. 将结果图复制到目标位置
     * 4. 通知系统媒体库扫描新文件
     * 5. 设置downloadSuccess标志
     */
    fun downloadResultImage(
        context: Context,
        resultImagePath: String,
        fileName: String = "result_alive.jpg"
    ) {
        launchAsync(
            operation = {
                // 检查源文件是否存在
                val sourceFile = File(resultImagePath)
                if (!sourceFile.exists()) {
                    throw IllegalArgumentException("Result image file not found")
                }

                // 创建保存目录（Pictures/Alive）
                val picturesDir = File(
                    android.os.Environment.getExternalStoragePublicDirectory(
                        android.os.Environment.DIRECTORY_PICTURES
                    ),
                    "Alive"
                )

                if (!picturesDir.exists()) {
                    picturesDir.mkdirs()
                }

                val destFile = File(picturesDir, fileName)

                // 复制文件到相册目录
                FileInputStream(sourceFile).use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }

                // 通知系统媒体库更新，使图片在相册中可见
                android.media.MediaScannerConnection.scanFile(
                    context,
                    arrayOf(destFile.absolutePath),
                    null,
                    null
                )

                destFile
            },
            onSuccess = {
                downloadSuccess.value = true
            }
        )
    }

    /**
     * 分享结果图到其他应用
     * @param context Android上下文，用于启动分享Intent
     * @param resultImagePath 要分享的结果图路径
     *
     * 操作流程：
     * 1. 检查文件是否存在
     * 2. 创建文件URI
     * 3. 构建分享Intent
     * 4. 显示应用选择器让用户选择分享目标
     */
    fun shareResultImage(context: Context, resultImagePath: String) {
        try {
            // 检查文件是否存在
            val file = File(resultImagePath)
            if (!file.exists()) {
                setError("Result image file not found")
                return
            }

            // 创建文件URI
            val uri = Uri.fromFile(file)
            // 构建分享Intent
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
            }

            // 显示应用选择器
            val chooser = Intent.createChooser(shareIntent, "Share Result Image")
            context.startActivity(chooser)
        } catch (e: Exception) {
            // 捕获分享过程中的异常
            setError(e.message)
        }
    }
}
