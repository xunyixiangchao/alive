package com.example.alive.network.api

import com.example.alive.data.entity.TaskStatus
import com.example.alive.network.dto.CircleRemovalResponse
import com.example.alive.network.dto.FavoriteResponse
import com.example.alive.network.dto.TaskStatusResponse
import kotlinx.coroutines.delay

/**
 * MockAliveApi - AliveApi的Mock实现
 *
 * 用途：
 * - 开发阶段测试UI和业务逻辑，不需要后端服务
 * - 模拟网络延迟和各种处理状态
 * - 测试轮询、错误处理等流程
 *
 * 使用方式：
 * 在ApiClient.getApi()中返回mockApi即可使用Mock实现
 * 改为返回retrofitApi则使用真实HTTP实现
 *
 * Mock特性：
 * - 模拟网络延迟（每个请求延迟500-1000ms）
 * - 任务轮询模拟：PENDING → MARKING → PROCESSING → COMPLETED
 * - 每次轮询自动推进状态
 * - 模拟收藏状态变更
 */
class MockAliveApi : AliveApi {

    /**
     * 模拟内存中的任务轮询状态
     * Key: taskId, Value: 当前状态索引
     */
    private val taskStatusMap = mutableMapOf<Long, Int>()

    /**
     * 任务状态轮询序列
     * 每次调用getTaskStatus时自动递进
     */
    private val statusSequence = listOf(
        TaskStatus.PENDING,
        TaskStatus.MARKING,
        TaskStatus.PROCESSING,
        TaskStatus.PROCESSING,
        TaskStatus.PROCESSING,
        TaskStatus.COMPLETED
    )

    /**
     * 提交圈选数据（Mock实现）
     *
     * 模拟过程：
     * 1. 延迟500ms模拟网络请求
     * 2. 生成随机taskId（范围：1000-9999）
     * 3. 初始化该任务的轮询状态
     * 4. 返回taskId
     */
    override suspend fun submitCircleRemoval(
        aliveImageId: Long,
        circleSelectionsJson: String
    ): CircleRemovalResponse {
        // 模拟网络延迟
        delay(500)

        // 生成模拟的taskId
        val taskId = (1000..9999).random().toLong()

        // 初始化该任务的轮询状态索引
        taskStatusMap[taskId] = 0

        // 返回Mock响应
        return CircleRemovalResponse(
            taskId = taskId,
            message = "Task created successfully (Mock)"
        )
    }

    /**
     * 获取任务处理状态（Mock实现）
     *
     * 模拟轮询过程：
     * 1. 延迟1000ms模拟处理时间
     * 2. 获取该任务的当前状态
     * 3. 推进状态索引到下一个（自动进度推进）
     * 4. 当status=COMPLETED时，返回模拟的结果图路径
     */
    override suspend fun getTaskStatus(taskId: Long): TaskStatusResponse {
        // 模拟网络延迟和处理时间
        delay(1000)

        // 获取或初始化该任务的状态索引
        val currentIndex = taskStatusMap.getOrDefault(taskId, 0)

        // 获取当前状态
        val currentStatus = if (currentIndex < statusSequence.size) {
            statusSequence[currentIndex]
        } else {
            TaskStatus.COMPLETED
        }

        // 推进状态到下一个
        if (currentIndex < statusSequence.size - 1) {
            taskStatusMap[taskId] = currentIndex + 1
        }

        // 计算处理进度（0-100）
        val progress = ((currentIndex + 1) * 100) / statusSequence.size

        // 如果任务完成，返回模拟的结果图路径
        val resultImagePath = if (currentStatus == TaskStatus.COMPLETED) {
            "/data/user/0/com.example.alive/files/results/task_${taskId}_result.jpg"
        } else {
            null
        }

        return TaskStatusResponse(
            taskId = taskId,
            status = currentStatus,
            progress = progress,
            message = "Processing... (Mock)",
            resultImagePath = resultImagePath
        )
    }

    /**
     * 更新收藏状态（Mock实现）
     *
     * 模拟过程：
     * 1. 延迟300ms模拟网络请求
     * 2. 直接返回新的收藏状态
     */
    override suspend fun updateFavorite(
        taskId: Long,
        isFavorite: Boolean
    ): FavoriteResponse {
        // 模拟网络延迟
        delay(300)

        // 返回Mock响应
        return FavoriteResponse(
            taskId = taskId,
            isFavorite = if (isFavorite) 1 else 0
        )
    }

    /**
     * 重置所有Mock状态（用于测试）
     */
    fun reset() {
        taskStatusMap.clear()
    }
}
