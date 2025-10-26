package com.example.alive.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * AliveImage - ALIVE图像实体
 *
 * 用于在本地数据库中存储用户选择的ALIVE图像/视频信息
 * Room自动为此类创建表：alive_images
 *
 * 数据来源：
 * - 用户在Fragment1中选择图片/视频时创建
 * - 通过Repository.insertAliveImage()保存到数据库
 *
 * 与Task的关系：
 * - 一个AliveImage可对应多个Task（一对多关系）
 * - Task表中的aliveImageId外键引用此表的id
 *
 * @param id 主键，自动生成的ID
 * @param uri 图像/视频的URI字符串
 *            - 本地文件URI：file:///path/to/image.jpg
 *            - 内容提供者URI：content://media/...
 * @param name 图像/视频的文件名
 * @param timestamp 创建时间戳（毫秒），用于排序和统计
 * @param isFavorite 是否被收藏，默认false
 */
@Entity(tableName = "alive_images")
data class AliveImage(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val uri: String,
    val name: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    /**
     * 获取用于显示的图像名称（去掉扩展名）
     */
    fun getDisplayName(): String {
        return name.substringBeforeLast('.')
    }

    /**
     * 验证实体的有效性
     */
    fun isValid(): Boolean {
        return uri.isNotEmpty() && name.isNotEmpty()
    }
}
