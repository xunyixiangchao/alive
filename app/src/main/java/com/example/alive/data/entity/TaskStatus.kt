package com.example.alive.data.entity

/**
 * TaskStatus - 任务状态枚举
 *
 * 定义任务在处理流程中的各种状态
 * 状态流转：PENDING → MARKING → PROCESSING → COMPLETED/FAILED
 */
enum class TaskStatus {
    /**
     * 待处理 - 任务刚创建，等待处理
     */
    PENDING,

    /**
     * 标记中 - 系统正在识别和标记人物区域
     */
    MARKING,

    /**
     * 处理中 - 系统正在进行人物消除处理
     */
    PROCESSING,

    /**
     * 已完成 - 处理完成，有可用的结果图
     */
    COMPLETED,

    /**
     * 已失败 - 处理失败，无结果
     */
    FAILED
}
