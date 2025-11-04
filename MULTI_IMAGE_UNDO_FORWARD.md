# 多图片圈选撤销/前进功能设计文档

## 功能概述

扩展了 FrameHistoryManager 工具类，从支持单张图片圈选发展到支持多张图片场景。用户可以在多张图片之间切换，每张图片保持独立的圈选历史，支持完整的撤销和前进操作，包括图片之间的自动切换。

## 核心需求分析

```
场景 1: 图片间的撤销导航
- 第一张图片圈选 3 个圆 → 撤销 3 次 → 回到初始状态
- 此时可选择第二张图片
- 第二张图片原始状态作为新的 initialFrame
- 第二张图片圈选后，撤销链路为：圈选状态 → 初始状态 → 第一张图最后一帧 → ... → 第一张图初始

场景 2: 清空操作
- 第一张图片圈选时，清空第一张的所有圈选（包括历史）
- 第二张图片作为完全新的历史，独立管理

场景 3: 多图片栈管理
- 用户可在任何时刻撤销回到前一张图片
- 按照"后进先出"（LIFO）原则管理图片栈
```

## 架构设计

### 1. ImageHistory 数据类

```kotlin
data class ImageHistory<T>(
    val imageId: String,              // 图片唯一标识
    val history: MutableList<T>,      // 该图片的帧历史列表
    var currentPosition: Int = 0       // 当前位置指针
)
```

**作用**：
- 为每张图片独立维护历史记录
- 保存当前位置，便于撤销/前进操作

### 2. imageStack 图片栈

```kotlin
private val imageStack = mutableListOf<ImageHistory<T>>()
```

**特点**：
- 栈顶（last）是当前正在编辑的图片
- 栈的顺序反映图片的编辑顺序
- 支持快速的添加和移除操作

**示例**:
```
初始状态（加载第一张图）:
imageStack = [ImageHistory(img1_id, [empty], 0)]

圈选第一张后，选择第二张:
imageStack = [ImageHistory(img1_id, [c1, c1+c2, ...], pos),
              ImageHistory(img2_id, [empty], 0)]

撤销回到第一张:
- 第二张被移除或仍保留（取决于实现）
- currentImageId 切换回 img1_id
```

### 3. newImage() 方法详解

```kotlin
fun newImage(imageId: String, initialFrame: T) {
    // 检查该图片是否已存在
    val existingIndex = imageStack.indexOfFirst { it.imageId == imageId }

    if (existingIndex >= 0) {
        // 图片已存在：移到栈顶
        val imageHistory = imageStack.removeAt(existingIndex)
        imageStack.add(imageHistory)
    } else {
        // 新图片：创建新的历史记录
        imageStack.add(ImageHistory(imageId, mutableListOf(initialFrame), 0))
    }

    currentImageId = imageId
}
```

**三种调用场景**：

1. **初始化第一张图**
   ```
   newImage("img1", emptyList())
   → imageStack = [ImageHistory(img1, [empty], 0)]
   → currentImageId = "img1"
   ```

2. **选择新的第二张图（在第一张初始状态）**
   ```
   newImage("img2", emptyList())
   → imageStack = [ImageHistory(img1, [...], pos),
                   ImageHistory(img2, [empty], 0)]
   → currentImageId = "img2"
   ```

3. **返回到第一张图（已保存历史）**
   ```
   newImage("img1", previousInitialFrame)
   → 检查到 img1 已存在
   → 将 img1 从索引 0 移到栈顶（索引 1）
   → currentImageId = "img1"
   → imageStack = [ImageHistory(img2, [...], pos),
                   ImageHistory(img1, [...], pos)]
   ```

### 4. UndoResult 密封类

用于区分撤销的不同结果，帮助 Fragment 做出相应的 UI 更新和提示：

```kotlin
sealed class UndoResult<T> {
    // 成功撤销到前一帧，仍在当前图片
    data class Success<T>(val frame: T) : UndoResult<T>()

    // 已撤销到当前图片的初始状态
    data class ReachedInitialFrame<T>(val frame: T, val imageId: String) : UndoResult<T>()

    // 从当前图切换到前一张图的最后状态
    data class SwitchToPreviousImage<T>(val frame: T, val imageId: String) : UndoResult<T>()

    // 无法继续撤销
    data class CantUndo<T>(val reason: String) : UndoResult<T>()
}
```

**意义**：
- `Success`: 正常撤销，无需额外处理
- `ReachedInitialFrame`: 提示用户可选择其他图片
- `SwitchToPreviousImage`: 通知用户图片已切换
- `CantUndo`: 已在最初始状态，无法撤销

## 完整工作流程

### 场景：两张图片的完整圈选过程

