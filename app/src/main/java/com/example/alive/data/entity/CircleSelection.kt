package com.example.alive.data.entity

import kotlinx.serialization.Serializable

/**
 * CircleSelection - 圆圈圈选数据
 *
 * 用于存储用户在图片上绘制的圆圈信息
 * 每个圆圈由中心坐标(centerX, centerY)和半径radius组成
 *
 * 序列化方式：
 * - 使用Kotlin Serialization库进行JSON序列化
 * - 用于在任务中存储多个圆圈的JSON数组
 * - 例如：[{"centerX": 100.0, "centerY": 150.0, "radius": 50.0}, ...]
 *
 * @param centerX 圆圈中心的X坐标（相对于图片）
 * @param centerY 圆圈中心的Y坐标（相对于图片）
 * @param radius 圆圈的半径（像素）
 */
@Serializable
data class CircleSelection(
    val centerX: Float,
    val centerY: Float,
    val radius: Float
) {
    /**
     * 验证圆圈数据的有效性
     * @return true如果所有字段都非负且有意义
     */
    fun isValid(): Boolean {
        return centerX >= 0 && centerY >= 0 && radius > 0
    }
}
