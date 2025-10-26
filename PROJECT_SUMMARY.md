# ALIVE Person Removal Android App - 项目完成

## 项目概述

本项目是一个严格遵循**MVVM架构**的Android应用，用于处理ALIVE图像中的人物消除功能。应用集成了Kotlin协程、Room数据库、Retrofit网络请求、Glide图片加载等现代Android技术栈。

---

## 项目结构

### 1. **数据层 (Data Layer)**

#### Entity类
- `AliveImage.kt` - ALIVE图像数据实体，存储图像文件信息
- `Task.kt` - 任务实体，记录处理任务的状态和结果
- `SelectionData.kt` - 圆圈圈选数据和帧数据

#### DTO类
- `ApiDtos.kt` - 所有API请求/响应的数据传输对象

#### DAO接口
- `AliveImageDao.kt` - ALIVE图像的数据访问对象
- `TaskDao.kt` - 任务的数据访问对象

#### 数据库
- `AliveDatabase.kt` - Room数据库定义
- `DatabaseConverters.kt` - 数据库类型转换器

### 2. **网络层 (Network Layer)**

- `AliveApi.kt` - Retrofit API接口定义
- `MockAliveApi.kt` - Mock实现，用于测试
- `ApiClient.kt` - API客户端工厂

### 3. **Repository层**

- `AliveRepository.kt` - 数据源聚合，负责访问数据库和网络API

### 4. **ViewModel层**

- `SharedViewModel.kt` - 在所有Fragment之间共享数据
- `Fragment1ViewModel.kt` - Fragment1的ViewModel（图片选择）
- `Fragment2ViewModel.kt` - Fragment2的ViewModel（提取8帧）
- `Fragment3ViewModel.kt` - Fragment3的ViewModel（手动圈选）
- `Fragment4ViewModel.kt` - Fragment4的ViewModel（等待任务）
- `Fragment5ViewModel.kt` - Fragment5的ViewModel（结果展示）
- `TaskListViewModel.kt` - 任务列表的ViewModel
- `AliveViewModelFactory.kt` - ViewModel工厂

### 5. **UI层 (Presentation Layer)**

#### Activity
- `MainActivity.kt` - 主活动，容纳所有Fragment

#### Fragment
- `HomeFragment.kt` - 主页/入口
- `Fragment1.kt` - 图片/视频选择
- `Fragment2.kt` - 自动提取8帧
- `Fragment3.kt` - 手动圈选要消除的人物
- `Fragment4.kt` - 等待任务完成
- `Fragment5.kt` - 显示结果并提供分享、收藏、下载功能
- `TaskListFragment.kt` - 任务列表

#### 自定义视图
- `CircleDrawingImageView.kt` - 支持手绘圆圈的ImageView

#### 适配器
- `FrameAdapter.kt` - 8帧列表的RecyclerView适配器
- `TaskListAdapter.kt` - 任务列表的RecyclerView适配器

### 6. **工具类 (Utilities)**

- `MediaUtils.kt` - 媒体操作工具（视频时长、MIME类型等）
- `VideoUtils.kt` - 视频处理工具（提取帧、创建ALIVE图等）

### 7. **布局文件 (Resources)**

```
res/layout/
├── activity_main.xml          # 主界面布局
├── fragment_home.xml          # 主页
├── fragment1.xml              # 图片选择
├── fragment2.xml              # 加载动画
├── fragment3.xml              # 圈选界面
├── fragment4.xml              # 处理等待
├── fragment5.xml              # 结果展示
├── fragment_task_list.xml     # 任务列表
├── item_frame.xml             # 帧列表项
└── item_task.xml              # 任务列表项
```

### 8. **导航图**

- `nav_graph.xml` - Navigation组件的导航图，定义所有Fragment之间的跳转

---

## 工作流程详解

### 1️⃣ **Fragment1 - 图片/视频选择**
- 用户点击图片区域打开系统相册
- 验证选择：
  - 对于视频：检查时长是否小于3秒
  - 对于图片：验证是否是ALIVE格式
- 如果验证失败，显示错误提示
- 如果验证成功，显示选中的图像，点击"Next"保存并进入Fragment2

