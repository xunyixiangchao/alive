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
 * - 本地提取选中视频的8帧（不需要API）
 * - 获取图片上传地址
 * - 上传图片到服务器
 * - 将选中的图片信息保存到数据库
 * - 显示加载状态和错误提示
 *
 * 完整的工作流程：
 * 1. 点击"选择图片"按钮
 * 2. 系统文件选择器打开
 * 3. 用户选择图片/视频
 * 4. 自动执行完整流程：
 *    - 保存图片到数据库
 *    - 本地提取8帧
 *    - 获取上传地址
 *    - 上传图片到服务器
 * 5. UI显示进度和完成状态
 * 6. 上传成功后跳转到Fragment2
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

            // 调用ViewModel执行完整流程：保存 → 抽帧 → 获取上传地址 → 上传
            viewModel.executeCompleteWorkflow(aliveImage, requireContext())
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
            // 检查是否已成功上传了图片
            val uploadedUrl = viewModel.uploadedImageUrl.value
            if (!uploadedUrl.isNullOrEmpty()) {
                // 将选中的图片和提取的帧保存到SharedViewModel
                viewModel.selectedImage.value?.let { image ->
                    sharedViewModel.setCurrentImage(image)
                }
                viewModel.extractedFrames.value?.let { frames ->
                    sharedViewModel.setExtractedFrames(frames)
                }

                // 导航到Fragment2（原来的8帧提取已在Fragment1完成）
                // 或可选择直接进入Fragment3（圈选）
                findNavController().navigate(
                    FragmentDirections.actionFragment1ToFragment2()
                )
            } else {
                // 提示用户需要先完成上传
                Toast.makeText(
                    requireContext(),
                    "Please select and upload an image first",
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
     * 1. selectedImage - 当前选中的图片
     * 2. extractedFrames - 本地提取的8帧
     * 3. uploadUrl - 获取到的上传地址
     * 4. uploadedImageUrl - 上传后的图片URL
     * 5. currentWorkflowStep - 当前工作流程步骤
     * 6. workflowStepDescription - 步骤描述
     * 7. isLoading - 加载状态
     * 8. errorMessage - 错误消息
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

        // 观察提取的帧数据
        viewModel.extractedFrames.observe(viewLifecycleOwner) { frames ->
            if (frames.isNotEmpty()) {
                // 显示已提取帧的数量
                binding.tvStatus.text = "✓ Step 2: Extracted ${frames.size} frames locally"
                binding.tvStatus.visibility = View.VISIBLE
            }
        }

        // 观察上传地址
        viewModel.uploadUrl.observe(viewLifecycleOwner) { url ->
            if (url.isNotEmpty()) {
                // 显示已获取上传地址
                binding.tvStatus.text = "✓ Step 3: Got upload URL"
                binding.tvStatus.visibility = View.VISIBLE
            }
        }

        // 观察上传后的图片URL
        viewModel.uploadedImageUrl.observe(viewLifecycleOwner) { url ->
            if (url.isNotEmpty()) {
                // 显示上传成功
                binding.tvStatus.text = "✓ Step 4: Image uploaded successfully"
                binding.tvStatus.visibility = View.VISIBLE
                // 启用"下一步"按钮
                binding.btnNext.isEnabled = true
            }
        }

        // 观察工作流程步骤（详细进度显示）
        viewModel.currentWorkflowStep.observe(viewLifecycleOwner) { step ->
            when (step) {
                0 -> {
                    // 未开始或错误后重置
                    binding.tvProgressStep.text = ""
                    binding.tvProgressStep.visibility = View.GONE
                }
                1 -> {
                    binding.tvProgressStep.text = "▶ Step 1/4: Saving image to database..."
                    binding.tvProgressStep.visibility = View.VISIBLE
                }
                2 -> {
                    binding.tvProgressStep.text = "▶ Step 2/4: Extracting frames locally..."
                    binding.tvProgressStep.visibility = View.VISIBLE
                }
                3 -> {
                    binding.tvProgressStep.text = "▶ Step 3/4: Getting upload URL..."
                    binding.tvProgressStep.visibility = View.VISIBLE
                }
                4 -> {
                    binding.tvProgressStep.text = "▶ Step 4/4: Uploading image..."
                    binding.tvProgressStep.visibility = View.VISIBLE
                }
                5 -> {
                    binding.tvProgressStep.text = "✓ All steps completed!"
                    binding.tvProgressStep.visibility = View.VISIBLE
                }
            }
        }

        // 观察步骤描述
        viewModel.workflowStepDescription.observe(viewLifecycleOwner) { description ->
            if (description.isNotEmpty()) {
                // 也可以在tvStatus中显示详细描述
                // binding.tvStatus.text = description
            }
        }

        // 观察加载状态（从BaseViewModel继承）
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 显示进度条和处理中提示
                binding.progressBar.visibility = View.VISIBLE
                binding.tvStatus.text = "Processing..."
                binding.tvStatus.visibility = View.VISIBLE
                binding.btnSelectImage.isEnabled = false
                binding.btnNext.isEnabled = false
            } else {
                // 隐藏进度条
                binding.progressBar.visibility = View.GONE
                binding.btnSelectImage.isEnabled = true
            }
        }

        // 观察错误消息（从BaseViewModel继承）
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                // 显示错误提示
                binding.tvStatus.text = "✗ $errorMsg"
                binding.tvStatus.visibility = View.VISIBLE
                binding.tvProgressStep.visibility = View.GONE  // 隐藏进度步骤
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()

                // 提供重试选项（可选）
                // 用户可以点击"选择图片"按钮重新开始
                // 或提供一个"重试"按钮

                // 清除错误消息
                viewModel.clearError()
            }
        }
    }
}
