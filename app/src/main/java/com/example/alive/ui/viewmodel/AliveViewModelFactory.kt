package com.example.alive.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alive.data.repository.AliveRepository

/**
 * AliveViewModelFactory - 通用的ViewModel工厂类
 *
 * 职责：
 * - 为所有Fragment的ViewModel创建实例
 * - 在创建时注入Repository依赖
 * - 支持BaseFragment中的通用ViewModel创建逻辑
 *
 * 支持的ViewModel类型：
 * - Fragment1ViewModel
 * - Fragment2ViewModel
 * - Fragment3ViewModel
 * - Fragment4ViewModel
 * - Fragment5ViewModel
 * - TaskListViewModel
 * - HomeViewModel
 *
 * 使用方式：
 * BaseFragment中使用此Factory创建ViewModel
 * ```kotlin
 * private fun initializeViewModel() {
 *     val factory = AliveViewModelFactory(repository)
 *     viewModel = ViewModelProvider(this, factory)[getViewModelClass()]
 * }
 * ```
 *
 * @param repository AliveRepository实例，用于注入到ViewModel
 */
class AliveViewModelFactory(
    private val repository: AliveRepository
) : ViewModelProvider.Factory {

    /**
     * 创建ViewModel实例
     *
     * @param modelClass ViewModel的Class对象
     * @return 创建的ViewModel实例
     * @throws IllegalArgumentException 如果ViewModel类型不被支持
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            // Fragment1ViewModel
            modelClass.isAssignableFrom(Fragment1ViewModel::class.java) -> {
                Fragment1ViewModel(repository) as T
            }
            // Fragment2ViewModel
            modelClass.isAssignableFrom(Fragment2ViewModel::class.java) -> {
                Fragment2ViewModel(repository) as T
            }
            // Fragment3ViewModel
            modelClass.isAssignableFrom(Fragment3ViewModel::class.java) -> {
                Fragment3ViewModel(repository) as T
            }
            // Fragment4ViewModel
            modelClass.isAssignableFrom(Fragment4ViewModel::class.java) -> {
                Fragment4ViewModel(repository) as T
            }
            // Fragment5ViewModel
            modelClass.isAssignableFrom(Fragment5ViewModel::class.java) -> {
                Fragment5ViewModel(repository) as T
            }
            // TaskListViewModel
            modelClass.isAssignableFrom(TaskListViewModel::class.java) -> {
                TaskListViewModel(repository) as T
            }
            // HomeViewModel（不需要Repository）
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel() as T
            }
            // 不支持的ViewModel类型
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
