# ALIVE Person Removal - Android App

![Kotlin](https://img.shields.io/badge/kotlin-1.9+-blue.svg)
![Android](https://img.shields.io/badge/android-35-green.svg)
![Architecture](https://img.shields.io/badge/architecture-MVVM-yellowgreen.svg)

一个**严格遵循MVVM架构**的Android应用，用于处理ALIVE图像中的人物消除。

## 📋 功能概览

- ✅ **图片/视频选择** - 支持ALIVE图片和小于3秒的视频
- ✅ **自动帧提取** - 智能提取8帧（首帧、末帧、中间均匀分布）
- ✅ **手动圆圈圈选** - 用红色圆圈标记要消除的人物
- ✅ **后台任务处理** - 异步处理，支持任务状态轮询
- ✅ **结果展示** - 分享、收藏、下载功能
- ✅ **任务管理** - 完整的任务历史记录

## 🏗️ 项目架构

```
┌─────────────────────────────────┐
│        UI Layer                 │
│  (Fragment + ViewModel)         │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│   ViewModel Layer               │
│  (Business Logic)               │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│   Repository Layer              │
│  (Data Aggregation)             │
└─────────────────────────────────┘
              ↓
┌─────────────────────────────────┐
│   Model Layer                   │
│  (Room + Retrofit API)          │
└─────────────────────────────────┘
```

## 🚀 快速开始

### 前置条件
- Android Studio Hedgehog或更新版本
- JDK 11+
- Android SDK 35+

### 安装步骤

1. **克隆项目**
   ```bash
   cd d:\soft\workspace\Alive
   ```

2. **同步Gradle**
   - Android Studio会自动同步依赖
   - 或运行：`./gradlew build`

3. **运行应用**
   - 点击 Run 按钮或按 `Shift + F10`
   - 选择模拟器或设备

## 📱 应用流程

### 场景演示

```
用户进入应用
    ↓
点击"Start Process"按钮
    ↓
Fragment1: 选择ALIVE图片或视频(<3秒)
    ↓
Fragment2: 自动提取8帧 (显示加载动画)
    ↓
Fragment3: 手动圆圈圈选要消除的人物
    ↓
Fragment4: 等待后端处理 (轮询任务状态)
    ↓
Fragment5: 显示处理结果
    ├─ 分享结果
    ├─ 收藏/取消收藏
    └─ 下载到相册

同时可查看：
任务列表 → 浏览所有历史任务
```

## 🛠️ 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Kotlin | 1.9+ | 编程语言 |
| Coroutines | 1.7.3 | 异步编程 |
| Room | 2.6.1 | 本地数据库 |
| Retrofit | 2.9.0 | 网络请求 |
| Glide | 4.16.0 | 图片加载 |
| Navigation | 2.7.5 | Fragment导航 |
| ViewModel | 2.6.2 | UI状态管理 |
| LiveData | 2.6.2 | 响应式数据 |

## 📁 项目结构

```
app/src/main/
├── java/com/example/alive/
│   ├── data/                    # 数据层
│   │   ├── dao/                # 数据库访问
│   │   ├── db/                 # Room数据库
│   │   ├── dto/                # API数据对象
│   │   ├── entity/             # 数据实体
│   │   └── repository/         # 数据仓库
│   ├── network/                # 网络层
│   │   └── api/               # Retrofit API
│   ├── ui/                     # UI层
│   │   ├── fragment/          # Fragment组件
│   │   ├── view/              # 自定义视图
│   │   ├── adapter/           # 列表适配器
│   │   └── viewmodel/         # ViewModel类
│   └── util/                   # 工具类
└── res/
    ├── layout/                 # XML布局文件
    ├── drawable/               # 图形资源
    ├── navigation/             # 导航配置
    └── values/                 # 字符串等资源
```

## 📚 核心功能详解

### Fragment1 - 图片选择
- 点击区域打开系统相册
- 自动验证ALIVE图片或视频时长
- 显示选中图像的信息

### Fragment2 - 帧提取
- 自动调用后端API
- 提取8帧（首帧、末帧、中间6帧）
- 接收标记后的alive图路径

### Fragment3 - 圆圈圈选
- 上方显示当前帧
- 手指绘制红色圆圈标记人物
- 下方8帧列表支持帧切换
- 提交后发送圈选数据

### Fragment4 - 任务等待
- 后台轮询任务状态
- 完成后自动跳转到结果页
- 支持取消返回主页

### Fragment5 - 结果展示
- 显示处理后的alive图
- **分享** - 调用系统分享
- **收藏** - 标记重要任务
- **下载** - 保存到相册

### TaskListFragment - 任务管理
- 查看所有提交的任务
- 显示任务状态（待处理/处理中/已完成）
- 点击完成任务查看结果

## 🔌 API接口

应用使用Mock实现进行开发测试，支持轻松切换到真实后端。

### API端点（Mock实现）

```
POST /api/extract8frames
  请求: { alive_image_path: String }
  响应: { status, frame_paths, marked_alive_path }

POST /api/circleremoval
  请求: { task_id, alive_image_path, selections_json }
  响应: { status, task_id }

GET /api/taskstatus?task_id=1
  响应: { task_id, status, result_image_path, error_message }

POST /api/favorite
  请求: { task_id, is_favorite }
  响应: { status, is_favorite }
```

### 切换到真实API

编辑 `ApiClient.kt`：
```kotlin
fun getApi(): AliveApi {
    // 改为返回 retrofitApi
    return retrofitApi  // 替代 mockApi
}
```

## 💾 数据库设计

### AliveImage表
```sql
CREATE TABLE alive_images (
    id INTEGER PRIMARY KEY,
    filePath TEXT,
    fileName TEXT,
    fileSize INTEGER,
    mimeType TEXT,
    isVideo INTEGER,
    duration INTEGER,
    createdTime INTEGER,
    thumbnailPath TEXT
)
```

### Task表
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY,
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

## 🎨 UI特色

- **响应式设计** - 适配各种屏幕尺寸
- **实时反馈** - 加载动画和进度提示
- **流畅导航** - 使用Navigation Component
- **自定义绘图** - CircleDrawingImageView支持红色圆圈绘制

## 📱 权限配置

```xml
<!-- AndroidManifest.xml -->
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
```

## 🔍 调试技巧

### 查看数据库
```bash
# 使用Android Studio Database Inspector
- View → Tool Windows → Database Inspector
- 选择alive_database
```

### 查看日志
```bash
# 过滤应用日志
adb logcat | grep -i alive
```

### 模拟API延迟
在 `MockAliveApi.kt` 中修改 `delay()` 时间。

## 📖 文档

- [项目总结](PROJECT_SUMMARY.md) - 详细的架构和实现说明
- [快速开始](QUICK_START.md) - 快速入门指南

## 🚧 已知限制

1. 图片处理使用简化实现，实际ALIVE格式需对接真实编码库
2. 视频帧提取基于MediaMetadataRetriever，处理大文件可能较慢
3. Mock API使用固定延迟，不完全模拟真实网络情况

## 🎯 未来改进

- [ ] 支持拖拽调整已绘制的圆圈
- [ ] 实现圆圈预览和参数调整UI
- [ ] 添加撤销/重做功能
- [ ] 支持批量上传任务
- [ ] 实现离线模式
- [ ] 添加单元测试和UI测试
- [ ] 性能优化（内存、网络）

## 📄 许可证

MIT License - 详见LICENSE文件

## 👤 作者

开发团队

## 💬 反馈与支持

遇到问题？
1. 检查logcat输出获取错误信息
2. 查看PROJECT_SUMMARY.md了解详细实现
3. 检查QUICK_START.md的常见问题部分

---

**祝你开发愉快！** 🎉

最后更新：2025-10-25
