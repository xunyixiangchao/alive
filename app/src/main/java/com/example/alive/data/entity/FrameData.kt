package com.example.alive.data.entity

/**
 * FrameData - 视频帧数据
 *
 * 用于存储从视频中提取的单个帧的信息
 * 主要用途：
 * 1. 在Fragment2中显示提取的8帧列表
 * 2. 在Fragment3中显示参考帧供用户参考
 *
 * 数据来源：
 * - 从VideoUtils.extract8FramesFromVideo()获取
 * - 帧存储在应用缓存目录：context.cacheDir/frames/
 *
 * @param frameNumber 帧号（0-7，表示第几帧）
 * @param filePath 帧图像的文件路径
 * @param timestamp 帧在视频中的时间戳（毫秒）
 */
data class FrameData(
    val frameNumber: Int,
    val filePath: String,
    val timestamp: Long = 0L
) {
    /**
     * 获取帧号的显示文本
     */
    fun getFrameNumberText(): String = "Frame ${frameNumber + 1}"
}