```
┌────────────────────────────────────────────────────────────┐
│ 初始化 - 加载第一张图片                                    │
│ newImage("img1", emptyList())                              │
│ imageStack = [ImageHistory("img1", [empty], 0)]           │
│ 按钮状态: 撤销❌禁用 前进❌禁用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 第一张图片上圈选                                            │
│ push([circle1])                                            │
│ imageStack = [ImageHistory("img1", [empty, [c1]], 1)]     │
│ 按钮状态: 撤销✅启用 前进❌禁用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 继续圈选                                                   │
│ push([circle1, circle2])                                  │
│ imageStack = [ImageHistory("img1", [e, [c1], [c1,c2]], 2)]│
│ 按钮状态: 撤销✅启用 前进❌禁用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户撤销                                                   │
│ undo() → UndoResult.Success([c1])                         │
│ imageStack = [ImageHistory("img1", [e, [c1], [c1,c2]], 1)]│
│ 按钮状态: 撤销✅启用 前进✅启用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户继续撤销                                               │
│ undo() → UndoResult.ReachedInitialFrame(empty, "img1")    │
│ imageStack = [ImageHistory("img1", [e, [c1], [c1,c2]], 0)]│
│ 按钮状态: 撤销❌禁用 前进✅启用                              │
│ Toast: "已回到初始状态，可以选择其他图片"                   │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户选择第二张图片                                         │
│ newImage("img2", emptyList())                              │
│ imageStack = [ImageHistory("img1", [e, [c1], [c1,c2]], 0),│
│              ImageHistory("img2", [empty], 0)]            │
│ 按钮状态: 撤销❌禁用 前进❌禁用                              │
│ currentImageId = "img2"                                    │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 第二张图片上圈选                                            │
│ push([circle_a])                                           │
│ imageStack[1] = ImageHistory("img2", [empty, [c_a]], 1)   │
│ 按钮状态: 撤销✅启用 前进❌禁用                              │
│ 注意: 第一张图片的历史被保留，但不可用（直到再选它）      │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 第二张图片上继续圈选                                        │
│ push([circle_a, circle_b])                                │
│ imageStack[1] = ImageHistory("img2", [e, [c_a], [c_a,c_b]], 2) │
│ 按钮状态: 撤销✅启用 前进❌禁用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户撤销第二张图片                                         │
│ undo() → UndoResult.Success([c_a])                        │
│ imageStack[1] = ImageHistory("img2", [...], 1)            │
│ 按钮状态: 撤销✅启用 前进✅启用                              │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户继续撤销到第二张初始状态                               │
│ undo() → UndoResult.ReachedInitialFrame(empty, "img2")    │
│ imageStack[1].currentPosition = 0                          │
│ 按钮状态: 撤销✅启用 前进✅启用                              │
│ 注意: 撤销按钮启用，因为第一张图片有历史                   │
└────────────────────────────────────────────────────────────┘
                         ↓
┌────────────────────────────────────────────────────────────┐
│ 用户再撤销，切换到第一张图片的最后状态                     │
│ undo() → UndoResult.SwitchToPreviousImage([c1,c2], "img1")│
│ currentImageId = "img1"                                    │
│ imageStack 重新排序，img1 变为栈顶                         │
│ 按钮状态: 撤销✅启用 前进❌禁用                              │
│ Toast: "切换到前一张图片"                                   │
│ CircleDrawingImageView 显示 [c1,c2]                       │
└────────────────────────────────────────────────────────────┘
```

## Fragment3 集成

### 初始化

```kotlin
override fun setupUI() {
    // ...

    // 创建历史管理器（无参构造）
    frameHistoryManager = FrameHistoryManager<List<CircleSelection>>()

    // 获取第一张图片并初始化
    val currentImage = sharedViewModel.currentImage.value
    if (currentImage != null) {
        frameHistoryManager?.newImage(currentImage.id.toString(), emptyList())
    }

    // ...
}
```

### 撤销按钮处理

```kotlin
binding.btnRemoveCircle.setOnClickListener {
    frameHistoryManager?.let { manager ->
        val undoResult = manager.undo()
        when (undoResult) {
            is FrameHistoryManager.UndoResult.Success -> {
                // 更新显示，仍在当前图片
                binding.circleDrawingImageView.setCircles(undoResult.frame)
                updateHistoryButtonStates()
            }
            is FrameHistoryManager.UndoResult.ReachedInitialFrame -> {
                // 已回到初始状态
                binding.circleDrawingImageView.setCircles(undoResult.frame)
                updateHistoryButtonStates()
                Toast.makeText(
                    requireContext(),
                    "已回到初始状态，可以选择其他图片",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is FrameHistoryManager.UndoResult.SwitchToPreviousImage -> {
                // 切换到前一张图片
                binding.circleDrawingImageView.setCircles(undoResult.frame)
                updateHistoryButtonStates()
                Toast.makeText(
                    requireContext(),
                    "切换到前一张图片 (${undoResult.imageId})",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is FrameHistoryManager.UndoResult.CantUndo -> {
                Toast.makeText(
                    requireContext(),
                    undoResult.reason,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
```

