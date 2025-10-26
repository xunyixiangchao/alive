package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.Fragment3Binding
import com.example.alive.ui.adapter.FrameAdapter
import com.example.alive.ui.viewmodel.Fragment3ViewModel

/**
 * Fragment3 - 圈选人物界面
 *
 * 职责：
 * - 显示用户可以手绘圆圈进行人物圈选的界面
 * - 使用CircleDrawingImageView支持用户在图片上绘制红色圆圈
 * - 在下方显示从Fragment2提取的8个帧作为参考
 * - 用户可在多个圈之间选择
 * - 提交圈选结果到后端服务，创建任务
 * - 显示提交过程的加载状态和结果
 *
 * 工作流程：
 * 1. 显示原始ALIVE图片（用户在此可绘制红色圆圈）
 * 2. 用户通过触摸在图片上绘制圆圈来标记人物区域
 * 3. 下方RecyclerView显示参考的8帧
 * 4. 用户点击"提交"按钮，圈选数据被转换为JSON并上传
 * 5. 后端创建Task并返回taskId
 * 6. 跳转到Fragment4进行任务轮询
 */
class Fragment3 : BaseFragment<Fragment3Binding, Fragment3ViewModel>() {

    /**
     * RecyclerView适配器，显示参考的8帧
     */
    private lateinit var frameAdapter: FrameAdapter

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): Fragment3Binding {
        return Fragment3Binding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<Fragment3ViewModel> {
        return Fragment3ViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 加载原始ALIVE图片到CircleDrawingImageView
     * 2. 配置参考帧的RecyclerView
     * 3. 设置"提交"和"返回"按钮的事件
     */
    override fun setupUI() {
        // 获取当前选中的图片
        val currentImage = sharedViewModel.currentImage.value
        if (currentImage != null) {
            // 将图片加载到CircleDrawingImageView
            // 使用Glide进行异步加载
            com.bumptech.glide.Glide.with(this)
                .load(currentImage.uri)
                .into(binding.circleDrawingImageView)
        }

        // 初始化RecyclerView适配器（显示8帧参考）
        frameAdapter = FrameAdapter(emptyList())

        // 配置RecyclerView
        binding.recyclerViewFrames.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = frameAdapter
        }

        // 设置"提交"按钮的点击事件
        binding.btnSubmit.setOnClickListener {
            // 从CircleDrawingImageView获取用户绘制的圆圈数据
            val circleSelections = binding.circleDrawingImageView.getCircles()

            if (circleSelections.isNotEmpty()) {
                // 调用ViewModel提交圈选数据
                viewModel.submitCircleRemoval(
                    aliveImageId = sharedViewModel.currentImage.value?.id ?: 0L,
                    circleSelections = circleSelections
                )
            } else {
                // 提示用户需要先绘制至少一个圆圈
                Toast.makeText(
                    requireContext(),
                    "Please draw at least one circle",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 设置"返回"按钮的点击事件
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 将参考帧加载到RecyclerView中
        val extractedFrames = sharedViewModel.extractedFrames.value
        if (!extractedFrames.isNullOrEmpty()) {
            frameAdapter.updateFrames(extractedFrames)
        }
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 观察的主要数据：
     * 1. submittedTaskId - 提交后返回的任务ID
     * 2. isLoading - 加载状态（显示/隐藏进度条）
     * 3. errorMessage - 错误消息（显示错误提示）
     */
    override fun observeViewModel() {
        // 观察提交任务的结果
        viewModel.submittedTaskId.observe(viewLifecycleOwner) { taskId ->
            if (taskId != null && taskId > 0) {
                // 任务创建成功
                // 将taskId保存到SharedViewModel
                sharedViewModel.setCurrentTaskId(taskId)

                // 显示成功提示
                Toast.makeText(
                    requireContext(),
                    "Task created successfully",
                    Toast.LENGTH_SHORT
                ).show()

                // 导航到Fragment4进行任务轮询
                findNavController().navigate(
                    FragmentDirections.actionFragment3ToFragment4()
                )
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 显示进度条
                binding.progressBar.visibility = View.VISIBLE
                binding.btnSubmit.isEnabled = false
            } else {
                // 隐藏进度条
                binding.progressBar.visibility = View.GONE
                binding.btnSubmit.isEnabled = true
            }
        }

        // 观察错误消息
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
