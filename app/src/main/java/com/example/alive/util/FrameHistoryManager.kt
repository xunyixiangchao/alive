package com.example.alive.util

/**
 * FrameHistoryManager - 多图片圈选帧历史管理工具类
 *
 * 职责：
 * - 管理用户圈选过程中的帧历史导航
 * - 支持多张图片的圈选，每张图片独立的历史栈
 * - 支持撤销（返回前一帧）和前进（返回后一帧）功能
 * - 支持图片切换，切换时保存当前图片的历史
 * - 跟踪当前位置和整体历史
 * - 计算撤销/前进按钮的启用/禁用状态
 *
 * 工作流程（多图片场景）：
 * 1. 初始化第一张图片，调用 newImage(imageId, initialFrame)
 * 2. 用户在第一张图上圈选，每次调用 push(frame) 添加新帧
 * 3. 用户点击撤销，调用 undo() 返回前一帧
 * 4. 用户撤销到初始状态后，可以选择第二张图片
 * 5. 调用 newImage(imageId2, initialFrame2)，自动保存第一张的历史
 * 6. 在第二张图上圈选，重复步骤 2-3
 * 7. 任何时刻点击撤销，返回到第一张图的最后状态，继续撤销回到初始状态
 *
 * @param T 帧数据的泛型类型（例如 List<CircleSelection> 等）
 */
class FrameHistoryManager<T> {

    /**
     * 单张图片的历史记录
     *
     * @param imageId 图片唯一标识
     * @param history 该图片的帧历史列表
     * @param currentPosition 当前位置指针
     */
    data class ImageHistory<T>(
        val imageId: String,
        val history: MutableList<T>,
        var currentPosition: Int = 0
    )

    /**
     * 图片历史栈，保存每张图片的历史
     * 栈顶是当前正在编辑的图片
     */
    private val imageStack = mutableListOf<ImageHistory<T>>()

    /**
     * 当前编辑的图片 ID
     */
    private var currentImageId: String? = null

    /**
     * 初始化新图片或切换到新图片
     *
     * 行为：
     * 1. 如果当前有正在编辑的图片，先保存其历史
     * 2. 检查新图片是否已在栈中
     *    - 如果在：切换到该图片的历史
     *    - 如果不在：创建新的历史记录
     * 3. 更新当前图片 ID
     *
     * 场景示例：
     * - 初始化第一张图：newImage("img1", emptyList())
     *   状态：栈 = [ImageHistory(img1, [empty], 0)]
     *
     * - 圈选后回退到初始状态，选择第二张图：newImage("img2", emptyList())
     *   状态：栈 = [ImageHistory(img1, [...], 0), ImageHistory(img2, [empty], 0)]
     *
     * - 在第二张圈选后，撤销回到初始状态，再选择第一张：newImage("img1", ...)
     *   状态：切换到第一张的历史，img1 变为栈顶
     *
     * @param imageId 新图片的唯一标识
     * @param initialFrame 新图片的初始帧（原始图片，无圆圈）
     */
    fun newImage(imageId: String, initialFrame: T) {
        // 检查该图片是否已存在于栈中
        val existingIndex = imageStack.indexOfFirst { it.imageId == imageId }

        if (existingIndex >= 0) {
            // 图片已存在，直接切换到该图片
            // 将其移到栈顶
            val imageHistory = imageStack.removeAt(existingIndex)
            imageStack.add(imageHistory)
        } else {
            // 新图片，创建新的历史记录
            imageStack.add(ImageHistory(imageId, mutableListOf(initialFrame), 0))
        }

        currentImageId = imageId
    }