### 图片切换（待实现）

当用户从 RecyclerView 选择另一张图片时：

```kotlin
// 在 RecyclerView 的适配器点击事件中
onImageSelected { selectedImage ->
    // 更新 CircleDrawingImageView
    com.bumptech.glide.Glide.with(this)
        .load(selectedImage.uri)
        .into(binding.circleDrawingImageView)

    // 切换历史管理器到新图片
    frameHistoryManager?.newImage(
        selectedImage.id.toString(),
        emptyList()  // 新图片的初始状态
    )

    // 更新按钮状态和显示
    binding.circleDrawingImageView.clearAllCircles()
    updateHistoryButtonStates()
}
```

## API 总结

### 公开方法

| 方法名 | 参数 | 返回值 | 说明 |
|--------|------|--------|------|
| `newImage()` | imageId, initialFrame | - | 初始化或切换到新图片 |
| `push()` | frame | - | 添加新帧到当前图片 |
| `undo()` | - | UndoResult<T> | 撤销，返回结果对象 |
| `forward()` | - | T? | 前进到后一帧 |
| `canUndo()` | - | Boolean | 检查是否可撤销 |
| `canForward()` | - | Boolean | 检查是否可前进 |
| `clearCurrentImage()` | - | - | 清空当前图片的圈选 |
| `clearAll()` | - | - | 清空所有历史 |
| `getCurrentImageId()` | - | String? | 获取当前图片 ID |
| `getImageCount()` | - | Int | 获取保存的图片数 |
| `getDebugInfo()` | - | String | 获取调试信息 |

## 与旧版本的区别

| 功能 | 旧版本 (单图片) | 新版本 (多图片) |
|------|-----------------|-----------------|
| 构造器 | `FrameHistoryManager(initialFrame)` | `FrameHistoryManager()` |
| 初始化 | 构造时初始化 | `newImage()` 初始化 |
| undo() 返回 | `T?` | `UndoResult<T>` |
| 图片切换 | N/A | 自动处理 |
| 清空操作 | `reset()` | `clearCurrentImage()` |
| 图片数量 | 1 | 多个 |

## 性能考虑

**内存占用**：
- 每张图片：O(n) 其中 n 为圆圈数量
- m 张图片：O(m * n)
- 例：8 张图片 × 8 个圆圈 ≈ 2KB（圆圈对象大小）

**CPU 占用**：
- `newImage()`: O(m) 其中 m 为图片数量（线性搜索）
- `push()/undo()/forward()`: O(1)
- `setCircles()`: O(n) 其中 n 为圆圈数量

**优化建议**：
- 对于大量图片，可使用 HashMap 替代线性搜索
- 每张图片限制历史帧数（最近 50 帧）
- 根据需要异步加载图片和历史

## 测试用例

### 测试 1: 单张图片撤销
- [ ] 第一张图圈选 2 个圆
- [ ] 撤销 1 次，验证返回 `Success`
- [ ] 撤销到初始状态，验证返回 `ReachedInitialFrame`
- [ ] 再撤销，验证返回 `CantUndo`

### 测试 2: 两张图片的切换和撤销
- [ ] 第一张图圈选 2 个圆，撤销回初始
- [ ] 选择第二张图，验证为初始状态
- [ ] 第二张图圈选 1 个圆，撤销
- [ ] 验证返回 `SwitchToPreviousImage` 并显示第一张图的最后状态

### 测试 3: 清空当前图片
- [ ] 第一张图圈选 3 个圆
- [ ] 点击"清空"
- [ ] 验证所有圆圈消失，按钮禁用
- [ ] 验证历史为初始状态（仅 1 帧）

### 测试 4: 多图片栈管理
- [ ] 圈选图 1 (2 个圆) → 图 2 (3 个圆) → 图 3 (1 个圆)
- [ ] 图 3 撤销到初始 → 切换到图 2 最后 → 再撤销到图 1 最后
- [ ] 验证图片顺序和状态正确

### 测试 5: 前进功能
- [ ] 圈选图片，撤销几次，前进，验证恢复状态
- [ ] 撤销后添加新圆，验证前进历史被清除

## 后续增强

1. **UI 改进**
   - 显示当前图片 ID 和位置："图片 1 的第 2/3 帧"
   - 图片切换时的动画过渡

2. **性能优化**
   - 使用 HashMap 加速图片查找
   - 限制历史栈大小

3. **持久化**
   - 保存历史到数据库
   - 恢复之前的编辑会话

4. **用户体验**
   - 键盘快捷键 (Ctrl+Z)
   - 手势支持 (滑动)
   - 视觉反馈 (动画、震动)

5. **高级特性**
   - 比较不同图片版本
   - 合并多张图片的圈选
   - 批量操作支持
