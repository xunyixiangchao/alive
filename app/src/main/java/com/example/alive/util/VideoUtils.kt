package com.example.alive.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaMetadataRetriever
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * 视频处理工具类
 * 提供与视频帧提取和处理相关的工具方法
 * 主要功能：
 * 1. 从视频中均匀提取8个关键帧
 * 2. 将多个帧合并成ALIVE格式图像
 * 3. Bitmap保存到文件
 *
 * 注意：此实现为简化版本，真实的ALIVE格式处理可能需要专门的编码库
 */
object VideoUtils {

    /**
     * 从视频中提取8帧（首帧、末帧、中间6帧均匀分布）
     * 根据视频时长均匀地提取8个关键帧
     * 提取完毕后调用回调函数通知调用者
     *
     * @param videoPath 视频文件的完整路径
     * @param outputDir 提取的帧图像的输出目录
     * @param onFrameExtracted 每当一帧提取完成时的回调函数，参数为帧索引和文件路径
     * @return 如果成功提取了8帧返回true，否则false
     */
    fun extract8FramesFromVideo(
        videoPath: String,
        outputDir: String,
        onFrameExtracted: (frameIndex: Int, framePath: String) -> Unit
    ): Boolean {
        return try {
            val retriever = MediaMetadataRetriever()
            // 设置数据源为视频文件
            retriever.setDataSource(videoPath)

            // 获取视频总时长（毫秒）
            val durationStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationStr?.toLongOrNull() ?: return false

            // 计算8帧的时间点（单位：微秒，MediaMetadataRetriever.getFrameAtTime使用微秒）
            val frameTimePoints = mutableListOf<Long>()
            frameTimePoints.add(0) // 首帧：位置0
            frameTimePoints.add(duration * 1000) // 末帧：视频末尾

            // 中间6帧均匀分布在视频中（7等分之间的点）
            for (i in 1..6) {
                val timePoint = (duration * 1000 * i) / 7
                frameTimePoints.add(timePoint)
            }

            // 排序时间点
            frameTimePoints.sort()

            // 创建输出目录（如果不存在）
            val outputDirectory = File(outputDir)
            if (!outputDirectory.exists()) {
                outputDirectory.mkdirs()
            }

            // 遍历时间点，逐个提取帧
            var frameIndex = 0
            for (timePoint in frameTimePoints) {
                try {
                    // 获取指定时间点的帧
                    // OPTION_CLOSEST表示获取最接近的关键帧
                    val bitmap = retriever.getFrameAtTime(timePoint, MediaMetadataRetriever.OPTION_CLOSEST)
                    if (bitmap != null) {
                        // 生成帧文件路径
                        val framePath = File(outputDirectory, "frame_$frameIndex.jpg").absolutePath
                        // 将Bitmap保存为JPEG文件
                        saveBitmap(bitmap, framePath)
                        // 调用回调函数通知调用者
                        onFrameExtracted(frameIndex, framePath)
                        frameIndex++
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            // 释放资源
            retriever.release()
            // 确认是否成功提取了8帧
            frameIndex >= 8
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 将Bitmap保存为JPEG文件
     * 内部方法，用于将内存中的Bitmap图像压缩并保存到磁盘
     *
     * @param bitmap 要保存的Bitmap对象
     * @param filePath 保存的文件路径
     * @return 如果保存成功返回true，否则false
     */
    private fun saveBitmap(bitmap: Bitmap, filePath: String): Boolean {
        return try {
            // 使用FileOutputStream写入文件
            FileOutputStream(filePath).use { fos ->
                // 使用JPEG格式压缩，质量为90（0-100）
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 从多个帧图像创建ALIVE格式图
     * 将8个帧图像合并成一个ALIVE格式的图像
     * 注意：这是一个简化实现，真实的ALIVE格式可能有特殊的编码方式
     *
     * 当前实现方式：
     * - 将所有帧按顺序水平拼接在一起
     * - 结果Bitmap的宽度为单个帧宽度的8倍，高度与单个帧相同
     *
     * @param framePaths 8个帧文件的路径列表
     * @param outputPath 生成的ALIVE图像的输出路径
     * @return 如果合并成功返回true，否则false
     */
    fun createAliveFromFrames(
        framePaths: List<String>,
        outputPath: String
    ): Boolean {
        return try {
            // 加载所有帧的Bitmap到内存
            val frames = mutableListOf<Bitmap>()

            for (framePath in framePaths) {
                // 解码帧文件为Bitmap
                val bitmap = android.graphics.BitmapFactory.decodeFile(framePath)
                if (bitmap != null) {
                    frames.add(bitmap)
                }
            }

            // 如果没有成功加载任何帧，返回false
            if (frames.isEmpty()) return false

            // 假设所有帧大小相同，以第一帧为参考
            val width = frames[0].width
            val height = frames[0].height

            // 创建结果Bitmap：宽度 = 单帧宽度 × 帧数，高度 = 单帧高度
            // 这样8个帧会水平拼接在一起
            val resultBitmap = Bitmap.createBitmap(width * frames.size, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(resultBitmap)

            // 在Canvas上依次绘制每个帧
            for ((index, bitmap) in frames.withIndex()) {
                // 计算该帧在结果图像中的水平位置
                canvas.drawBitmap(bitmap, (index * width).toFloat(), 0f, null)
            }

            // 将结果Bitmap保存为JPEG文件
            FileOutputStream(outputPath).use { fos ->
                resultBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
