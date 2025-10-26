# ALIVE Person Removal - 快速实现功能清单

> 这份清单包含了完整的应用结构和每个模块的详细实现步骤，可用于快速重复生成相同的应用。

---

## 📋 目录

1. [项目初始化](#项目初始化)
2. [依赖配置](#依赖配置)
3. [数据层实现](#数据层实现)
4. [网络层实现](#网络层实现)
5. [ViewModel层实现](#viewmodel层实现)
6. [UI层实现](#ui层实现)
7. [工具类实现](#工具类实现)
8. [资源文件](#资源文件)
9. [配置文件](#配置文件)

---

## 🚀 项目初始化

### 步骤1.1: 创建Android项目基础结构

```bash
项目名称: Alive
包名: com.example.alive
最小API: 28
目标API: 35
Kotlin支持: 是
ViewBinding: 启用
```

### 步骤1.2: 创建目录结构

```
app/src/main/java/com/example/alive/
├── data/                    # 数据层
│   ├── dao/                # 数据访问对象
│   ├── db/                 # Room数据库
│   ├── dto/                # 数据传输对象
│   ├── entity/             # 数据实体
│   └── repository/         # 数据仓库
├── network/                # 网络层
│   └── api/               # API接口
├── ui/                     # UI层
│   ├── fragment/          # Fragment组件
│   ├── view/              # 自定义视图
│   ├── adapter/           # 适配器
│   └── viewmodel/         # ViewModel
└── util/                   # 工具类
```

---

## 📦 依赖配置

### 步骤2.1: build.gradle.kts 依赖列表

**必需的依赖（精确版本）：**

```gradle
// Fragment & Navigation
androidx.fragment:fragment-ktx:1.6.2
androidx.navigation:navigation-fragment-ktx:2.7.5
androidx.navigation:navigation-ui-ktx:2.7.5

// Lifecycle & ViewModel
androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2
androidx.lifecycle:lifecycle-livedata-ktx:2.6.2
androidx.lifecycle:lifecycle-runtime-ktx:2.6.2

// Room Database
androidx.room:room-runtime:2.6.1
androidx.room:room-ktx:2.6.1
androidx.room:room-compiler:2.6.1 (annotationProcessor)

// Retrofit & OkHttp
com.squareup.retrofit2:retrofit:2.9.0
com.squareup.retrofit2:converter-gson:2.9.0
com.squareup.okhttp3:okhttp:4.11.0
com.squareup.okhttp3:logging-interceptor:4.11.0

// Gson
com.google.code.gson:gson:2.10.1

// Glide
com.github.bumptech.glide:glide:4.16.0
com.github.bumptech.glide:compiler:4.16.0 (annotationProcessor)

// Coroutines
org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

// AppCompat & Material
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.10.0

// 解决注解冲突
org.jetbrains:annotations:23.0.0 (exclude com.intellij:annotations)
```

### 步骤2.2: AndroidManifest.xml 权限配置

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />

<activity android:name=".ui.MainActivity" android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

---

## 💾 数据层实现

### 步骤3.1: Entity 实体类

**创建4个Entity文件：**

| 文件名 | 位置 | 内容 |
|--------|------|------|
| `AliveImage.kt` | `data/entity/` | ALIVE图像表实体 |
| `Task.kt` | `data/entity/` | 任务表实体 + TaskStatus枚举 |
| `SelectionData.kt` | `data/entity/` | CircleSelection和FrameData数据类 |

**关键字段：**
- AliveImage: id, filePath, fileName, fileSize, mimeType, isVideo, duration, createdTime, thumbnailPath
- Task: id, aliveImageId, markingImagePath, circleSelectionsJson, resultImagePath, status, createdTime, completedTime, isFavorite, errorMessage
- TaskStatus枚举: PENDING, MARKING, PROCESSING, COMPLETED, FAILED

### 步骤3.2: DTO 数据传输对象

**创建1个DTO文件：**

| 文件名 | 位置 | 包含的类 |
|--------|------|---------|
| `ApiDtos.kt` | `data/dto/` | Extract8FramesRequest/Response、CircleRemovalRequest/Response、TaskStatusResponse、FavoriteRequest/Response |

### 步骤3.3: DAO 数据访问对象

**创建2个DAO文件：**

| 文件名 | 位置 | 操作 |
|--------|------|------|
| `AliveImageDao.kt` | `data/dao/` | insert, update, delete, getById, getAllFlow, getAll, deleteById |
| `TaskDao.kt` | `data/dao/` | insert, update, delete, getById, getAllFlow, getAll, getByStatusFlow, getByStatus, getFavoritesFlow, updateStatus, updateFavorite, deleteById |

### 步骤3.4: Database 数据库

**创建2个数据库文件：**

| 文件名 | 位置 | 内容 |
|--------|------|------|
| `AliveDatabase.kt` | `data/db/` | @Database(entities=[AliveImage, Task], version=1) |
| `DatabaseConverters.kt` | `data/db/` | TaskStatus枚举转换器 |

### 步骤3.5: Repository 数据仓库

**创建1个Repository文件：**

| 文件名 | 位置 | 职责 |
|--------|------|------|
| `AliveRepository.kt` | `data/repository/` | 聚合DAO和API调用 |

**关键方法：**
- AliveImage: getAllAliveImages, insertAliveImage, getAliveImageById, deleteAliveImage
- Task: getAllTasks, getTasksByStatus, getFavoriteTasks, insertTask, getTaskById, updateTask, updateTaskFavorite, deleteTask
- API: extract8Frames, submitCircleRemoval, getTaskStatus, updateFavorite

---

## 🌐 网络层实现

### 步骤4.1: API 接口定义

**创建1个API文件：**

| 文件名 | 位置 | 内容 |
|--------|------|------|
| `AliveApi.kt` | `network/api/` | 4个suspend函数：extract8Frames, submitCircleRemoval, getTaskStatus, updateFavorite |

### 步骤4.2: Mock API 实现

**创建1个Mock文件：**

| 文件名 | 位置 | 内容 |
|--------|------|------|
| `MockAliveApi.kt` | `network/api/` | AliveApi接口的Mock实现，每个函数带delay模拟 |

**延迟时间：**
- extract8Frames: 2000ms
- submitCircleRemoval: 1000ms
- getTaskStatus: 500ms
- updateFavorite: 500ms

### 步骤4.3: API 客户端

**创建1个客户端文件：**

| 文件名 | 位置 | 内容 |
|--------|------|------|
| `ApiClient.kt` | `network/` | Retrofit实例和Mock实例的工厂，支持切换 |

---

## 🎯 ViewModel层实现

### 步骤5.1: ViewModel工厂

**创建1个工厂文件：**

| 文件名 | 位置 | 职责 |
|--------|------|------|
| `AliveViewModelFactory.kt` | `ui/viewmodel/` | 为所有ViewModel创建实例 |

### 步骤5.2: 核心ViewModel

**创建7个ViewModel文件：**

| 文件名 | 位置 | LiveData/StateFlow | 方法 |
|--------|------|-------------------|------|
| `SharedViewModel.kt` | `ui/viewmodel/` | currentAliveImage, currentTask, framesData, currentFrameIndex, circleSelections, markedAliveImagePath, resultAliveImagePath | setAliveImage, setTask, setFramesData, setCurrentFrameIndex, addCircleSelection, removeCircleSelection, getCircleSelectionsForFrame, clearCircleSelections, setMarkedAliveImagePath, setResultAliveImagePath, saveTask, updateTask |
| `Fragment1ViewModel.kt` | `ui/viewmodel/` | selectedImage, isLoading, errorMessage | saveSelectedImage, clearError |
| `Fragment2ViewModel.kt` | `ui/viewmodel/` | isLoading, errorMessage, framesExtracted | extract8Frames, clearError |
| `Fragment3ViewModel.kt` | `ui/viewmodel/` | isLoading, errorMessage, submissionSuccess | submitCircleRemoval, clearError |
| `Fragment4ViewModel.kt` | `ui/viewmodel/` | isLoading, taskCompleted, resultImagePath, errorMessage | pollTaskStatus, stopPolling, updateTaskStatus |
| `Fragment5ViewModel.kt` | `ui/viewmodel/` | isLoading, errorMessage, isFavorite, downloadSuccess | updateFavoriteStatus, downloadResultImage, shareResultImage, clearError |
| `TaskListViewModel.kt` | `ui/viewmodel/` | allTasks, completedTasks, pendingTasks (StateFlow) | loadTasks, deleteTask, updateTaskFavorite, getTaskById |

---

## 🎨 UI层实现

### 步骤6.1: Activity

**创建1个Activity文件：**

| 文件名 | 位置 | 职责 |
|--------|------|------|
| `MainActivity.kt` | `ui/` | 初始化数据库、Repository、SharedViewModel，设置NavController |

### 步骤6.2: Fragment 组件

**创建8个Fragment文件：**

| 文件名 | 位置 | 功能 |
|--------|------|------|
| `HomeFragment.kt` | `ui/fragment/` | 主页入口，两个按钮：Start Process、View Tasks |
| `Fragment1.kt` | `ui/fragment/` | 选择ALIVE图片或视频，验证格式和时长 |
| `Fragment2.kt` | `ui/fragment/` | 显示加载动画，自动调用extract8Frames API，自动跳转 |
| `Fragment3.kt` | `ui/fragment/` | 显示marked alive图，手绘红色圆圈，8帧列表，提交 |
| `Fragment4.kt` | `ui/fragment/` | 显示加载动画，轮询任务状态，完成后自动跳转或取消返回 |
| `Fragment5.kt` | `ui/fragment/` | 显示结果图，分享、收藏、下载按钮，导航 |
| `TaskListFragment.kt` | `ui/fragment/` | 显示任务列表，支持点击查看完成任务 |

### 步骤6.3: 自定义View

**创建1个自定义View文件：**

| 文件名 | 位置 | 功能 |
|--------|------|------|
| `CircleDrawingImageView.kt` | `ui/view/` | 支持手绘红色圆圈的ImageView，保存CircleSelection列表 |

### 步骤6.4: RecyclerView适配器

**创建2个Adapter文件：**

| 文件名 | 位置 | 用途 |
|--------|------|------|
| `FrameAdapter.kt` | `ui/adapter/` | 8帧列表，item显示帧索引和图像 |
| `TaskListAdapter.kt` | `ui/adapter/` | 任务列表，item显示缩略图、状态、时间、收藏标记 |

---

## 🛠️ 工具类实现

### 步骤7.1: 媒体工具

**创建1个工具文件：**

| 文件名 | 位置 | 方法 |
|--------|------|------|
| `MediaUtils.kt` | `util/` | isAliveImage, getVideoDuration, getVideoDurationFromPath, getFileSize, getMimeType, isVideo, getFileName, getRealPath |

### 步骤7.2: 视频工具

**创建1个工具文件：**

| 文件名 | 位置 | 方法 |
|--------|------|------|
| `VideoUtils.kt` | `util/` | extract8FramesFromVideo, saveBitmap, createAliveFromFrames |

---

## 📄 资源文件

### 步骤8.1: 布局文件（18个XML）

**Activity布局：**
```
activity_main.xml
├─ Toolbar
└─ NavHostFragment (nav_graph)
```

**Fragment布局：**
```
fragment_home.xml
├─ TextView (标题)
├─ Button (Start Process)
└─ Button (View Tasks)

fragment1.xml
├─ TextView (说明)
├─ ImageView (图片区域)
├─ TextView (图片信息)
├─ Button (Back/Next)

fragment2.xml
├─ TextView (标题)
├─ ProgressBar (加载动画)
└─ Button (Back)

fragment3.xml
├─ TextView (说明)
├─ CircleDrawingImageView (主显示)
├─ TextView (帧选择说明)
├─ RecyclerView (8帧列表)
├─ Button (Undo/Clear All/Submit)
├─ ProgressBar
└─ Button (Back)

fragment4.xml
├─ TextView (标题)
├─ ProgressBar (加载动画)
├─ TextView (说明)
└─ Button (Cancel)

fragment5.xml
├─ TextView (标题)
├─ ImageView (结果图)
├─ LinearLayout (Share/Favorite/Download)
├─ LinearLayout (Home/TaskList)
└─ ProgressBar

fragment_task_list.xml
├─ TextView (标题)
├─ RecyclerView (任务列表)
└─ Button (Back)
```

**Item布局：**
```
item_frame.xml
├─ ImageView (帧图像)
└─ TextView (帧索引)

item_task.xml
├─ ImageView (缩略图)
├─ LinearLayout
│  ├─ TextView (状态)
│  └─ TextView (创建时间)
└─ ImageView (收藏星标)
```

**Drawable：**
```
image_area_background.xml (圆角灰色框)
ic_favorite.xml (星形图标)
```

### 步骤8.2: 导航图（navigation/nav_graph.xml）

**Fragment节点和Action：**
```
fragment_home
├─ action_home_to_fragment1
└─ action_home_to_task_list

fragment1
├─ action_fragment1_to_fragment2
└─ action_fragment1_to_home

fragment2
├─ action_fragment2_to_fragment3
└─ action_fragment2_to_fragment1

fragment3
├─ action_fragment3_to_fragment4
└─ action_fragment3_to_fragment2

fragment4
├─ action_fragment4_to_fragment5
└─ action_fragment4_to_home

fragment5
├─ action_fragment5_to_home
└─ action_fragment5_to_task_list

fragment_task_list
├─ action_task_list_to_home
└─ action_task_list_to_fragment5
```

### 步骤8.3: 字符串资源（values/strings.xml）

```xml
<string name="app_name">Alive</string>
<string name="alive_person_removal">ALIVE Person Removal</string>
<!-- 其他字符串资源根据UI需要添加 -->
```

---

## ⚙️ 配置文件

### 步骤9.1: build.gradle.kts

**关键配置：**
```gradle
compileSdk = 35
minSdk = 28
targetSdk = 35

compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

buildFeatures {
    compose = true
    viewBinding = true
}

packagingOptions {
    resources {
        excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
}
```

### 步骤9.2: AndroidManifest.xml

**必需元素：**
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />

<activity
    android:name=".ui.MainActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

---

## 📋 完整的文件创建清单

### 总计：61个文件

**Kotlin文件（27个）：**
- 1个Activity
- 8个Fragment
- 7个ViewModel
- 1个ViewModel工厂
- 2个DAO
- 2个数据库文件
- 1个Repository
- 2个API文件
- 1个自定义View
- 2个Adapter
- 2个工具类
- 1个Entity文件（3个类）
- 1个DTO文件（6个类）
- 1个SelectionData文件（2个类）

**XML资源文件（18个）：**
- 1个Activity布局
- 7个Fragment布局
- 2个Item布局
- 2个Drawable
- 1个Navigation图
- 1个Theme配置（已有）
- 1个Strings资源
- 2个其他配置（data_extraction_rules, backup_rules）

**配置文件（4个）：**
- build.gradle.kts
- AndroidManifest.xml
- nav_graph.xml
- strings.xml

**文档文件（12个）：**
- README.md
- PROJECT_SUMMARY.md
- QUICK_START.md
- CHECKLIST.md
- DEVELOPER_GUIDE.md
- IMPLEMENTATION_CHECKLIST.md（本文件）

---

## 🎯 快速实现步骤（顺序）

### 第1阶段：项目基础
1. ✅ 创建Android项目
2. ✅ 配置build.gradle.kts依赖
3. ✅ 配置AndroidManifest.xml权限
4. ✅ 创建目录结构

### 第2阶段：数据层
5. ✅ 创建Entity实体类（3个文件）
6. ✅ 创建DTO数据类（1个文件）
7. ✅ 创建DAO接口（2个文件）
8. ✅ 创建数据库类（2个文件）
9. ✅ 创建Repository（1个文件）

### 第3阶段：网络层
10. ✅ 创建API接口（1个文件）
11. ✅ 创建Mock实现（1个文件）
12. ✅ 创建API客户端（1个文件）

### 第4阶段：ViewModel层
13. ✅ 创建ViewModel工厂（1个文件）
14. ✅ 创建SharedViewModel（1个文件）
15. ✅ 创建6个业务ViewModel（6个文件）
16. ✅ 创建TaskListViewModel（1个文件）

### 第5阶段：UI层
17. ✅ 创建MainActivity（1个文件）
18. ✅ 创建8个Fragment（8个文件）
19. ✅ 创建自定义View（1个文件）
20. ✅ 创建2个Adapter（2个文件）

### 第6阶段：工具类
21. ✅ 创建MediaUtils（1个文件）
22. ✅ 创建VideoUtils（1个文件）

### 第7阶段：资源文件
23. ✅ 创建18个XML布局文件
24. ✅ 创建navigation/nav_graph.xml
25. ✅ 配置strings.xml

### 第8阶段：文档
26. ✅ 创建所有文档文件

---

## 🔍 核心逻辑提要

### Fragment1 流程
```
打开相册 → 获取URI → 验证格式/时长 → 显示图像 → Next → 保存到数据库 → 跳转Fragment2
```

### Fragment2 流程
```
进入 → 调用extract8Frames API → 收到8帧路径 → 自动跳转Fragment3
```

### Fragment3 流程
```
加载marked alive图 → 用户手绘红色圆圈 → 切换帧 → Submit → 发送圈选数据 → 创建Task → 跳转Fragment4
```

### Fragment4 流程
```
进入 → 开始轮询(每2秒) → status==completed → 更新Task状态 → 自动跳转Fragment5
或 → 用户点击Cancel → 返回Home
```

### Fragment5 流程
```
显示结果图 → Share/Favorite/Download → 导航到Home或TaskList
```

---

## 💡 API模拟数据格式

### extract8Frames
```json
{
  "status": "success",
  "frame_paths": [8个路径],
  "marked_alive_path": "标记后的alive图路径"
}
```

### submitCircleRemoval
```json
{
  "status": "success",
  "task_id": 1,
  "message": "提交成功"
}
```

### getTaskStatus
```json
{
  "task_id": 1,
  "status": "pending|processing|completed",
  "result_image_path": "结果路径",
  "error_message": null
}
```

### updateFavorite
```json
{
  "status": "success",
  "is_favorite": 1,
  "message": "更新成功"
}
```

---

## 📊 数据库表结构

### alive_images表
```sql
CREATE TABLE alive_images (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    filePath TEXT NOT NULL,
    fileName TEXT NOT NULL,
    fileSize INTEGER,
    mimeType TEXT,
    isVideo INTEGER,
    duration INTEGER,
    createdTime INTEGER,
    thumbnailPath TEXT
)
```

### tasks表
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    aliveImageId INTEGER,
    markingImagePath TEXT,
    circleSelectionsJson TEXT,
    resultImagePath TEXT,
    status TEXT,
    createdTime INTEGER,
    completedTime INTEGER,
    isFavorite INTEGER,
    errorMessage TEXT
)
```

---

## 🚀 快速构建命令

```bash
# 清理和构建
./gradlew clean build

# 安装Debug版本
./gradlew installDebug

# 运行应用
./gradlew run

# 生成Release版本
./gradlew assembleRelease
```

---

## ✅ 验证清单

实现完成后检查：

- [ ] 项目能成功编译
- [ ] 没有编译警告（除了库警告）
- [ ] 所有权限已声明
- [ ] 数据库能正确创建
- [ ] 所有Fragment能正常导航
- [ ] Mock API能正确返回数据
- [ ] 圆圈绘制功能正常
- [ ] 任务轮询能工作
- [ ] 分享/收藏/下载功能正常
- [ ] 没有内存泄漏

---

## 📌 注意事项

1. **最小SDK**: 确保设置为28（为了支持作用域存储）
2. **依赖版本**: 使用文件中指定的精确版本，避免版本不兼容
3. **注解冲突**: 必须排除`com.intellij:annotations`以避免重复类
4. **View Binding**: 确保在build.gradle.kts中启用viewBinding
5. **Navigation**: nav_graph.xml必须放在res/navigation目录下
6. **Coroutines**: 所有网络和数据库操作必须在协程中
7. **Mock数据**: 延迟设置要合理，以模拟真实网络

---

## 🎓 架构关键点

1. **UI → ViewModel → Repository → Model**: 单向依赖
2. **SharedViewModel**: Fragment间通过它共享数据，不直接通信
3. **Mock优先**: 开发测试使用Mock，上线前切换真实API
4. **响应式**: 所有数据变化通过LiveData/StateFlow通知UI
5. **协程作用域**: 使用viewModelScope确保生命周期安全

---

## 📝 此清单用途

✅ **快速重复构建**: 按照此清单顺序创建文件，可快速构建相同应用
✅ **新团队成员**: 新成员可根据此清单快速理解项目结构
✅ **代码审查**: 用于验证是否遵循既定结构
✅ **文档参考**: 完整的文件和功能映射关系

---

**最后更新**: 2025-10-25
**版本**: 1.0
**状态**: 完成可用 ✅
