# Fragment3 圈选撤销/前进功能设计文档

## 功能概述

为 Fragment3 的圈选人物功能添加了完整的撤销（Undo）和前进（Forward）功能，允许用户在圈选过程中灵活地返回前一个状态或跳转到后一个状态。

## 核心组件

### 1. FrameHistoryManager 工具类
**位置**: `app/src/main/java/com/example/alive/util/FrameHistoryManager.kt`

一个泛型历史管理器，支持任意类型的帧数据导航。

**主要方法**:
- `push(frame: T)`: 添加新帧到历史，清除当前位置之后的所有帧（如果有的话）
- `undo(): T?`: 返回前一帧，如果已在第一帧则返回 null
- `forward(): T?`: 返回后一帧，如果已在最后一帧则返回 null
- `canUndo(): Boolean`: 检查是否可以撤销（用于禁用按钮）
- `canForward(): Boolean`: 检查是否可以前进（用于禁用按钮）
- `getCurrentFrame(): T`: 获取当前帧
- `getCurrentPosition(): Int`: 获取当前位置（1-based）
- `getHistorySize(): Int`: 获取历史总帧数

**工作原理**:
```
初始状态: history = [empty], position = 0
用户圈选第1个圆: history = [empty, circle1], position = 1
用户圈选第2个圆: history = [empty, circle1, circle1+circle2], position = 2
用户圈选第3个圆: history = [empty, circle1, circle1+circle2, circle1+circle2+circle3], position = 3

用户点击撤销:   history = [empty, circle1, circle1+circle2, circle1+circle2+circle3], position = 2
                显示: circle1+circle2
用户点击前进:   history = [empty, circle1, circle1+circle2, circle1+circle2+circle3], position = 3
                显示: circle1+circle2+circle3

用户圈选新圆（在撤销后）: history = [empty, circle1, circle1+circle2, NEW_STATE], position = 3
                        注意：circle1+circle2+circle3 被删除，因为已执行撤销
```

### 2. CircleDrawingImageView 增强
**位置**: `app/src/main/java/com/example/alive/ui/view/CircleDrawingImageView.kt`

在现有圆圈绘制功能基础上添加了：

**新增方法**:
- `setCircles(circles: List<CircleSelection>)`: 从历史中恢复圆圈列表，用于撤销/前进时更新显示
- `getCircleCount(): Int`: 获取当前圆圈数量
- `setOnCircleCompletedListener(callback: () -> Unit)`: 设置圆圈完成时的回调，当用户完成绘制新圆圈时触发

**修改**:
- 在 `onTouchEvent` 的 `ACTION_UP` 中，成功添加圆圈后触发 `onCircleCompleted` 回调

### 3. Fragment3 集成
**位置**: `app/src/main/java/com/example/alive/ui/fragment/Fragment3.kt`

#### 核心变量
```kotlin
private var frameHistoryManager: FrameHistoryManager<List<CircleSelection>>? = null
```

#### 初始化流程
1. 创建 `FrameHistoryManager` 实例，初始帧为空列表
2. 设置 `CircleDrawingImageView` 的圆圈完成回调，每次新圆圈完成时调用 `addFrameToHistory()`
3. 绑定"撤销"按钮事件：调用 `manager.undo()` 并更新 UI
4. 绑定"前进"按钮事件：调用 `manager.forward()` 并更新 UI
5. 绑定"清空"按钮事件：清空圆圈并重置历史管理器

#### 关键方法

**updateHistoryButtonStates()**
```kotlin
private fun updateHistoryButtonStates() {
    frameHistoryManager?.let { manager ->
        binding.btnRemoveCircle.isEnabled = manager.canUndo()
        binding.btnForward.isEnabled = manager.canForward()
    }
}
```
根据当前历史位置更新按钮启用状态：
- 第一帧时：撤销按钮禁用（`canUndo() = false`）
- 最后一帧时：前进按钮禁用（`canForward() = false`）

**addFrameToHistory()**
```kotlin
private fun addFrameToHistory() {
    val currentCircles = binding.circleDrawingImageView.getCircles()
    frameHistoryManager?.push(currentCircles)
    updateHistoryButtonStates()
}
```
当用户完成绘制新圆圈时调用，将当前状态保存到历史。

### 4. 布局文件更新
**位置**: `app/src/main/res/layout/fragment3.xml`

在按钮行中添加了"前进"按钮：
```xml
<Button
    android:id="@+id/btn_forward"
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="Forward"
    android:layout_marginEnd="4dp"
    android:textSize="12sp"
    android:enabled="false" />
```

初始状态禁用（`enabled="false"`），只有在可以前进时才启用。

## 用户交互流程

### 场景 1：基本撤销和前进
```
1. 用户在图片上绘制圆1
   → CircleDrawingImageView 触发 onCircleCompleted 回调
   → Fragment3 调用 addFrameToHistory()
   → FrameHistoryManager 保存 [circle1]
   → 撤销按钮启用，前进按钮禁用（已在最后）

2. 用户继续绘制圆2
   → FrameHistoryManager 保存 [circle1, circle2]
   → 撤销和前进按钮都禁用

3. 用户点击"撤销"
   → FrameHistoryManager.undo() 返回 [circle1]
   → CircleDrawingImageView.setCircles([circle1]) 更新显示
   → updateHistoryButtonStates() 更新按钮（撤销禁用，前进启用）

4. 用户点击"前进"
   → FrameHistoryManager.forward() 返回 [circle1, circle2]
   → CircleDrawingImageView.setCircles([circle1, circle2]) 更新显示
   → updateHistoryButtonStates() 更新按钮状态
```

