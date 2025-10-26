package com.example.alive.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alive.data.entity.FrameData
import com.example.alive.databinding.ItemFrameBinding

/**
 * FrameAdapter - RecyclerView适配器，用于显示视频帧列表
 *
 * 用途：
 * - Fragment2中显示提取的8帧列表
 * - Fragment3中显示参考帧列表
 *
 * 功能：
 * - 使用Glide加载帧图像
 * - 显示帧号
 * - 支持动态更新帧数据
 *
 * @param frames 初始的帧数据列表
 */
class FrameAdapter(
    private var frames: List<FrameData> = emptyList()
) : RecyclerView.Adapter<FrameAdapter.FrameViewHolder>() {

    /**
     * FrameViewHolder - 单个帧的视图容器
     */
    inner class FrameViewHolder(
        private val binding: ItemFrameBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        /**
         * 绑定帧数据到视图
         */
        fun bind(frameData: FrameData) {
            // 显示帧号
            binding.tvFrameNumber.text = frameData.getFrameNumberText()

            // 使用Glide加载帧图像
            Glide.with(binding.root)
                .load(frameData.filePath)
                .centerCrop()
                .into(binding.ivFrame)
        }
    }

    /**
     * 创建ViewHolder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrameViewHolder {
        val binding = ItemFrameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FrameViewHolder(binding)
    }

    /**
     * 绑定数据到ViewHolder
     */
    override fun onBindViewHolder(holder: FrameViewHolder, position: Int) {
        holder.bind(frames[position])
    }

    /**
     * 获取列表项数量
     */
    override fun getItemCount(): Int = frames.size

    /**
     * 更新帧数据列表
     * @param newFrames 新的帧列表
     */
    fun updateFrames(newFrames: List<FrameData>) {
        frames = newFrames
        notifyDataSetChanged()
    }
}
