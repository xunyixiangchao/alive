package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.FragmentTaskListBinding
import com.example.alive.ui.adapter.TaskListAdapter
import com.example.alive.ui.viewmodel.TaskListViewModel
import kotlinx.coroutines.launch

/**
 * TaskListFragment - 任务列表界面
 *
 * 职责：
 * - 显示所有已提交的任务列表
 * - 支持过滤：查看已完成任务、待处理任务或所有任务
 * 3. 支持删除任务
 * - 支持收藏/取消收藏任务
 * - 点击任务可查看任务详情和结果
 * - 自动刷新任务列表（通过Flow观察）
 *
 * 工作流程：
 * 1. Fragment加载时，ViewModel自动从数据库加载所有任务
 * 2. 任务列表在RecyclerView中显示
 * 3. 用户可使用底部选项卡过滤任务（All / Pending / Completed）
 * 4. 用户可滑动删除任务或点击查看详情
 * 5. 数据库中的任务变化自动反映在UI中（Flow的reactive特性）
 *
 * 数据来源：
 * - TaskListViewModel使用StateFlow管理三个列表：
 *   - allTasks: 所有任务
 *   - completedTasks: 已完成的任务
 *   - pendingTasks: 待处理的任务
 * - 这些StateFlow自动同步于数据库，无需手动刷新
 */
class TaskListFragment : BaseFragment<FragmentTaskListBinding, TaskListViewModel>() {

    /**
     * RecyclerView适配器
     */
    private lateinit var taskListAdapter: TaskListAdapter

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): FragmentTaskListBinding {
        return FragmentTaskListBinding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<TaskListViewModel> {
        return TaskListViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 配置RecyclerView和适配器
     * 2. 设置底部选项卡过滤功能
     * 3. 设置返回按钮
     */
    override fun setupUI() {
        // 创建任务列表适配器
        // 传入删除和收藏回调函数
        taskListAdapter = TaskListAdapter(
            tasks = emptyList(),
            onDeleteTask = { task ->
                // 删除任务的回调
                viewModel.deleteTask(task)
                Toast.makeText(requireContext(), "Task deleted", Toast.LENGTH_SHORT).show()
            },
            onFavoriteTask = { taskId, isFavorite ->
                // 更新收藏状态的回调
                viewModel.updateTaskFavorite(taskId, isFavorite)
            },
            onTaskClick = { task ->
                // 点击任务查看详情的回调
                if (task.status == com.example.alive.data.entity.TaskStatus.COMPLETED) {
                    // 如果任务已完成，可以导航到结果详情页面
                    sharedViewModel.setCurrentTask(task)
                    findNavController().navigate(
                        TaskListFragmentDirections.actionTaskListFragmentToFragment5()
                    )
                } else {
                    // 如果任务未完成，显示提示
                    Toast.makeText(
                        requireContext(),
                        "Task is still processing",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )

        // 配置RecyclerView
        binding.recyclerViewTasks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = taskListAdapter
        }

        // 设置底部选项卡过滤功能
        binding.chipGroupFilter.setOnCheckedChangeListener { chipGroup, checkedId ->
            when (checkedId) {
                binding.chipAllTasks.id -> {
                    // 显示所有任务
                    viewModel.allTasks.value?.let { tasks ->
                        taskListAdapter.updateTasks(tasks)
                    }
                }
                binding.chipPendingTasks.id -> {
                    // 显示待处理任务
                    viewModel.pendingTasks.value?.let { tasks ->
                        taskListAdapter.updateTasks(tasks)
                    }
                }
                binding.chipCompletedTasks.id -> {
                    // 显示已完成任务
                    viewModel.completedTasks.value?.let { tasks ->
                        taskListAdapter.updateTasks(tasks)
                    }
                }
            }
        }

        // 设置"返回"按钮的点击事件
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 初始化显示所有任务
        binding.chipAllTasks.isChecked = true
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 观察的主要数据：
     * 1. allTasks - 所有任务列表
     * 2. completedTasks - 已完成任务列表
     * 3. pendingTasks - 待处理任务列表
     * 4. isLoading - 加载状态
     * 5. errorMessage - 错误消息
     */
    override fun observeViewModel() {
        // 观察所有任务列表
        lifecycleScope.launch {
            viewModel.allTasks.collect { tasks ->
                // 如果当前过滤器是"All Tasks"，更新适配器
                if (binding.chipAllTasks.isChecked) {
                    taskListAdapter.updateTasks(tasks)
                }
            }
        }

        // 观察已完成任务列表
        lifecycleScope.launch {
            viewModel.completedTasks.collect { tasks ->
                // 如果当前过滤器是"Completed Tasks"，更新适配器
                if (binding.chipCompletedTasks.isChecked) {
                    taskListAdapter.updateTasks(tasks)
                }
            }
        }

        // 观察待处理任务列表
        lifecycleScope.launch {
            viewModel.pendingTasks.collect { tasks ->
                // 如果当前过滤器是"Pending Tasks"，更新适配器
                if (binding.chipPendingTasks.isChecked) {
                    taskListAdapter.updateTasks(tasks)
                }
            }
        }

        // 观察加载状态
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        // 观察错误消息
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }
}
