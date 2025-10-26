package com.example.alive.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alive.data.entity.AliveImage
import com.example.alive.data.entity.FrameData
import com.example.alive.data.entity.Task
import com.example.alive.data.repository.AliveRepository

/**
 * SharedViewModel - Fragment间的数据共享枢纽
 *
 * 职责：
 * - 存储跨Fragment的共享数据
 * - 提供Fragment之间的通信桥梁
 * - 管理当前选中的图片、任务、帧列表等全局状态
 *
 * 生命周期：
 * - Activity级别，在MainActivity中创建
 * - 所有Fragment共享同一个实例
 * - Activity销毁时SharedViewModel自动清理
 *
 * 重要原则：
 * - SharedViewModel使用LiveData/StateFlow，Fragment通过observe()订阅
 * - 不要在SharedViewModel中执行长时任务，只存储和传递数据
 * - 具体的业务逻辑应在各Fragment的ViewModel中实现
 *
 * 共享数据列表：
 * 1. currentImage - 当前选中的ALIVE图片
 * 2. extractedFrames - Fragment2提取的8个帧列表
 * 3. currentTaskId - 当前正在处理的任务ID
 * 4. currentTask - 当前任务的完整信息
 * 5. circleSelections - Fragment3中用户绘制的圆圈数据
 */
class SharedViewModel(
    private val repository: AliveRepository
) : ViewModel() {

    // ==================== LiveData定义 ====================

    /**
     * 当前选中的ALIVE图片
     * 来源：Fragment1选择后设置
     * 使用者：Fragment2（获取视频路径）、Fragment3（显示图片）、Repository（保存数据库）
     */
    private val _currentImage = MutableLiveData<AliveImage?>(null)
    val currentImage: LiveData<AliveImage?> = _currentImage

    /**
     * Fragment2提取的8个帧列表
     * 来源：Fragment2提取完成后设置
     * 使用者：Fragment3（显示参考帧）
     */
    private val _extractedFrames = MutableLiveData<List<FrameData>>(emptyList())
    val extractedFrames: LiveData<List<FrameData>> = _extractedFrames

    /**
     * 当前正在处理的任务ID
     * 来源：Fragment3提交后设置
     * 使用者：Fragment4（轮询状态）
     */
    private val _currentTaskId = MutableLiveData<Long?>(null)
    val currentTaskId: LiveData<Long?> = _currentTaskId

    /**
     * 当前任务的完整信息
     * 来源：Fragment4轮询完成或Fragment5获取
     * 使用者：Fragment5（显示结果）、TaskListFragment（查看详情）
     */
    private val _currentTask = MutableLiveData<Task?>(null)
    val currentTask: LiveData<Task?> = _currentTask

    // ==================== Setter方法 ====================

    /**
     * 设置当前选中的图片
     *
     * Fragment1调用此方法保存用户选择的图片
     *
     * @param image AliveImage对象
     */
    fun setCurrentImage(image: AliveImage) {
        _currentImage.value = image
    }

    /**
     * 设置提取的8个帧
     *
     * Fragment2调用此方法保存提取的帧列表
     *
     * @param frames 帧列表
     */
    fun setExtractedFrames(frames: List<FrameData>) {
        _extractedFrames.value = frames
    }

    /**
     * 设置当前任务ID
     *
     * Fragment3提交圈选后调用此方法保存返回的任务ID
     *
     * @param taskId 任务ID
     */
    fun setCurrentTaskId(taskId: Long) {
        _currentTaskId.value = taskId
    }

    /**
     * 设置当前任务信息
     *
     * Fragment4轮询完成或Fragment5需要显示任务详情时调用
     *
     * @param task Task对象
     */
    fun setCurrentTask(task: Task) {
        _currentTask.value = task
    }

    /**
     * 清除所有共享数据
     *
     * 用途：
     * - 用户回到主菜单后清理之前的流程数据
     * - 开始新的图片处理流程
     * - 避免残留的旧数据影响新流程
     */
    fun clearAllData() {
        _currentImage.value = null
        _extractedFrames.value = emptyList()
        _currentTaskId.value = null
        _currentTask.value = null
    }

    /**
     * 清除当前流程的数据（保留已完成的任务）
     *
     * 用途：
     * - Fragment5返回主菜单后，清除此次流程的中间数据
     * - 但保留currentTask用于展示最终结果
     */
    fun clearProcessData() {
        _currentImage.value = null
        _extractedFrames.value = emptyList()
        _currentTaskId.value = null
    }

    // ==================== 可选的辅助方法 ====================

    /**
     * 检查是否已选择图片
     *
     * @return true如果currentImage不为null
     */
    fun hasSelectedImage(): Boolean = _currentImage.value != null

    /**
     * 检查是否已提取帧
     *
     * @return true如果extractedFrames不为空
     */
    fun hasExtractedFrames(): Boolean = _extractedFrames.value?.isNotEmpty() == true

    /**
     * 检查是否有当前任务
     *
     * @return true如果currentTask不为null
     */
    fun hasCurrentTask(): Boolean = _currentTask.value != null

    /**
     * 重置到初始状态
     *
     * 用途：用户点击返回主菜单时调用
     */
    fun reset() {
        clearAllData()
    }
}