    /**
     * 添加新帧到当前图片的历史中
     *
     * 行为：
     * 1. 如果当前位置不在末尾，删除当前位置之后的所有帧（清除"前进"历史）
     * 2. 将新帧添加到历史末尾
     * 3. 将当前位置移动到新帧
     *
     * 例子（单张图片）：
     * - 历史: [A, B, C, D], 当前位置: 1 (B)
     * - 用户圈选新帧 E，调用 push(E)
     * - 结果: [A, B, E], 当前位置: 2 (E)
     *
     * @param frame 要添加到历史的新帧
     * @throws IllegalStateException 如果没有初始化任何图片
     */
    fun push(frame: T) {
        val currentImage = getCurrentImageHistory()
            ?: throw IllegalStateException("没有初始化任何图片，请先调用 newImage()")

        // 清除当前位置之后的所有帧
        if (currentImage.currentPosition < currentImage.history.size - 1) {
            currentImage.history.removeRange(
                currentImage.currentPosition + 1,
                currentImage.history.size
            )
        }

        // 添加新帧
        currentImage.history.add(frame)
        currentImage.currentPosition = currentImage.history.size - 1
    }

    /**
     * 撤销操作，返回前一帧
     *
     * 行为：
     * 1. 在当前图片的历史中，位置向前移动一步
     * 2. 如果当前图片已撤销到初始状态（position = 0），返回 UndoResult.CantUndo
     * 3. 如果撤销到初始状态，返回 UndoResult.ReachedInitialFrame
     * 4. 如果撤销到前一张图片，返回 UndoResult.SwitchToPreviousImage
     *
     * @return 撤销结果，包含新的帧数据和状态信息
     */
    fun undo(): UndoResult<T> {
        val currentImage = getCurrentImageHistory()
            ?: return UndoResult.CantUndo("没有初始化任何图片")

        // 尝试在当前图片中撤销
        if (currentImage.currentPosition > 0) {
            currentImage.currentPosition--
            val frame = currentImage.history[currentImage.currentPosition]

            // 检查是否已回到初始状态
            if (currentImage.currentPosition == 0) {
                return UndoResult.ReachedInitialFrame(frame, currentImage.imageId)
            }

            return UndoResult.Success(frame)
        }

        // 当前图片已在初始状态，尝试回退到前一张图片
        if (imageStack.size > 1) {
            // 获取前一张图片
            val previousImageIndex = imageStack.size - 2
            val previousImage = imageStack[previousImageIndex]

            // 如果前一张图片有历史（位置不在初始），返回其最后一帧
            if (previousImage.currentPosition > 0) {
                currentImageId = previousImage.imageId
                // 不改变位置，直接返回当前帧
                return UndoResult.SwitchToPreviousImage(
                    previousImage.history[previousImage.currentPosition],
                    previousImage.imageId
                )
            } else {
                // 前一张图片也在初始状态，继续回退
                // 移除当前图片，切换到前一张
                imageStack.removeAt(imageStack.size - 1)
                currentImageId = previousImage.imageId
                return UndoResult.SwitchToPreviousImage(
                    previousImage.history[previousImage.currentPosition],
                    previousImage.imageId
                )
            }
        }

        // 已在最初始状态，无法继续撤销
        return UndoResult.CantUndo("已在最初始状态")
    }

    /**
     * 前进操作，返回后一帧
     *
     * 行为：
     * - 在当前图片的历史中，位置向后移动一步
     * - 如果已在最后一帧，返回 null（表示无法前进）
     *
     * @return 后一帧数据，如果无法前进返回 null
     * @throws IllegalStateException 如果没有初始化任何图片
     */
    fun forward(): T? {
        val currentImage = getCurrentImageHistory()
            ?: throw IllegalStateException("没有初始化任何图片，请先调用 newImage()")

        return if (currentImage.currentPosition < currentImage.history.size - 1) {
            currentImage.currentPosition++
            currentImage.history[currentImage.currentPosition]
        } else {
            null
        }
    }

    /**
     * 检查是否可以撤销
     *
     * 返回值：
     * - true: 当前位置 > 0 或有前一张图片的历史
     * - false: 已在最初始状态且没有前一张图片
     *
     * @return 是否可以撤销
     */
    fun canUndo(): Boolean {
        val currentImage = getCurrentImageHistory() ?: return false

        // 当前图片不在初始状态
        if (currentImage.currentPosition > 0) return true

        // 当前图片在初始状态，检查是否有前一张图片
        if (imageStack.size > 1) {
            val previousImage = imageStack[imageStack.size - 2]
            // 只有当前一张图片有非初始状态的内容才能撤销
            return previousImage.currentPosition > 0
        }

        return false
    }

