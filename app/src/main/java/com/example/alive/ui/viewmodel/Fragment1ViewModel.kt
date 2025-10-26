package com.example.alive.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alive.data.entity.AliveImage
import com.example.alive.data.repository.AliveRepository

/**
 * Fragment1ViewModel - 图片选择界面的ViewModel
 *
 * 职责：
 * - 处理用户选择的Alive图像
 * - 将选择的图像保存到本地数据库
 * - 管理图像保存过程中的加载状态和错误信息
 *
 * 继承自BaseViewModel，自动获得：
 * - isLoading: 加载状态管理
 * - errorMessage: 错误消息管理
 * - clearError(): 错误清除方法
 * - setLoading/setError: 状态设置方法
 * - launchAsync: 异步操作框架
 *
 * @param repository AliveRepository实例，用于数据持久化操作
 */
class Fragment1ViewModel(
    repository: AliveRepository
) : BaseViewModel(repository) {

    /**
     * 当前选择的Alive图像对象
     * 在用户从图库或相机选择图片并成功保存后更新
     */
    val selectedImage = MutableLiveData<AliveImage?>()

    /**
     * 保存用户选择的Alive图像到数据库
     * @param aliveImage 要保存的AliveImage对象，包含图像路径和元数据
     *
     * 使用BaseViewModel提供的launchAsync方法，简化异步操作代码
     * 自动处理：
     * 1. 加载状态设置
     * 2. 异常捕获和错误消息设置
     * 3. 最后清除加载状态
     */
    fun saveSelectedImage(aliveImage: AliveImage) {
        launchAsync(
            operation = {
                // 插入图像到数据库并获取生成的ID
                repository.insertAliveImage(aliveImage)
            },
            onSuccess = { id ->
                // 插入成功，更新selectedImage为带有数据库ID的完整对象
                selectedImage.value = aliveImage.copy(id = id)
            }
        )
    }
}