### 2️⃣ **Fragment2 - 提取8帧**
- 自动调用后端API提取8帧（首帧、末帧、中间6帧均匀分布）
- 显示加载动画
- 接收返回的8帧路径和标记后的alive图路径
- **自动跳转**到Fragment3

### 3️⃣ **Fragment3 - 手动圈选**
- 上方显示当前帧的marked alive图
- 用户可以手指点击并拖动来绘制**红色圆圈**
- 下方显示8帧的水平列表，用户可点击切换显示的帧
- 功能按钮：
  - **Undo**: 删除最后一个圆圈
  - **Clear All**: 清除当前帧的所有圆圈
  - **Submit**: 提交圈选数据
- 提交后进入Fragment4

### 4️⃣ **Fragment4 - 任务处理中**
- 显示加载动画
- 后台轮询任务状态（每2秒检查一次）
- 用户可以点击"Cancel"返回主页
- 任务完成后自动跳转到Fragment5

### 5️⃣ **Fragment5 - 结果展示**
- 显示处理后的alive图
- 功能按钮：
  - **Share**: 调用系统分享功能分享结果图
  - **Favorite**: 收藏/取消收藏（调用API并保存到数据库）
  - **Download**: 下载到相册（保存到Pictures/Alive目录）
- 导航按钮：
  - **Home**: 返回主页
  - **Task List**: 查看任务列表

### 📋 **任务列表页面**
- 显示所有提交的任务
- 每个任务卡片显示：
  - 缩略图（仅完成的任务）
  - 任务状态（Pending/Processing/Completed/Failed）
  - 创建时间
  - 收藏星标（如果已收藏）
- 点击完成的任务可查看结果

---

## 技术栈

| 技术 | 用途 | 版本 |
|------|------|------|
| **Kotlin** | 编程语言 | 1.9+ |
| **Coroutines** | 异步编程 | 1.7.3 |
| **Room** | 本地数据库 | 2.6.1 |
| **Retrofit** | 网络请求 | 2.9.0 |
| **Glide** | 图片加载 | 4.16.0 |
| **Navigation** | Fragment导航 | 2.7.5 |
| **ViewModel** | UI数据管理 | 2.6.2 |
| **LiveData** | 数据观察 | 2.6.2 |

---

## MVVM架构要点

### ✅ **严格的层级划分**

```
┌─────────────────────────────────────┐
│         UI Layer (Fragment)          │
│  ┌──────────────────────────────┐   │
│  │  1. Fragment布局展示         │   │
│  │  2. 观察ViewModel中的数据    │   │
│  │  3. 用户交互触发ViewModel    │   │
│  └──────────────────────────────┘   │
└─────────────────────────────────────┘
            ↓↑ 数据绑定
┌─────────────────────────────────────┐
│    ViewModel Layer (业务逻辑)        │
│  ┌──────────────────────────────┐   │
│  │  1. 封装UI状态(LiveData)      │   │
│  │  2. 处理用户操作             │   │
│  │  3. 调用Repository           │   │
│  │  4. 通过协程管理异步任务     │   │
│  └──────────────────────────────┘   │
└─────────────────────────────────────┘
            ↓↑ 调用
┌─────────────────────────────────────┐
│    Repository Layer (数据聚合)      │
│  ┌──────────────────────────────┐   │
│  │  1. 聚合多个数据源           │   │
│  │  2. Room数据库访问           │   │
│  │  3. API网络请求             │   │
│  └──────────────────────────────┘   │
└─────────────────────────────────────┘
            ↓↑ 访问
┌─────────────────────────────────────┐
│     Model Layer (数据层)             │
│  ┌──────────────────────────────┐   │
│  │  1. Entity - 数据库实体      │   │
│  │  2. DTO - 网络传输对象       │   │
│  │  3. Room Database            │   │
│  │  4. Retrofit API             │   │
│  └──────────────────────────────┘   │
└─────────────────────────────────────┘
```

### 🔄 **数据流向**

```
用户操作 → Fragment → ViewModel → Repository → Model
↑                                              ↓
└── LiveData/StateFlow 观察数据变化 ←─────────┘
```

### 📱 **SharedViewModel的用途**

在Fragment1-5之间共享数据，避免重复创建Entity：
- `currentAliveImage` - 当前选中的图像
- `currentTask` - 当前任务
- `framesData` - 8帧数据
- `circleSelections` - 圆圈圈选数据
- `resultAliveImagePath` - 处理后的结果路径

