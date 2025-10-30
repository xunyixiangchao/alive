package com.example.alive.util

/**
 * FrameHistoryManager - 圈选帧历史管理工具类
 *
 * 职责：
 * - 管理用户圈选过程中的帧历史导航
 * - 支持撤销（返回前一帧）和前进（返回后一帧）功能
 * - 跟踪当前位置和整体历史
 * - 计算撤销/前进按钮的启用/禁用状态
 *
 * 工作流程：
 * 1. 初始化时传入初始帧数据
 * 2. 用户每圈选一个圆形，调用 push(frame) 添加新帧到历史
 * 3. 用户点击撤销时，调用 undo() 返回前一帧
 * 4. 用户点击前进时，调用 forward() 返回后一帧
 * 5. 根据 canUndo() 和 canForward() 的返回值控制按钮启用状态
 *
 * @param T 帧数据的泛型类型（例如 CircleSelection, FrameData 等）
 */
class FrameHistoryManager<T>(initialFrame: T) {

    /**
     * 帧历史栈，使用 LinkedList 实现，支持快速的头部和尾部操作
     */
    private val history = mutableListOf<T>()

    /**
     * 当前位置指针，指向 history 中的当前帧
     * - 初始值：0（指向初始帧）
     * - 范围：0 到 history.size - 1
     */
    private var currentPosition = 0

    init {
        // 将初始帧添加到历史
        history.add(initialFrame)
    }

    /**
     * 添加新帧到历史中
     *
     * 行为：
     * 1. 如果当前位置不在末尾，删除当前位置之后的所有帧（清除"前进"历史）
     * 2. 将新帧添加到历史末尾
     * 3. 将当前位置移动到新帧
     *
     * 例子：
     * - 历史: [A, B, C, D], 当前位置: 1 (B)
     * - 用户圈选新帧 E，调用 push(E)
     * - 结果: [A, B, E], 当前位置: 2 (E)
     * - C 和 D 被删除（因为已经执行了撤销操作）
     *
     * @param frame 要添加到历史的新帧
     */
    fun push(frame: T) {
        // 清除当前位置之后的所有帧
        if (currentPosition < history.size - 1) {
            history.removeRange(currentPosition + 1, history.size)
        }
        // 添加新帧
        history.add(frame)
        currentPosition = history.size - 1
    }

    /**
     * 撤销操作，返回前一帧
     *
     * 行为：
     * - 如果当前位置 > 0，将位置向前移动一步
     * - 返回新的当前帧
     * - 如果已在第一帧，返回 null（表示无法撤销）
     *
     * 使用前需要调用 canUndo() 检查
     *
     * @return 前一帧数据，如果无法撤销返回 null
     */
    fun undo(): T? {
        return if (currentPosition > 0) {
            currentPosition--
            history[currentPosition]
        } else {
            null
        }
    }

    /**
     * 前进操作，返回后一帧
     *
     * 行为：
     * - 如果当前位置 < history.size - 1，将位置向后移动一步
     * - 返回新的当前帧
     * - 如果已在最后一帧，返回 null（表示无法前进）
     *
     * 使用前需要调用 canForward() 检查
     *
     * @return 后一帧数据，如果无法前进返回 null
     */
    fun forward(): T? {
        return if (currentPosition < history.size - 1) {
            currentPosition++
            history[currentPosition]
        } else {
            null
        }
    }

    /**
     * 检查是否可以撤销
     *
     * 返回值：
     * - true: 当前位置 > 0，撤销按钮应该启用
     * - false: 当前位置 == 0，撤销按钮应该禁用（已在第一帧）
     *
     * @return 是否可以撤销
     */
    fun canUndo(): Boolean = currentPosition > 0

    /**
     * 检查是否可以前进
     *
     * 返回值：
     * - true: 当前位置 < history.size - 1，前进按钮应该启用
     * - false: 当前位置 == history.size - 1，前进按钮应该禁用（已在最后一帧）
     *
     * @return 是否可以前进
     */
    fun canForward(): Boolean = currentPosition < history.size - 1

    /**
     * 获取当前帧
     *
     * @return 当前位置的帧数据
     */
    fun getCurrentFrame(): T = history[currentPosition]

    /**
     * 获取当前位置（用于 UI 显示，例如 "第 2/8 帧"）
     *
     * @return 当前位置（1-based，即第 1、2、3 帧）
     */
    fun getCurrentPosition(): Int = currentPosition + 1

    /**
     * 获取历史总帧数
     *
     * @return 历史中的总帧数
     */
    fun getHistorySize(): Int = history.size

    /**
     * 获取全部历史帧列表（仅供调试或特殊需求）
     *
     * @return 历史帧列表的副本
     */
    fun getHistory(): List<T> = history.toList()

    /**
     * 清空历史，重置到初始状态
     *
     * @param newInitialFrame 新的初始帧
     */
    fun reset(newInitialFrame: T) {
        history.clear()
        history.add(newInitialFrame)
        currentPosition = 0
    }

    /**
     * 获取调试信息（显示当前状态）
     *
     * @return 调试字符串，格式："Position: 1/8, CanUndo: false, CanForward: true"
     */
    fun getDebugInfo(): String {
        return "Position: ${getCurrentPosition()}/${getHistorySize()}, " +
               "CanUndo: ${canUndo()}, " +
               "CanForward: ${canForward()}"
    }
}
