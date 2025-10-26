package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.alive.databinding.Fragment5Binding
import com.example.alive.ui.viewmodel.Fragment5ViewModel
import com.bumptech.glide.Glide

/**
 * Fragment5 - 结果展示界面
 *
 * 职责：
 * - 显示图片处理的最终结果
 * - 提供分享结果图的功能
 * - 提供下载结果图到本地相册的功能
 * - 支持收藏/取消收藏任务
 * - 显示任务的基本信息（创建时间、处理时间等）
 * - 提供返回主菜单的选项
 *
 * 工作流程：
 * 1. 加载并显示处理后的结果图
 * 2. 显示任务信息（如果有）
 * 3. 用户可点击分享按钮分享结果图到其他应用
 * 4. 用户可点击下载按钮将结果图保存到本地相册
 * 5. 用户可点击收藏按钮收藏该任务
 * 6. 用户点击返回或完成后返回主菜单
 */
class Fragment5 : BaseFragment<Fragment5Binding, Fragment5ViewModel>() {

    /**
     * 创建ViewBinding实例
     */
    override fun createViewBinding(): Fragment5Binding {
        return Fragment5Binding.inflate(layoutInflater)
    }

    /**
     * 返回ViewModel的Class对象
     */
    override fun getViewModelClass(): Class<Fragment5ViewModel> {
        return Fragment5ViewModel::class.java
    }

    /**
     * 设置UI组件和事件监听器
     *
     * 主要操作：
     * 1. 加载并显示结果图
     * 2. 设置分享、下载、收藏按钮的点击事件
     * 3. 显示任务信息
     */
    override fun setupUI() {
        // 获取当前任务信息
        val currentTask = sharedViewModel.currentTask.value

        if (currentTask != null) {
            // 显示任务信息
            binding.tvTaskInfo.text = "Task ID: ${currentTask.id}\nStatus: ${currentTask.status}"
            binding.tvTaskInfo.visibility = View.VISIBLE

            // 加载结果图到ImageView
            if (!currentTask.resultImagePath.isNullOrEmpty()) {
                Glide.with(this)
                    .load(currentTask.resultImagePath)
                    .into(binding.ivResultImage)
            }

            // 初始化收藏状态
            viewModel.isFavorite.value = currentTask.isFavorite
        }

        // 设置"分享"按钮的点击事件
        binding.btnShare.setOnClickListener {
            val currentTask = sharedViewModel.currentTask.value
            if (currentTask != null && !currentTask.resultImagePath.isNullOrEmpty()) {
                // 调用ViewModel的分享方法
                viewModel.shareResultImage(
                    requireContext(),
                    currentTask.resultImagePath
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "No result image to share",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 设置"下载"按钮的点击事件
        binding.btnDownload.setOnClickListener {
            val currentTask = sharedViewModel.currentTask.value
            if (currentTask != null && !currentTask.resultImagePath.isNullOrEmpty()) {
                // 调用ViewModel的下载方法
                viewModel.downloadResultImage(
                    requireContext(),
                    currentTask.resultImagePath,
                    "result_${currentTask.id}.jpg"
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "No result image to download",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 设置"收藏"按钮的点击事件
        binding.btnFavorite.setOnClickListener {
            val currentTask = sharedViewModel.currentTask.value
            if (currentTask != null) {
                // 切换收藏状态
                val newFavoriteStatus = !(viewModel.isFavorite.value ?: false)
                viewModel.updateFavoriteStatus(currentTask.id, newFavoriteStatus)
            }
        }

        // 设置"完成/返回主菜单"按钮的点击事件
        binding.btnDone.setOnClickListener {
            // 返回到主菜单
            findNavController().popBackStack(
                com.example.alive.R.id.homeFragment,
                false
            )
        }
    }

    /**
     * 观察ViewModel中的数据变化
     *
     * 观察的主要数据：
     * 1. isFavorite - 收藏状态
     * 2. downloadSuccess - 下载成功标志
     * 3. isLoading - 加载状态
     * 4. errorMessage - 错误消息
     */
    override fun observeViewModel() {
        // 观察收藏状态
        viewModel.isFavorite.observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.btnFavorite.text = "☆ Unfavorite"
            } else {
                binding.btnFavorite.text = "★ Favorite"
            }
        }

        // 观察下载成功状态
        viewModel.downloadSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                // 显示下载成功提示
                Toast.makeText(
                    requireContext(),
                    "Image downloaded to Pictures/Alive",
                    Toast.LENGTH_SHORT
                ).show()
                // 重置下载状态
                viewModel.downloadSuccess.value = false
            }
        }

        // 观察加载状态（分享、下载、收藏操作）
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                // 禁用所有按钮，显示进度条
                binding.progressBar.visibility = View.VISIBLE
                binding.btnShare.isEnabled = false
                binding.btnDownload.isEnabled = false
                binding.btnFavorite.isEnabled = false
            } else {
                // 启用所有按钮，隐藏进度条
                binding.progressBar.visibility = View.GONE
                binding.btnShare.isEnabled = true
                binding.btnDownload.isEnabled = true
                binding.btnFavorite.isEnabled = true
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
