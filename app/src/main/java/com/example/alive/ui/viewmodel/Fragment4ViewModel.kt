package com.example.alive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alive.data.entity.TaskStatus
import com.example.alive.data.repository.AliveRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragment4ViewModel - 任务等待界面的ViewModel
 *
 * 职责：
 * - 轮询后端检查任务处理状态
 * - 管理任务完成状态和结果图路径
 * - 更新本地数据库中的任务状态
 * - 处理轮询超时和错误情况
 * - 提供停止轮询的功能
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理（此处手动设置为true）
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 *
 * @param repository AliveRepository实例，用于网络请求和数据库操作
 */
class Fragment4ViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 任务完成标志
     * true表示任务已成功完成
     */
    val taskCompleted = MutableLiveData(false)

    /**
     * 结果图路径
     * 任务成功完成后，存储处理结果图的文件路径
     */
    val resultImagePath = MutableLiveData<String?>()

    /**
     * 轮询协程任务
     * 用于在需要时取消轮询操作
     */
    private var pollingJob: kotlinx.coroutines.Job? = null

    init {
        // 初始化时设置加载状态为true（正在等待）
        isLoading.value = true
    }

    /**
     * 轮询后端检查任务状态
     * @param taskId 要检查的任务ID
     * @param maxWaitTime 最大等待时间（毫秒），默认60秒
     *
     * 操作流程：
     * 1. 在协程中启动轮询循环
     * 2. 每2秒调用一次后端API检查任务状态
     * 3. 根据返回的状态做不同处理：
     *    - "completed": 保存结果图路径，设置任务完成标志，结束轮询
     *    - "failed": 设置错误消息，结束轮询
     *    - 其他状态: 继续轮询
     * 4. 如果达到最大等待时间，设置超时错误消息
     * 5. 捕获并处理可能的网络异常
     */
    fun pollTaskStatus(taskId: Long, maxWaitTime: Long = 60000) {
        pollingJob = viewModelScope.launch {
            val startTime = System.currentTimeMillis()

            // 轮询直到超时
            while (System.currentTimeMillis() - startTime < maxWaitTime) {
                try {
                    // 调用后端API查询任务状态
                    val response = repository.getTaskStatus(taskId)

                    when (response.status) {
                        "completed" -> {
                            // 任务已完成
                            resultImagePath.value = response.resultImagePath
                            taskCompleted.value = true
                            setLoading(false)
                            break
                        }
                        "failed" -> {
                            // 任务失败
                            setError(response.errorMessage ?: "Task failed")
                            setLoading(false)
                            break
                        }
                        else -> {
                            // 任务仍在处理中，继续轮询
                            delay(2000) // 每2秒检查一次
                        }
                    }
                } catch (e: Exception) {
                    // 捕获网络或其他异常
                    setError(e.message)
                    setLoading(false)
                    break
                }
            }

            // 如果循环正常结束（超时）
            if (System.currentTimeMillis() - startTime >= maxWaitTime) {
                setError("Polling timeout")
                setLoading(false)
            }
        }
    }

    /**
     * 停止任务状态轮询
     * 取消正在进行的轮询协程
     */
    fun stopPolling() {
        pollingJob?.cancel()
    }

    /**
     * 更新本地数据库中的任务状态
     * @param taskId 要更新的任务ID
     * @param status 新的任务状态
     * @param resultPath 结果图路径（可选）
     *
     * 操作流程：
     * 1. 从数据库中查询任务对象
     * 2. 如果是完成状态，记录完成时间
     * 3. 更新任务的状态、完成时间和结果图路径
     */
    fun updateTaskStatus(taskId: Long, status: TaskStatus, resultPath: String?) {
        viewModelScope.launch {
            try {
                // 从数据库获取任务对象
                val task = repository.getTaskById(taskId) ?: return@launch
                // 如果任务完成，记录完成时间
                val completedTime = if (status == TaskStatus.COMPLETED) System.currentTimeMillis() else null
                // 更新任务信息
                repository.updateTask(
                    task.copy(
                        status = status,
                        completedTime = completedTime,
                        resultImagePath = resultPath
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
