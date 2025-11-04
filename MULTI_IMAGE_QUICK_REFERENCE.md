# 多图片撤销/前进 - 快速参考指南

## 🎯 核心变更

**旧版本（单图片）**:
```kotlin
val manager = FrameHistoryManager(emptyList())
manager.push(circles)
val prevCircles = manager.undo()  // 返回 T 或 null
```

**新版本（多图片）**:
```kotlin
val manager = FrameHistoryManager<List<CircleSelection>>()
manager.newImage("img1", emptyList())  // 初始化第一张图
manager.push(circles)
val result = manager.undo()  // 返回 UndoResult<T>
when (result) {
    is Success -> { /* 更新显示 */ }
    is ReachedInitialFrame -> { /* 提示可选择其他图 */ }
    is SwitchToPreviousImage -> { /* 图片已切换 */ }
    is CantUndo -> { /* 无法撤销 */ }
}
```

## 📊 使用流程

### 1. 初始化

```kotlin
// Fragment3.setupUI()
frameHistoryManager = FrameHistoryManager<List<CircleSelection>>()

val currentImage = sharedViewModel.currentImage.value
if (currentImage != null) {
    frameHistoryManager?.newImage(currentImage.id.toString(), emptyList())
}
```

### 2. 圈选时（回调自动调用）

```kotlin
// CircleDrawingImageView 完成圆圈时触发
private fun addFrameToHistory() {
    val currentCircles = binding.circleDrawingImageView.getCircles()
    frameHistoryManager?.push(currentCircles)
    updateHistoryButtonStates()
}
```

### 3. 撤销按钮

```kotlin
binding.btnRemoveCircle.setOnClickListener {
    val result = frameHistoryManager?.undo()
    when (result) {
        is UndoResult.Success -> {
            binding.circleDrawingImageView.setCircles(result.frame)
        }
        is UndoResult.ReachedInitialFrame -> {
            binding.circleDrawingImageView.setCircles(result.frame)
            // 显示可以选择其他图片的提示
        }
        is UndoResult.SwitchToPreviousImage -> {
            binding.circleDrawingImageView.setCircles(result.frame)
            // 图片已自动切换到 result.imageId
        }
        is UndoResult.CantUndo -> {
            // 显示不能撤销的原因
        }
    }
    updateHistoryButtonStates()
}
```

### 4. 选择新图片

```kotlin
// 当用户从 RecyclerView 选择另一张图片时
fun onImageSelected(imageId: String, imageUri: Uri) {
    // 加载新图片
    Glide.with(this).load(imageUri).into(binding.circleDrawingImageView)

    // 初始化新图片的历史
    frameHistoryManager?.newImage(imageId, emptyList())

    // 清空显示，准备新的圈选
    binding.circleDrawingImageView.clearAllCircles()
    updateHistoryButtonStates()
}
```

## 🔄 完整交互流程

```
┌─ 加载图片 1 ─────────────────────┐
│ newImage("img1", [])             │
│ 撤销: ❌ | 前进: ❌              │
└──────────────────────────────────┘
           ↓ (圈选 2 个圆)
┌─ 图片 1 圈选中 ──────────────────┐
│ push([c1, c2])                   │
│ 撤销: ✅ | 前进: ❌              │
└──────────────────────────────────┘
           ↓ (撤销)
┌─ 图片 1 撤销中 ──────────────────┐
│ undo() → Success([c1])           │
│ 撤销: ✅ | 前进: ✅              │
└──────────────────────────────────┘
           ↓ (撤销到初始)
┌─ 图片 1 初始状态 ────────────────┐
│ undo() → ReachedInitialFrame([]) │
│ 撤销: ❌ | 前进: ✅              │
│ 💡 可以选择其他图片              │
└──────────────────────────────────┘
           ↓ (选择图片 2)
┌─ 加载图片 2 ─────────────────────┐
│ newImage("img2", [])             │
│ 撤销: ❌ | 前进: ❌              │
│ 图片 1 的历史被保留               │
└──────────────────────────────────┘
           ↓ (圈选 1 个圆)
┌─ 图片 2 圈选中 ──────────────────┐
│ push([c_a])                      │
│ 撤销: ✅ | 前进: ❌              │
└──────────────────────────────────┘
           ↓ (撤销)
┌─ 图片 2 初始状态 ────────────────┐
│ undo() → ReachedInitialFrame([]) │
│ 撤销: ✅ | 前进: ❌              │
│ 💡 可以撤销到图片 1 的最后状态    │
└──────────────────────────────────┘
           ↓ (再撤销)
┌─ 切换到图片 1 ───────────────────┐
│ undo() → SwitchToPreviousImage   │
│ currentImageId = "img1"          │
│ 显示: [c1, c2]                   │
│ 撤销: ✅ | 前进: ❌              │
└──────────────────────────────────┘
```

## 🔑 关键概念

### UndoResult 四种情况

