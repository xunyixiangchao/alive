package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.FragmentHomeBinding
import com.example.alive.ui.viewmodel.HomeViewModel

/**
 * HomeFragment - 主菜单界面
 *
 * 职责：
 * - 显示应用的主菜单
 * - 提供两个主要选项的导航入口：
 *   1. "Start Process" - 开始新的图片处理流程（Fragment1）
 *   2. "View Tasks" - 查看任务历史（TaskListFragment）
 * - 简单的导航界面，不包含复杂的业务逻辑
 *
 * HomeViewModel说明：
 * - HomeViewModel是一个空实现，主要作用是满足BaseFragment的泛型参数要求
 * - HomeFragment的所有导航逻辑都在此Fragment中实现
 * - 未来如需添加业务逻辑（如菜单状态管理、统计数据）可在HomeViewModel中扩展
 */
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<HomeViewModel> {
        return HomeViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 设置"Start Process"按钮的点击事件
     * 2. 设置"View Tasks"按钮的点击事件
     */
    override fun setupUI() {
        // 设置"Start Process"按钮的点击事件
        binding.btnStartProcess.setOnClickListener {
            // 导航到Fragment1（图片选择）
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToFragment1()
            )
        }

        // 设置"View Tasks"按钮的点击事件
        binding.btnViewTasks.setOnClickListener {
            // 导航到TaskListFragment（任务列表）
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTaskListFragment()
            )
        }
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 对于HomeFragment，由于HomeViewModel是空实现，此方法通常不需要做任何事
     * 但仍需实现此抽象方法
     */
    override fun observeViewModel() {
        // HomeFragment不观察任何ViewModel数据
        // 所有的导航逻辑都在setupUI()中处理
    }
}
