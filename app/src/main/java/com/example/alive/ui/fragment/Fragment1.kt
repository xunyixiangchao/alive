package com.example.alive.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.alive.data.entity.AliveImage
import com.example.alive.databinding.Fragment1Binding
import com.example.alive.ui.viewmodel.Fragment1ViewModel

/**
 * Fragment1 - 图片选择界面
 *
 * 职责：
 * - 提供图片/视频选择界面
 * - 用户通过点击按钮调起系统选择器选择ALIVE图片或视频
 * - 将选中的图片/视频信息保存到数据库
 * - 更新SharedViewModel中的currentImage，为后续流程提供数据
 * - 显示加载状态和错误提示
 *
 * 用户交互流程：
 * 1. 点击 "Select Image" 按钮
 * 2. 系统文件选择器打开
 * 3. 选择图片/视频文件
 * 4. 文件路径被保存到数据库
 * 5. UI显示已选择的文件信息
 * 6. 自动跳转到Fragment2（8帧提取）
 */
class Fragment1 : BaseFragment<Fragment1Binding, Fragment1ViewModel>() {

    /**
     * 活动结果契约：选择视觉媒体（图片或视频）
     * 使用ActivityResultContracts.PickVisualMedia的新API（需Android 5.0+）
     *
     * registerForActivityResult的回调在用户选择文件后调用
     * result.data包含选中文件的URI
     */
    private val pickMediaLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        // 用户从选择器返回时调用
        if (uri != null) {
            // 获取文件路径
            val filePath = uri.toString()

            // 创建AliveImage实体，包含URI和获取时间戳
            val aliveImage = AliveImage(
                uri = filePath,
                name = uri.lastPathSegment ?: "unknown",
                timestamp = System.currentTimeMillis()
            )

            // 调用ViewModel保存到数据库
            viewModel.saveSelectedImage(aliveImage)
        } else {
            // 用户取消了选择
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 创建ViewBinding实例
     * 子类实现此方法为BaseFragment提供类型安全的View绑定
     */
    override fun createViewBinding(): Fragment1Binding {
        return Fragment1Binding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     * 用于BaseFragment中的ViewModelProvider创建对应的ViewModel实例
     */
    override fun getViewModelClass(): Class<Fragment1ViewModel> {
        return Fragment1ViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 在此方法中：
     * 1. 初始化UI组件的状态
     * 2. 设置点击事件监听器
     * 3. 配置RecyclerView等列表组件（如果有）
     * 4. 其他UI相关的初始化
     */
    override fun setupUI() {
        // 设置"选择图片"按钮的点击事件
        binding.btnSelectImage.setOnClickListener {
            // 打开系统文件选择器，允许选择图片或视频
            pickMediaLauncher.launch(
                ActivityResultContracts.PickVisualMedia.ImageAndVideo
            )
        }

        // 设置"下一步"按钮的点击事件
        binding.btnNext.setOnClickListener {
            // 检查是否已选择了图片
            val selectedImage = viewModel.selectedImage.value
            if (selectedImage != null) {
                // 将选中的图片保存到SharedViewModel
                sharedViewModel.setCurrentImage(selectedImage)

                // 导航到Fragment2（8帧提取）
                // 使用Navigation组件的navController进行导航
                findNavController().navigate(
                    FragmentDirections.actionFragment1ToFragment2()
                )
            } else {
                // 提示用户需要先选择图片
                Toast.makeText(
                    requireContext(),
                    "Please select an image first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 设置"返回"按钮的点击事件
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 在此方法中观察ViewModel的LiveData或StateFlow
     * 根据数据变化更新UI或执行其他操作
     *
     * 观察的数据包括：
     * 1. selectedImage - 当前选中的图片，用于显示选择结果
     * 2. isLoading - 加载状态，用于显示/隐藏进度条
     * 3. errorMessage - 错误消息，用于显示错误提示
     */
    override fun observeViewModel() {
        // 观察选中的图片数据
        viewModel.selectedImage.observe(viewLifecycleOwner) { image ->
            if (image != null) {
                // 显示已选择的文件名
                binding.tvSelectedFileName.text = "Selected: ${image.name}"
                binding.tvSelectedFileName.visibility = View.VISIBLE
            }
        }

        // 观察加载状态（从BaseViewModel继承）
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 显示进度条
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSelectImage.isEnabled = false
                binding.btnNext.isEnabled = false
            } else {
                // 隐藏进度条
                binding.progressBar.visibility = View.GONE
                binding.btnSelectImage.isEnabled = true
                binding.btnNext.isEnabled = true
            }
        }

        // 观察错误消息（从BaseViewModel继承）
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                // 显示错误提示
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                // 清除错误消息
                viewModel.clearError()
            }
        }
    }
}
