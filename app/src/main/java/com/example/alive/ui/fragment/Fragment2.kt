package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.Fragment2Binding
import com.example.alive.ui.adapter.FrameAdapter
import com.example.alive.ui.viewmodel.Fragment2ViewModel

/**
 * Fragment2 - 8帧提取界面
 *
 * 职责：
 * - 显示从用户选择的ALIVE图片/视频中本地提取的8帧
 * - 使用RecyclerView展示帧列表
 * - 显示帧提取的加载状态
 * - 处理用户继续操作（跳转到Fragment3进行圈选）
 * - 错误处理和重试机制
 *
 * 工作流程：
 * 1. onViewCreated时自动调用ViewModel的extract8Frames()
 * 2. ViewModel使用VideoUtils本地提取8帧（不调用API）
 * 3. 8帧保存到应用缓存目录
 * 4. 帧列表显示在RecyclerView中
 * 5. 用户点击"下一步"后跳转到Fragment3
 *
 * 本地帧提取策略（由VideoUtils实现）：
 * - 首帧（0ms）
 * - 末帧（视频总时长）
 * - 中间6帧（均匀分布在7个等分点）
 * - 总计8帧
 */
class Fragment2 : BaseFragment<Fragment2Binding, Fragment2ViewModel>() {

    /**
     * RecyclerView适配器，用于显示提取的帧
     * 将延迟初始化，在setupUI()中创建
     */
    private lateinit var frameAdapter: FrameAdapter

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): Fragment2Binding {
        return Fragment2Binding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<Fragment2ViewModel> {
        return Fragment2ViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 配置RecyclerView和适配器
     * 2. 设置"下一步"和"返回"按钮的事件监听器
     * 3. 触发8帧提取操作
     */
    override fun setupUI() {
        // 初始化RecyclerView适配器
        frameAdapter = FrameAdapter(emptyList())

        // 配置RecyclerView
        binding.recyclerViewFrames.apply {
            // 使用线性布局管理器，方向为水平
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = frameAdapter
        }

        // 设置"下一步"按钮的点击事件
        binding.btnNext.setOnClickListener {
            // 检查是否已成功提取帧
            val frames = viewModel.extractedFrames.value
            if (!frames.isNullOrEmpty()) {
                // 将提取的帧保存到SharedViewModel
                sharedViewModel.setExtractedFrames(frames)

                // 导航到Fragment3（圈选界面）
                findNavController().navigate(
                    FragmentDirections.actionFragment2ToFragment3()
                )
            } else {
                // 提示用户帧提取失败或未完成
                Toast.makeText(
                    requireContext(),
                    "Please wait for frame extraction to complete",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 设置"返回"按钮的点击事件
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 开始提取8帧（ViewModel会自动在init块中调用，或在这里显式调用）
        // 如果ViewModel的init中已调用过，这里可以不再调用
        viewModel.extract8Frames(
            imageUri = sharedViewModel.currentImage.value?.uri ?: "",
            context = requireContext()
        )
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 观察的主要数据：
     * 1. extractedFrames - 提取的帧列表
     * 2. isLoading - 加载状态（显示/隐藏进度条）
     * 3. errorMessage - 错误消息（显示错误提示）
     */
    override fun observeViewModel() {
        // 观察提取的帧列表
        viewModel.extractedFrames.observe(viewLifecycleOwner) { frames ->
            if (frames != null && frames.isNotEmpty()) {
                // 更新RecyclerView适配器中的帧数据
                frameAdapter.updateFrames(frames)

                // 显示提取完成的提示
                binding.tvExtractionStatus.text = "Successfully extracted ${frames.size} frames"
                binding.tvExtractionStatus.visibility = View.VISIBLE
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 显示进度条和提取中提示
                binding.progressBar.visibility = View.VISIBLE
                binding.tvExtractionStatus.text = "Extracting frames..."
                binding.tvExtractionStatus.visibility = View.VISIBLE
                binding.btnNext.isEnabled = false
            } else {
                // 隐藏进度条
                binding.progressBar.visibility = View.GONE
                binding.btnNext.isEnabled = true
            }
        }

        // 观察错误消息
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                // 显示错误提示
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                binding.tvExtractionStatus.text = "Error: $errorMsg"
                binding.tvExtractionStatus.visibility = View.VISIBLE

                // 清除错误消息
                viewModel.clearError()
            }
        }
    }
}