| 结果类型 | 场景 | 需要的操作 |
|---------|------|---------|
| `Success` | 撤销到前一帧，仍在当前图片 | 更新 CircleDrawingImageView |
| `ReachedInitialFrame` | 撤销到当前图片初始状态 | 提示可选择其他图片 |
| `SwitchToPreviousImage` | 从当前图切换到前一张图 | 提示图片已切换 |
| `CantUndo` | 已在最初始状态 | 显示错误信息 |

### 图片栈的 LIFO 特性

```
操作序列：加载图1 → 加载图2 → 加载图3

栈的样子：[img1, img2, img3]  (img3 在栈顶)

撤销时：
- 在图3撤销 → 切换到图2
- 在图2撤销 → 切换到图1
- 在图1撤销 → CantUndo
```

### 清空操作

- `clearCurrentImage()`: 清空当前图片的圈选，回到初始状态，**保留其他图片的历史**
- `clearAll()`: 清空所有图片的所有历史，完全重置

## ⚠️ 常见陷阱

### 1. 忘记 newImage() 初始化

❌ **错误**：
```kotlin
val manager = FrameHistoryManager<List<CircleSelection>>()
manager.push(circles)  // 抛出 IllegalStateException
```

✅ **正确**：
```kotlin
val manager = FrameHistoryManager<List<CircleSelection>>()
manager.newImage("img1", emptyList())
manager.push(circles)  // OK
```

### 2. 不处理 UndoResult

❌ **错误**：
```kotlin
manager.undo()  // 返回值被忽略
```

✅ **正确**：
```kotlin
val result = manager.undo()
when (result) {
    is UndoResult.Success -> { /* 处理 */ }
    is UndoResult.ReachedInitialFrame -> { /* 处理 */ }
    // ...
}
```

### 3. 图片切换时不调用 newImage()

❌ **错误**：
```kotlin
// 用户选择了新图片，但没有初始化
binding.circleDrawingImageView.clearAllCircles()
```

✅ **正确**：
```kotlin
// 用户选择了新图片
binding.circleDrawingImageView.clearAllCircles()
frameHistoryManager?.newImage(imageId, emptyList())  // 初始化
updateHistoryButtonStates()
```

## 📝 代码模板

### 完整的撤销处理

```kotlin
private fun handleUndo() {
    frameHistoryManager?.let { manager ->
        when (val result = manager.undo()) {
            is FrameHistoryManager.UndoResult.Success -> {
                binding.circleDrawingImageView.setCircles(result.frame)
                updateHistoryButtonStates()
                // 可选：记录日志
                Log.d("Fragment3", "撤销成功: 位置 ${manager.getCurrentPosition()}")
            }
            is FrameHistoryManager.UndoResult.ReachedInitialFrame -> {
                binding.circleDrawingImageView.setCircles(result.frame)
                updateHistoryButtonStates()
                Toast.makeText(
                    requireContext(),
                    "已回到初始状态，可以选择其他图片",
                    Toast.LENGTH_SHORT
                ).show()
            }
            is FrameHistoryManager.UndoResult.SwitchToPreviousImage -> {
                binding.circleDrawingImageView.setCircles(result.frame)
                updateHistoryButtonStates()
                Toast.makeText(
                    requireContext(),
                    "切换到图片 ${result.imageId}",
                    Toast.LENGTH_SHORT
                ).show()
                // 可选：更新 UI 来显示当前图片ID
            }
            is FrameHistoryManager.UndoResult.CantUndo -> {
                Toast.makeText(
                    requireContext(),
                    "无法撤销: ${result.reason}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
```

### 完整的图片选择处理

```kotlin
private fun selectImage(imageId: String, imageUri: Uri) {
    // 1. 加载图片
    Glide.with(this)
        .load(imageUri)
        .into(binding.circleDrawingImageView)

    // 2. 初始化历史管理器
    frameHistoryManager?.newImage(imageId, emptyList())

    // 3. 清空 UI
    binding.circleDrawingImageView.clearAllCircles()

    // 4. 更新按钮状态
    updateHistoryButtonStates()

    // 5. 可选：显示反馈
    Log.d("Fragment3", "已选择图片: $imageId")
}
```

## 🧪 调试技巧

```kotlin
// 查看当前状态
Log.d("FrameHistoryManager", frameHistoryManager?.getDebugInfo() ?: "未初始化")

// 输出示例：
// ImageId: img1, Position: 2/3, CanUndo: true, CanForward: false, ImageCount: 2

// 手动检查按钮状态
val canUndo = frameHistoryManager?.canUndo() ?: false
val canForward = frameHistoryManager?.canForward() ?: false
Log.d("Fragment3", "撤销: ${if (canUndo) "✅" else "❌"}, 前进: ${if (canForward) "✅" else "❌"}")
```

## 📚 相关文件

- `FrameHistoryManager.kt`: 核心工具类 (370 行)
- `Fragment3.kt`: UI 集成 (修改)
- `CircleDrawingImageView.kt`: 回调机制 (修改)
- `MULTI_IMAGE_UNDO_FORWARD.md`: 详细设计文档

---

**最后修改**: 2025-11-04
**版本**: 2.0 (多图片支持)
**状态**: ✅ 实现完成
