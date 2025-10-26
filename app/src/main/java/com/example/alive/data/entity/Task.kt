package com.example.alive.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Task - 任务实体
 *
 * 用于在本地数据库中存储图像处理任务的信息
 * Room自动为此类创建表：tasks
 *
 * 数据来源和生命周期：
 * 1. 用户在Fragment3中进行圈选操作
 * 2. Fragment3ViewModel调用repository.submitCircleRemoval()
 * 3. API返回taskId，Task被插入到数据库
 * 4. Fragment4持续轮询API获取任务状态，更新Task的status
 * 5. 任务完成后显示resultImagePath
 * 6. 用户可在TaskListFragment查看所有历史任务
 *
 * 与AliveImage的关系：
 * - 每个Task必须关联一个AliveImage（通过aliveImageId外键）
 * - 一个AliveImage可对应多个Task（用户可为同一图片创建多个任务）
 *
 * @param id 任务ID（主键），由后端API返回
 * @param aliveImageId 关联的AliveImage ID（外键）
 * @param status 任务状态（PENDING/MARKING/PROCESSING/COMPLETED/FAILED）
 * @param circleSelectionsJson 用户绘制的圆圈数据，存储为JSON字符串
 *                             格式：[{"centerX":100.0,"centerY":150.0,"radius":50.0},...]
 * @param resultImagePath 处理完成后的结果图路径，若为null表示任务未完成
 * @param createdAt 任务创建时间戳（毫秒）
 * @param updatedAt 任务最后更新时间戳（毫秒）
 * @param isFavorite 是否被收藏，默认false
 */
@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = AliveImage::class,
            parentColumns = ["id"],
            childColumns = ["aliveImageId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Task(
    @PrimaryKey
    val id: Long,

    val aliveImageId: Long,
    val status: TaskStatus,
    val circleSelectionsJson: String,
    val resultImagePath: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
) {
    /**
     * 从JSON字符串解析圆圈数据
     * @return List<CircleSelection> 圆圈列表
     */
    fun parseCircleSelections(): List<CircleSelection> {
        return try {
            Json.decodeFromString<List<CircleSelection>>(circleSelectionsJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * 判断任务是否已完成
     */
    fun isCompleted(): Boolean = status == TaskStatus.COMPLETED

    /**
     * 判断任务是否失败
     */
    fun isFailed(): Boolean = status == TaskStatus.FAILED

    /**
     * 判断任务是否还在处理中
     */
    fun isProcessing(): Boolean = status in listOf(
        TaskStatus.PENDING,
        TaskStatus.MARKING,
        TaskStatus.PROCESSING
    )

    /**
     * 获取状态的显示文本
     */
    fun getStatusText(): String = when (status) {
        TaskStatus.PENDING -> "Pending"
        TaskStatus.MARKING -> "Marking"
        TaskStatus.PROCESSING -> "Processing"
        TaskStatus.COMPLETED -> "Completed"
        TaskStatus.FAILED -> "Failed"
    }

    companion object {
        /**
         * 将圆圈列表转换为JSON字符串
         * @param circles 圆圈列表
         * @return JSON字符串
         */
        fun circleSelectionsToJson(circles: List<CircleSelection>): String {
            return Json.encodeToString(circles)
        }
    }
}
