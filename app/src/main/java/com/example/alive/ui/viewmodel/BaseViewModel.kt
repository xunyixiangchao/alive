package com.example.alive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alive.data.repository.AliveRepository
import kotlinx.coroutines.launch

/**
 * BaseViewModel - 所有ViewModel的基类
 *
 * 职责：
 * - 提供通用的加载状态管理
 * - 提供统一的错误消息处理
 * - 提供异步操作的通用框架
 * - 消除各个ViewModel中的重复代码
 *
 * 特点：
 * - 所有子类自动获得isLoading和errorMessage属性
 * - 提供clearError()方法统一清除错误状态
 * - 提供setLoading()和setError()方法方便设置状态
 * - 提供launchAsync()方法简化协程操作的模板代码
 *
 * @param repository AliveRepository实例，用于数据操作
 */
abstract class BaseViewModel(
    protected val repository: AliveRepository
) : ViewModel() {

    /**
     * 加载状态标志
     * true表示正在执行异步操作，false表示操作完成或空闲
     * 子类可直接使用此属性，无需重复定义
     */
    val isLoading = MutableLiveData(false)

    /**
     * 错误消息
     * 当异步操作失败时存储错误信息，成功或未发生错误时为null
     * 子类可直接使用此属性，无需重复定义
     */
    val errorMessage = MutableLiveData<String?>()

    /**
     * 清除错误消息
     * 通常在用户确认看到错误提示后调用
     * 这是一个通用方法，所有ViewModel都能使用
     */
    fun clearError() {
        errorMessage.value = null
    }

    /**
     * 设置加载状态
     * @param loading true表示开始加载，false表示加载结束
     */
    protected fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    /**
     * 设置错误消息
     * @param error 错误消息，如果为null则表示清除错误
     */
    protected fun setError(error: String?) {
        errorMessage.value = error
    }

    /**
     * 执行异步操作的通用方法
     *
     * 此方法封装了异步操作的通用流程：
     * 1. 设置加载状态为true（如果showLoading为true）
     * 2. 在viewModelScope协程中执行operation
     * 3. 操作成功后调用onSuccess回调
     * 4. 操作失败时自动捕获异常并设置错误消息
     * 5. 最后将加载状态设为false（如果showLoading为true）
     *
     * 使用此方法可大幅简化ViewModel中的异步代码
     *
     * @param T 异步操作的返回值类型
     * @param operation 要执行的异步操作，返回类型为T
     * @param onSuccess 操作成功后的回调，接收操作结果
     * @param showLoading 是否显示加载状态，默认为true
     *
     * 示例：
     * ```
     * launchAsync(
     *     operation = { repository.fetchData() },
     *     onSuccess = { data -> resultData.value = data },
     *     showLoading = true
     * )
     * ```
     */
    protected fun <T> launchAsync(
        operation: suspend () -> T,
        onSuccess: (T) -> Unit,
        showLoading: Boolean = true
    ) {
        if (showLoading) setLoading(true)

        viewModelScope.launch {
            try {
                // 执行异步操作
                val result = operation()
                // 操作成功，调用回调函数处理结果
                onSuccess(result)
            } catch (e: Exception) {
                // 操作失败，自动设置错误消息
                setError(e.message ?: "Unknown error occurred")
            } finally {
                // 最后清除加载状态
                if (showLoading) setLoading(false)
            }
        }
    }

    /**
     * 执行异步操作的简化版本（无返回值）
     *
     * 当异步操作不需要返回值时，可使用此方法简化代码
     *
     * @param operation 要执行的异步操作
     * @param showLoading 是否显示加载状态，默认为true
     * @param onSuccess 操作成功后的回调（无参数）
     *
     * 示例：
     * ```
     * launchAsyncUnit(
     *     operation = { repository.deleteData() },
     *     onSuccess = { /* 数据删除成功 */ }
     * )
     * ```
     */
    protected fun launchAsyncUnit(
        operation: suspend () -> Unit,
        showLoading: Boolean = true,
        onSuccess: (() -> Unit)? = null
    ) {
        launchAsync(
            operation = operation,
            onSuccess = { onSuccess?.invoke() },
            showLoading = showLoading
        )
    }
}
