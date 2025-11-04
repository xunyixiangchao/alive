package com.example.alive.util

/**
 * FrameHistoryManager - 圈选帧历史管理工具类（支持多图片）
 *
 * 设计思路：在原有单图片逻辑基础上，用"图片栈"的概念扩展多图支持
 * 核心思想：将多张图片的历史看作一个"栈的栈"，每个图片是一个独立的历史管理
 *
 * 职责：
 * - 管理用户國选过程中的帧历史导航
 * - 支持多张图片的独立历史管理
 * - 支持撤销（返回前一帧）和前进（返回后一帧）功能
 * - 支持图片切换，自动处理跨图片的撤销
 *
 * 工作流程：
 * 1. 调用 init(imageId, initialFrame) 初始化或切换到某张图片
 * 2. 用户圈选，调用 push(frame) 添加新帧
 * 3. 用户撤销，调用 undo() 返回前一帧
 * 4. 如果当前图片撤销到初始状态且有前一张图，自动切换到前一张图
 *
 * @param T 帧数据的泛型类型（例如 List<CircleSelection> 等）
 */
class FrameHistoryManager<T> {

    /**
     * 单张图片的历史记录
     * 这个数据类保持了原有的单图逻辑结构
     */
    private data class SingleImageHistory<T>(
        val imageId: String,
        val history: MutableList<T>,
        var currentPosition: Int = 0
    )

    /**
     * 图片栈：保存多张图片的历史
     * 每个元素是一个独立的图片历史管理
     * 栈顶是当前正在编辑的图片
     */
    private val imageStack = mutableListOf<SingleImageHistory<T>>()

    /**
     * 当前编辑的图片 ID
     */
    private var currentImageId: String? = null

    /**
     * 初始化或切换到某张图片
     *
     * 核心逻辑：
     * 1. 检查该图片是否已在栈中
     * 2. 如果在：移到栈顶（作为当前编辑图片）
     * 3. 如果不在：创建新的历史记录加入栈
     *
     * @param imageId 图片唯一标识
     * @param initialFrame 该图片的初始帧（原始图片）
     */
    fun init(imageId: String, initialFrame: T) {
        // 查找该图片是否已存在
        val existingIndex = imageStack.indexOfFirst { it.imageId == imageId }

        if (existingIndex >= 0) {
            // 图片已存在：从栈中取出并加到栈顶
            val existing = imageStack.removeAt(existingIndex)
            imageStack.add(existing)
        } else {
            // 新图片：创建历史记录
            imageStack.add(
                SingleImageHistory(
                    imageId = imageId,
                    history = mutableListOf(initialFrame),
                    currentPosition = 0
                )
            )
        }

        currentImageId = imageId
    }

    /**
     * 添加新帧到当前图片的历史
     *
     * 逻辑保持与原有单图版本相同：
     * 1. 如果当前位置不在末尾，删除后续帧（清除前进历史）
     * 2. 添加新帧
     * 3. 更新位置为新帧
     *
     * @param frame 新帧数据
     */
    fun push(frame: T) {
        val current = getCurrentImage()
            ?: throw IllegalStateException("请先调用 init() 初始化图片")

        // 清除当前位置之后的所有帧
        if (current.currentPosition < current.history.size - 1) {
            current.history.removeRange(
                current.currentPosition + 1,
                current.history.size
            )
        }

        // 添加新帧
        current.history.add(frame)
        current.currentPosition = current.history.size - 1
    }

    /**
     * 撤销操作
     *
     * 新增逻辑（对比单图版本）：
     * 1. 先在当前图片中撤销
     * 2. 如果当前图片已回到初始状态（position=0），且有前一张图片
     * 3. 自动切换到前一张图片的末尾
     *
     * @return 撤销后的帧，如果无法撤销返回 null
     */
    fun undo(): T? {
        val current = getCurrentImage() ?: return null

        // 情况1：当前图片可以撤销（位置 > 0）
        if (current.currentPosition > 0) {
            current.currentPosition--
            return current.history[current.currentPosition]
        }

        // 情况2：当前图片已在初始状态，检查是否有前一张图片
        if (imageStack.size > 1) {
            // 获取前一张图片
            val previousImageIndex = imageStack.size - 2
            val previousImage = imageStack[previousImageIndex]

            // 切换到前一张图片
            // 将前一张图片移到栈顶
            imageStack.removeAt(previousImageIndex)
            imageStack.add(previousImage)
            currentImageId = previousImage.imageId

            // 如果前一张图片有历史，返回其最后一帧
            if (previousImage.currentPosition > 0) {
                return previousImage.history[previousImage.currentPosition]
            } else {
                // 前一张图片也在初始状态，递归撤销
                return undo()
            }
        }

        // 情况3：已经是最初始状态，无法撤销
        return null
    }