    /**
     * 检查是否可以前进
     *
     * 返回值：
     * - true: 当前位置 < history.size - 1
     * - false: 已在最后一帧
     *
     * @return 是否可以前进
     */
    fun canForward(): Boolean {
        val currentImage = getCurrentImageHistory() ?: return false
        return currentImage.currentPosition < currentImage.history.size - 1
    }

    /**
     * 获取当前帧
     *
     * @return 当前位置的帧数据
     * @throws IllegalStateException 如果没有初始化任何图片
     */
    fun getCurrentFrame(): T {
        val currentImage = getCurrentImageHistory()
            ?: throw IllegalStateException("没有初始化任何图片，请先调用 newImage()")
        return currentImage.history[currentImage.currentPosition]
    }

    /**
     * 获取当前图片 ID
     *
     * @return 当前图片的唯一标识
     */
    fun getCurrentImageId(): String? = currentImageId

    /**
     * 获取当前位置（用于 UI 显示，例如 "第 2/8 帧"）
     *
     * @return 当前位置（1-based，即第 1、2、3 帧）
     */
    fun getCurrentPosition(): Int {
        val currentImage = getCurrentImageHistory() ?: return 0
        return currentImage.currentPosition + 1
    }

    /**
     * 获取历史总帧数
     *
     * @return 当前图片的总帧数
     */
    fun getHistorySize(): Int {
        val currentImage = getCurrentImageHistory() ?: return 0
        return currentImage.history.size
    }

    /**
     * 获取栈中的图片数量
     *
     * @return 已保存的图片数量
     */
    fun getImageCount(): Int = imageStack.size

    /**
     * 清除当前图片的所有圈选，回到初始状态
     *
     * 行为：
     * - 将当前位置重置为 0（初始帧）
     * - 删除位置 1 之后的所有帧
     */
    fun clearCurrentImage() {
        val currentImage = getCurrentImageHistory() ?: return

        // 只保留初始帧
        if (currentImage.history.size > 1) {
            currentImage.history.removeRange(1, currentImage.history.size)
        }

        currentImage.currentPosition = 0
    }

    /**
     * 完全清空所有历史
     */
    fun clearAll() {
        imageStack.clear()
        currentImageId = null
    }

    /**
     * 获取调试信息（显示当前状态）
     *
     * @return 调试字符串
     */
    fun getDebugInfo(): String {
        val currentImage = getCurrentImageHistory()
        return if (currentImage != null) {
            "ImageId: ${currentImage.imageId}, " +
            "Position: ${getCurrentPosition()}/${getHistorySize()}, " +
            "CanUndo: ${canUndo()}, " +
            "CanForward: ${canForward()}, " +
            "ImageCount: ${getImageCount()}"
        } else {
            "未初始化任何图片"
        }
    }

    /**
     * 获取当前图片的历史记录（内部使用）
     */
    private fun getCurrentImageHistory(): ImageHistory<T>? {
        return imageStack.lastOrNull()
    }

    /**
     * 撤销操作的结果密封类
     *
     * 用于区分不同的撤销结果：
     * - Success: 成功撤销到前一帧
     * - ReachedInitialFrame: 已撤销到初始状态
     * - SwitchToPreviousImage: 从当前图切换到前一张图
     * - CantUndo: 无法继续撤销
     */
    sealed class UndoResult<T> {
        /**
         * 成功撤销，仍在当前图片
         */
        data class Success<T>(val frame: T) : UndoResult<T>()

        /**
         * 已撤销到当前图片的初始状态
         */
        data class ReachedInitialFrame<T>(val frame: T, val imageId: String) : UndoResult<T>()

        /**
         * 从当前图切换到前一张图的最后状态
         */
        data class SwitchToPreviousImage<T>(val frame: T, val imageId: String) : UndoResult<T>()

        /**
         * 无法继续撤销
         */
        data class CantUndo<T>(val reason: String) : UndoResult<T>()
    }
}
