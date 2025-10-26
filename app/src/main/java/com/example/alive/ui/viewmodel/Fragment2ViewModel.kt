package com.example.alive.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.alive.data.entity.FrameData
import com.example.alive.data.repository.AliveRepository
import com.example.alive.util.VideoUtils

/**
 * Fragment2ViewModel - 8帧提取界面的ViewModel
 *
 * 职责：
 * - 在本地从ALIVE图像或视频中提取8帧关键帧（不调用API）
 * - 管理帧提取过程中的加载状态
 * - 处理提取结果并转换为FrameData列表
 * - 处理提取过程中的错误情况
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 * - launchAsync: 异步操作框架
 *
 * 实现方式：
 * - 使用VideoUtils工具类进行本地帧提取
 * - 提取的帧文件保存到应用缓存目录
 * - 使用协程在后台线程执行提取操作，避免阻塞UI
 *
 * @param repository AliveRepository实例（本实现中主要用于后续数据库操作）
 */
class Fragment2ViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 提取的帧数据列表
     * 成功提取后包含8个FrameData对象，每个对象包含帧索引和图片路径
     */
    val framesExtracted = MutableLiveData<List<FrameData>?>()

    /**
     * 从ALIVE图像或视频中在本地提取8帧关键帧
     *
     * 使用BaseViewModel提供的launchAsync方法处理异步操作
     *
     * 实现细节：
     * - 如果是视频文件，使用MediaMetadataRetriever提取首帧、末帧、中间6帧（均匀分布）
     * - 如果是图像文件，视为单帧数据
     * - 提取的帧临时保存到应用缓存目录（应用卸载时自动清理）
     * - 整个提取过程在协程中运行，不阻塞UI线程
     *
     * @param filePath ALIVE图像或视频的本地文件路径
     * @param context Android上下文，用于获取缓存目录
     */
    fun extract8Frames(filePath: String, context: Context) {
        launchAsync(
            operation = {
                // 获取应用缓存目录用于保存临时的帧文件
                val outputDir = context.cacheDir.absolutePath + "/frames"
                val frames = mutableListOf<FrameData>()

                // 调用VideoUtils提取8帧，每当一帧完成时通过回调获知
                val extractionSuccessful = VideoUtils.extract8FramesFromVideo(
                    filePath,
                    outputDir
                ) { frameIndex, framePath ->
                    // 为每个提取的帧创建FrameData对象并添加到列表
                    frames.add(FrameData(frameIndex = frameIndex, imagePath = framePath))
                }

                // 检查提取结果
                if (extractionSuccessful && frames.size >= 8) {
                    // 提取成功，返回排序后的帧列表
                    frames.sortedBy { it.frameIndex }
                } else {
                    // 提取失败或帧数不足
                    throw IllegalStateException("Failed to extract 8 frames from video")
                }
            },
            onSuccess = { frames ->
                // 提取成功，更新framesExtracted
                framesExtracted.value = frames
            }
        )
    }
}
