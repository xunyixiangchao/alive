package com.example.alive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alive.data.entity.CircleSelection
import com.example.alive.data.repository.AliveRepository
import com.google.gson.Gson

/**
 * Fragment3ViewModel - 手动圈选界面的ViewModel
 *
 * 职责：
 * - 提交用户在各帧上标记的圆圈國选数据到后端
 * - 管理國选提交过程中的加载状态
 * - 处理提交结果和错误情况
 * - 将國选数据转换为JSON格式供后端处理
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 * - launchAsync: 异步操作框架
 *
 * @param repository AliveRepository实例，用于网络请求操作
 */
class Fragment3ViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 提交成功标志
     * true表示國选数据已成功提交到后端并开始处理
     */
    val submissionSuccess = MutableLiveData(false)

    /**
     * 提交圆圈國选数据到后端进行目标去除处理
     * @param taskId 当前任务的ID
     * @param markedAliveImagePath 标记后的Alive图路径
     * @param circleSelectionsMap 國选数据映射，Key为帧索引，Value为该帧上的國选列表
     *
     * 使用BaseViewModel提供的launchAsync方法处理异步操作
     */
    fun submitCircleRemoval(
        taskId: Long,
        markedAliveImagePath: String,
        circleSelectionsMap: Map<Int, List<CircleSelection>>
    ) {
        launchAsync(
            operation = {
                // 将國选数据转换为JSON格式
                val selectionsJson = Gson().toJson(circleSelectionsMap)
                // 调用后端API提交國选去除请求
                repository.submitCircleRemoval(
                    taskId = taskId,
                    aliveImagePath = markedAliveImagePath,
                    selectionsJson = selectionsJson
                )
            },
            onSuccess = { response ->
                if (response.status == "success") {
                    // 提交成功
                    submissionSuccess.value = true
                } else {
                    // API返回失败状态
                    setError(response.message ?: "Failed to submit removal request")
                }
            }
        )
    }
}
