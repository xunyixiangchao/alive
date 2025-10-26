package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.Fragment4Binding
import com.example.alive.data.entity.TaskStatus
import com.example.alive.ui.viewmodel.Fragment4ViewModel

/**
 * Fragment4 - 任务处理轮询界面
 *
 * 职责：
 * - 显示任务正在处理中的状态界面
 * - 定时轮询后端API查询任务状态
 * - 当任务完成时自动跳转到Fragment5显示结果
 * - 显示当前任务的处理进度和状态
 * - 提供"重新轮询"手动刷新选项（可选）
 * - 错误处理和重试机制
 *
 * 工作流程：
 * 1. Fragment显示"Processing..."提示
 * 2. ViewModel中的轮询协程开始定期检查任务状态（每2秒一次）
 * 3. 当status=COMPLETED时，自动跳转到Fragment5
 * 4. 当status=FAILED时，显示错误提示
 * 5. 用户可点击"返回"按钮，轮询会自动取消（协程清理）
 *
 * 轮询机制：
 * - 轮询Job保存在ViewModel中
 * - Fragment销毁时，ViewModel自动清理协程（viewModelScope）
 * - 轮询间隔：2秒
 * - 超时重试：无限重试，直到任务完成或用户返回
 */
class Fragment4 : BaseFragment<Fragment4Binding, Fragment4ViewModel>() {

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): Fragment4Binding {
        return Fragment4Binding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<Fragment4ViewModel> {
        return Fragment4ViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 显示处理中的提示信息
     * 2. 设置"返回"按钮事件
     * 3. 启动任务状态轮询
     */
    override fun setupUI() {
        // 显示处理中的状态提示
        binding.tvStatus.text = "Processing your request..."
        binding.tvStatus.visibility = View.VISIBLE

        // 显示进度条
        binding.progressBar.visibility = View.VISIBLE

        // 设置"返回"按钮的点击事件
        binding.btnBack.setOnClickListener {
            // 返回时轮询会自动取消（由ViewModel的viewModelScope管理）
            findNavController().navigateUp()
        }

        // 启动任务状态轮询
        val currentTaskId = sharedViewModel.currentTaskId.value
        if (currentTaskId != null && currentTaskId > 0) {
            viewModel.startPolling(currentTaskId)
        } else {
            // 如果任务ID未设置，显示错误
            Toast.makeText(
                requireContext(),
                "Task ID not found",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 观察的主要数据：
     * 1. currentTaskStatus - 当前任务状态
     * 2. isLoading - 加载状态（轮询中状态）
     * 3. errorMessage - 轮询中的错误信息
     */
    override fun observeViewModel() {
        // 观察任务状态变化
        viewModel.currentTaskStatus.observe(viewLifecycleOwner) { status ->
            when (status) {
                TaskStatus.PENDING -> {
                    binding.tvStatus.text = "Task pending..."
                    binding.progressBar.visibility = View.VISIBLE
                }
                TaskStatus.MARKING -> {
                    binding.tvStatus.text = "Marking regions..."
                    binding.progressBar.visibility = View.VISIBLE
                }
                TaskStatus.PROCESSING -> {
                    binding.tvStatus.text = "Processing image..."
                    binding.progressBar.visibility = View.VISIBLE
                }
                TaskStatus.COMPLETED -> {
                    // 任务处理完成
                    binding.tvStatus.text = "Task completed!"
                    binding.progressBar.visibility = View.GONE

                    // 获取完成的任务信息
                    val taskId = sharedViewModel.currentTaskId.value
                    if (taskId != null) {
                        val task = repository.getTaskById(taskId)
                        if (task != null) {
                            // 将完成的任务保存到SharedViewModel
                            sharedViewModel.setCurrentTask(task)

                            // 自动导航到Fragment5显示结果
                            Toast.makeText(
                                requireContext(),
                                "Redirecting to results...",
                                Toast.LENGTH_SHORT
                            ).show()

                            // 使用视图跳转到Fragment5
                            view?.postDelayed({
                                findNavController().navigate(
                                    FragmentDirections.actionFragment4ToFragment5()
                                )
                            }, 500)
                        }
                    }
                }
                TaskStatus.FAILED -> {
                    // 任务处理失败
                    binding.tvStatus.text = "Task failed"
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "Task processing failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // 观察加载状态（轮询中）
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                // 轮询完成（但任务可能未完成）
                // 进度条可能已在上面的status观察中处理
            }
        }

        // 观察错误消息（轮询中发生的错误）
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                // 显示轮询错误，但不中断轮询过程
                Toast.makeText(
                    requireContext(),
                    "Polling error: $errorMsg (retrying...)",
                    Toast.LENGTH_SHORT
                ).show()

                // 清除错误消息，继续轮询
                viewModel.clearError()
            }
        }
    }
}
