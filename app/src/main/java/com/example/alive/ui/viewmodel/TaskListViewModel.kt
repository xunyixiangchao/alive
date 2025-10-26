package com.example.alive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.alive.data.entity.Task
import com.example.alive.data.entity.TaskStatus
import com.example.alive.data.repository.AliveRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * TaskListViewModel - 任务列表界面的ViewModel
 *
 * 职责：
 * - 从数据库加载并管理所有任务列表
 * - 维护已完成任务和待处理任务的分类列表
 * - 提供任务删除功能
 * - 提供任务收藏状态更新功能
 * - 通过任务ID查询特定任务
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 *
 * @param repository AliveRepository实例，用于数据库操作
 */
class TaskListViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 所有任务列表的StateFlow
     * 包含数据库中的全部任务，按创建时间排序
     * 使用StateFlow实现自动更新UI
     */
    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    val allTasks: StateFlow<List<Task>> = _allTasks

    /**
     * 已完成任务列表的StateFlow
     * 只包含状态为COMPLETED的任务
     */
    private val _completedTasks = MutableStateFlow<List<Task>>(emptyList())
    val completedTasks: StateFlow<List<Task>> = _completedTasks

    /**
     * 待处理任务列表的StateFlow
     * 包含状态为PENDING、MARKING或PROCESSING的任务
     */
    private val _pendingTasks = MutableStateFlow<List<Task>>(emptyList())
    val pendingTasks: StateFlow<List<Task>> = _pendingTasks

    /**
     * 初始化时加载所有任务数据
     */
    init {
        loadTasks()
    }

    /**
     * 从数据库加载任务并分类
     *
     * 操作流程：
     * 1. 从Repository获取任务Flow
     * 2. 收集任务列表并更新_allTasks
     * 3. 根据任务状态过滤并更新已完成任务列表
     * 4. 根据任务状态过滤并更新待处理任务列表
     */
    private fun loadTasks() {
        viewModelScope.launch {
            try {
                // 收集数据库中的任务Flow，自动更新
                repository.getAllTasks().collect { tasks ->
                    _allTasks.value = tasks
                    // 过滤出已完成的任务
                    _completedTasks.value = tasks.filter { it.status == TaskStatus.COMPLETED }
                    // 过滤出待处理的任务（包含PENDING、MARKING、PROCESSING状态）
                    _pendingTasks.value = tasks.filter {
                        it.status in listOf(TaskStatus.PENDING, TaskStatus.MARKING, TaskStatus.PROCESSING)
                    }
                }
            } catch (e: Exception) {
                // 捕获并记录错误
                setError(e.message)
            }
        }
    }

    /**
     * 删除指定任务
     * @param task 要删除的Task对象
     *
     * 删除操作会从数据库中永久移除该任务
     * Flow会自动更新，UI会收到新的任务列表
     */
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * 更新任务的收藏状态
     * @param taskId 要更新的任务ID
     * @param isFavorite 新的收藏状态，true为收藏，false为取消收藏
     *
     * 仅更新本地数据库中的收藏状态
     */
    fun updateTaskFavorite(taskId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateTaskFavorite(taskId, isFavorite)
            } catch (e: Exception) {
                setError(e.message)
            }
        }
    }

    /**
     * 根据任务ID获取任务对象
     * @param taskId 任务ID
     * @return 对应的Task对象，如果未找到则返回null
     *
     * 从当前内存中的allTasks列表中查找，不直接访问数据库
     */
    fun getTaskById(taskId: Long): Task? {
        return _allTasks.value.find { it.id == taskId }
    }
}
