package com.example.alive.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alive.data.repository.AliveRepository

/**
 * SharedViewModelFactory - SharedViewModel的工厂类
 *
 * 职责：
 * - 创建SharedViewModel实例时注入Repository
 * - 遵循工厂模式和依赖注入原则
 *
 * 使用方式：
 * MainActivity中使用此Factory创建SharedViewModel
 * ```kotlin
 * private val sharedViewModel: SharedViewModel by viewModels {
 *     SharedViewModelFactory(getRepository())
 * }
 * ```
 *
 * @param repository AliveRepository实例
 */
class SharedViewModelFactory(
    private val repository: AliveRepository
) : ViewModelProvider.Factory {

    /**
     * 创建ViewModel实例
     *
     * @param modelClass ViewModel的Class对象
     * @return 创建的ViewModel实例
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> {
                SharedViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