---

## 主要功能实现

### 🖼️ **图片/视频验证**
```kotlin
// 判断ALIVE格式
MediaUtils.isAliveImage(filePath: String): Boolean

// 获取视频时长
MediaUtils.getVideoDuration(context: Context, uri: Uri): Long
```

### 🎬 **视频帧提取**
```kotlin
// 从视频提取8帧
VideoUtils.extract8FramesFromVideo(
    videoPath: String,
    outputDir: String,
    onFrameExtracted: (index, path) -> Unit
): Boolean
```

### 🔴 **圆圈绘制**
- 自定义`CircleDrawingImageView`
- 支持多个圆圈的绘制和删除
- 实时显示正在绘制的圆圈

### 💾 **数据持久化**
- Room数据库保存`AliveImage`和`Task`
- 支持查询、更新、删除操作
- TypeConverter处理枚举类型

### 🌐 **网络请求**
- Mock实现用于开发测试
- 支持轻松切换到真实API
- 使用Retrofit + Gson + OkHttp

### ⏳ **任务轮询**
Fragment4中实现后台轮询，每2秒检查一次任务状态，直到完成或超时

### 📤 **分享/收藏/下载**
- **分享**: 调用系统Intent.ACTION_SEND
- **收藏**: 调用API并更新本地数据库
- **下载**: 复制到Pictures/Alive目录，通知媒体库扫描

---

## API接口（Mock实现）

### 1. Extract 8 Frames
```
POST /api/extract8frames
Request: { alive_image_path: String }
Response: {
    status: "success",
    frame_paths: [8个路径],
    marked_alive_path: String
}
```

### 2. Circle Removal
```
POST /api/circleremoval
Request: {
    task_id: Long,
    alive_image_path: String,
    selections_json: String (JSON格式)
}
Response: {
    status: "success",
    task_id: Long
}
```

### 3. Task Status
```
GET /api/taskstatus?task_id=1
Response: {
    task_id: Long,
    status: "pending|processing|completed|failed",
    result_image_path: String (nullable),
    error_message: String (nullable)
}
```

### 4. Favorite
```
POST /api/favorite
Request: {
    task_id: Long,
    is_favorite: Boolean
}
Response: {
    status: "success",
    is_favorite: 1|0  // 1=收藏, 0=未收藏
}
```

---

## 权限配置

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
```

---

## 建议改进方向

1. **图像处理优化**
   - 使用OpenCV或FFmpeg进行更高效的视频处理
   - 实现图像缓存机制

2. **网络优化**
   - 添加请求超时、重试机制
   - 实现断点续传

3. **UI/UX改进**
   - 添加圆圈预览和参数调整
   - 实现拖拽调整已绘制的圆圈
   - 添加撤销/重做功能

4. **性能优化**
   - 使用ViewBinding优化布局绑定
   - 实现图片分辨率自适应
   - 添加内存泄漏检测（LeakCanary）

5. **错误处理**
   - 更详细的错误分类和提示
   - 添加离线模式支持
   - 网络异常重试机制

6. **测试**
   - 添加单元测试（Repository、ViewModel）
   - 添加UI测试（Espresso）
   - 添加集成测试

---

## 运行说明

1. **同步Gradle依赖**
   ```bash
   ./gradlew build
   ```

2. **运行应用**
   - 使用Android Studio Run按钮
   - 或命令行：`./gradlew installDebug`

3. **测试Mock API**
   - 应用启动后会使用MockAliveApi
   - 所有API调用都会返回模拟数据

4. **切换到真实API**
   - 修改`ApiClient.kt`的`getApi()`方法
   - 返回`retrofitApi`而不是`mockApi`
   - 更新`BASE_URL`为实际后端地址

---

## 总结

本项目完整展示了Android MVVM架构的实践应用，涵盖：
- ✅ 清晰的分层架构
- ✅ 协程和Flow的异步编程
- ✅ Room数据库的本地存储
- ✅ Retrofit网络通信
- ✅ Navigation Fragment管理
- ✅ ViewModel和LiveData的数据管理
- ✅ 自定义View的绘图功能
- ✅ 完整的业务流程实现

项目代码遵循Android官方最佳实践，易于扩展和维护。
