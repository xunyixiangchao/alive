package com.example.alive.network.dto

import kotlinx.serialization.Serializable
import com.example.alive.data.entity.TaskStatus

/**
 * CircleRemovalResponse - 圈选提交的API响应
 *
 * 当Fragment3提交圈选数据时，服务器返回此Response
 * 包含新创建的任务ID
 *
 * @param taskId 服务器创建的任务ID，用于后续轮询
 * @param message 响应消息（可选）
 */
@Serializable
data class CircleRemovalResponse(
    val taskId: Long,
    val message: String? = null
)

/**
 * TaskStatusResponse - 任务状态查询的API响应
 *
 * Fragment4轮询时返回当前任务的处理状态
 * 包含最新的任务状态和处理进度
 *
 * @param taskId 任务ID
 * @param status 当前任务状态
 * @param progress 处理进度百分比（0-100）
 * @param message 状态消息（如错误原因）
 * @param resultImagePath 处理完成后的结果图路径（仅当status=COMPLETED时）
 */
@Serializable
data class TaskStatusResponse(
    val taskId: Long,
    val status: TaskStatus,
    val progress: Int = 0,
    val message: String? = null,
    val resultImagePath: String? = null
)

/**
 * FavoriteResponse - 收藏状态更新的API响应
 *
 * @param taskId 任务ID
 * @param isFavorite 新的收藏状态（1=收藏, 0=未收藏）
 */
@Serializable
data class FavoriteResponse(
    val taskId: Long,
    val isFavorite: Int
)

/**
 * ErrorResponse - 错误响应
 *
 * @param code 错误码
 * @param message 错误信息
 */
@Serializable
data class ErrorResponse(
    val code: Int,
    val message: String
)

/**
 * UploadUrlResponse - 获取上传地址的API响应
 *
 * @param uploadUrl 用于上传图片的临时URL地址
 * @param expiresIn 上传地址的有效期（秒）
 */
@Serializable
data class UploadUrlResponse(
    val uploadUrl: String,
    val expiresIn: Int = 3600
)

/**
 * ImageUploadResponse - 上传图片的API响应
 *
 * @param success 上传是否成功
 * @param imageUrl 上传成功后的图片URL（可用于后续处理）
 * @param message 响应消息
 */
@Serializable
data class ImageUploadResponse(
    val success: Boolean,
    val imageUrl: String,
    val message: String? = null
)