    /**
     * 前进操作
     * 逻辑与单图版本完全相同
     *
     * @return 前进后的帧，如果无法前进返回 null
     */
    fun forward(): T? {
        val current = getCurrentImage() ?: return null

        return if (current.currentPosition < current.history.size - 1) {
            current.currentPosition++
            current.history[current.currentPosition]
        } else {
            null
        }
    }

    /**
     * 检查是否可以撤销
     * 扩展逻辑：考虑前一张图片的历史
     */
    fun canUndo(): Boolean {
        val current = getCurrentImage() ?: return false

        // 当前图片可以撤销
        if (current.currentPosition > 0) return true

        // 当前图片在初始状态，检查是否有前一张图片的历史
        if (imageStack.size > 1) {
            val previous = imageStack[imageStack.size - 2]
            return previous.currentPosition > 0
        }

        return false
    }

    /**
     * 检查是否可以前进
     * 逻辑与单图版本相同
     */
    fun canForward(): Boolean {
        val current = getCurrentImage() ?: return false
        return current.currentPosition < current.history.size - 1
    }

    /**
     * 获取当前帧
     * 逻辑与单图版本相同
     */
    fun getCurrentFrame(): T {
        val current = getCurrentImage()
            ?: throw IllegalStateException("请先调用 init() 初始化图片")
        return current.history[current.currentPosition]
    }

    /**
     * 获取当前位置（用于 UI 显示）
     * 逻辑与单图版本相同
     */
    fun getCurrentPosition(): Int {
        val current = getCurrentImage() ?: return 0
        return current.currentPosition + 1
    }

    /**
     * 获取历史总帧数
     * 逻辑与单图版本相同
     */
    fun getHistorySize(): Int {
        val current = getCurrentImage() ?: return 0
        return current.history.size
    }

    /**
     * 获取当前图片 ID
     * 新增方法：用于多图片场景
     */
    fun getCurrentImageId(): String? = currentImageId

    /**
     * 获取保存的图片数量
     * 新增方法：用于多图片场景
     */
    fun getImageCount(): Int = imageStack.size

    /**
     * 清除当前图片的所有圈选（保留其他图片的历史）
     * 新增方法：用于多图片场景的清空操作
     */
    fun clearCurrentImage() {
        val current = getCurrentImage() ?: return

        // 只保留初始帧，删除所有圈选
        if (current.history.size > 1) {
            current.history.removeRange(1, current.history.size)
        }
        current.currentPosition = 0
    }

    /**
     * 清除所有图片的所有历史
     * 新增方法：完全重置
     */
    fun clearAll() {
        imageStack.clear()
        currentImageId = null
    }

    /**
     * 重置到初始状态（单图片时的逻辑，保持向后兼容）
     * 注意：此方法现在只影响当前图片
     */
    fun reset(newInitialFrame: T) {
        val current = getCurrentImage()
        if (current != null) {
            current.history.clear()
            current.history.add(newInitialFrame)
            current.currentPosition = 0
        } else {
            // 如果没有任何图片，创建新的
            init("default", newInitialFrame)
        }
    }

    /**
     * 获取调试信息
     */
    fun getDebugInfo(): String {
        val current = getCurrentImage()
        return if (current != null) {
            "Image: ${current.imageId}, " +
            "Position: ${getCurrentPosition()}/${getHistorySize()}, " +
            "CanUndo: ${canUndo()}, " +
            "CanForward: ${canForward()}, " +
            "ImageCount: ${getImageCount()}"
        } else {
            "未初始化"
        }
    }

    /**
     * 获取当前图片的历史（内部方法）
     */
    private fun getCurrentImage(): SingleImageHistory<T>? = imageStack.lastOrNull()
}