### 场景 2：撤销后新增圆圈（清除前进历史）
```
1. 用户绘制 circle1, circle2, circle3
   历史: [[], [circle1], [circle1,circle2], [circle1,circle2,circle3]]
   位置: 3

2. 用户点击撤销两次
   位置变为: 1
   显示: [circle1]

3. 用户绘制新的 circle2'（不同于之前的 circle2）
   → FrameHistoryManager.push([circle1, circle2'])
   → 之前的 [circle1,circle2], [circle1,circle2,circle3] 被删除
   历史: [[], [circle1], [circle1,circle2']]
   位置: 2
   前进按钮禁用（因为已在最后）
```

### 场景 3：清空所有
```
1. 用户点击"清空"按钮
   → binding.circleDrawingImageView.clearAllCircles() 清空所有圆圈
   → frameHistoryManager?.reset(emptyList()) 重置历史
   → updateHistoryButtonStates() 禁用撤销和前进按钮
   状态恢复到初始状态
```

## 按钮启用/禁用规则

| 状态 | 撤销按钮 | 前进按钮 |
|------|--------|--------|
| 初始状态（无圆圈） | ❌ 禁用 | ❌ 禁用 |
| 绘制第1个圆圈后 | ✅ 启用 | ❌ 禁用 |
| 绘制第2个圆圈后 | ✅ 启用 | ❌ 禁用 |
| 撤销1次（回到第1圆圈） | ❌ 禁用 | ✅ 启用 |
| 前进1次（回到第2圆圈） | ✅ 启用 | ❌ 禁用 |
| 撤销后新增圆圈 | ✅ 启用 | ❌ 禁用 |

## 设计优势

1. **泛型设计**: `FrameHistoryManager<T>` 可用于任何类型的帧数据，易于复用
2. **内存高效**: 仅保存每个状态的增量（圆圈列表），不保存整个界面快照
3. **清晰的状态管理**: 单一历史管理器，避免多个 LiveData 的复杂性
4. **UI 同步**: 通过回调机制自动同步 View 和 ViewModel 状态
5. **用户友好**: 按钮状态与可操作性一致，防止无效操作

## 技术要点

1. **回调机制**: CircleDrawingImageView 通过 `setOnCircleCompletedListener` 通知 Fragment 新圆圈完成，实现 View 与 ViewModel 的解耦
2. **状态快照**: 每次 push() 时保存当前圆圈列表的完整快照，支持快速恢复
3. **位置指针**: 使用 currentPosition 指针跟踪历史中的当前位置，避免创建新对象
4. **清除前进历史**: push() 方法在添加新帧前删除当前位置之后的所有帧，实现直观的撤销-前进语义

## 测试用例

### 测试 1：基本撤销
- [ ] 绘制 3 个圆圈
- [ ] 点击撤销，验证显示返回到 2 个圆圈的状态
- [ ] 点击撤销，验证显示返回到 1 个圆圈的状态
- [ ] 验证第三次撤销无反应（已在第一帧）

### 测试 2：前进功能
- [ ] 从上一个测试的状态出发（显示 1 个圆圈）
- [ ] 点击前进，验证显示前进到 2 个圆圈
- [ ] 点击前进，验证显示前进到 3 个圆圈
- [ ] 验证第三次前进无反应（已在最后一帧）

### 测试 3：撤销后新增
- [ ] 绘制 3 个圆圈
- [ ] 撤销 2 次（显示 1 个圆圈）
- [ ] 绘制新的第 2 个圆圈（与之前的形状不同）
- [ ] 验证前进按钮禁用（旧的第 2、3 圆圈已被删除）
- [ ] 验证显示的是新的第 2 个圆圈（不是旧的）

### 测试 4：清空功能
- [ ] 绘制 3 个圆圈，执行撤销-前进的混合操作
- [ ] 点击"清空"
- [ ] 验证所有圆圈消失
- [ ] 验证撤销和前进按钮都禁用

### 测试 5：按钮状态同步
- [ ] 验证初始化时两个按钮都禁用
- [ ] 绘制第 1 个圆圈，验证撤销启用、前进禁用
- [ ] 绘制第 2 个圆圈，验证撤销启用、前进禁用
- [ ] 撤销，验证撤销禁用、前进启用
- [ ] 撤销到第一帧，验证撤销禁用、前进启用

## 相关文件汇总

| 文件 | 修改内容 |
|------|--------|
| `FrameHistoryManager.kt` | 新增 - 泛型历史管理器 |
| `CircleDrawingImageView.kt` | 添加回调机制和 setCircles() 方法 |
| `Fragment3.kt` | 集成历史管理器，绑定按钮事件 |
| `fragment3.xml` | 添加"前进"按钮，初始禁用状态 |

## 后续优化方向

1. **UI 增强**: 显示"第 X/Y 帧"的位置指示器
2. **键盘快捷键**: 支持 Ctrl+Z（撤销）和 Ctrl+Y（前进）
3. **动画反馈**: 撤销/前进时添加淡入淡出动画
4. **持久化**: 保存历史到 SharedPreferences 或数据库
5. **性能优化**: 对于大量历史帧，使用差异存储（delta storage）而非快照
6. **手势支持**: 支持左右滑动进行撤销/前进
