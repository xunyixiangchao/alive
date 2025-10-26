package com.example.alive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alive.data.entity.Task
import com.example.alive.databinding.ItemTaskBinding

/**
 * TaskListAdapter - RecyclerView适配器，用于显示任务列表
 *
 * 用途：
 * - TaskListFragment中显示所有任务
 * - 支持过滤显示不同状态的任务
 *
 * 功能：
 * - 显示任务ID、状态、创建时间
 * - 支持删除任务（滑动或按钮）
 * - 支持收藏/取消收藏
 * - 点击任务查看详情
 *
 * @param tasks 初始的任务列表
 * @param onDeleteTask 删除任务的回调
 * @param onFavoriteTask 更新收藏状态的回调
 * @param onTaskClick 点击任务的回调
 */
class TaskListAdapter(
    private var tasks: List<Task> = emptyList(),
    private val onDeleteTask: (Task) -> Unit,
    private val onFavoriteTask: (Long, Boolean) -> Unit,
    private val onTaskClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    /**
     * TaskViewHolder - 单个任务的视图容器
     */
    inner class TaskViewHolder(
        private val binding: ItemTaskBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * 绑定任务数据到视图
         */
        fun bind(task: Task) {
            // 显示任务ID
            binding.tvTaskId.text = "Task #${task.id}"

            // 显示任务状态
            binding.tvTaskStatus.text = task.getStatusText()

            // 显示创建时间
            val createdTime = formatTimestamp(task.createdAt)
            binding.tvCreatedTime.text = "Created: $createdTime"

            // 设置收藏按钮状态
            binding.btnFavorite.text = if (task.isFavorite) "★" else "☆"

            // 设置收藏按钮点击事件
            binding.btnFavorite.setOnClickListener {
                onFavoriteTask(task.id, !task.isFavorite)
            }

            // 设置删除按钮点击事件
            binding.btnDelete.setOnClickListener {
                onDeleteTask(task)
            }

            // 设置整个项目的点击事件
            binding.root.setOnClickListener {
                onTaskClick(task)
            }

            // 如果有结果图路径，加载缩略图
            if (!task.resultImagePath.isNullOrEmpty()) {
                Glide.with(binding.root)
                    .load(task.resultImagePath)
                    .centerCrop()
                    .into(binding.ivTaskThumbnail)
            }
        }
    }

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    /**
     * 绑定数据到ViewHolder
     */
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    /**
     * 获取列表项数量
     */
    override fun getItemCount(): Int = tasks.size

    /**
     * 更新任务列表
     * @param newTasks 新的任务列表
     */
    fun updateTasks(newTasks: List<Task>) {
        tasks = newTasks
        notifyDataSetChanged()
    }

    /**
     * 格式化时间戳为可读的日期时间字符串
     * @param timestamp 时间戳（毫秒）
     * @return 格式化的日期时间字符串
     */
    private fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }
}
