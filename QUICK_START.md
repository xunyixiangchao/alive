# 项目快速开始指南

## 项目已完成的内容

✅ 完整的MVVM架构实现
✅ 7个Fragment（Home、Fragment1-5、TaskList）
✅ 数据库（Room）实现
✅ 网络请求（Retrofit + Mock）
✅ 图片/视频选择与验证
✅ 8帧提取和圆圈绘制
✅ 任务管理和结果展示

---

## 项目结构速览

```
app/src/main/java/com/example/alive/
├── data/                    # 数据层
│   ├── dao/                # 数据库访问对象
│   ├── db/                 # Room数据库
│   ├── dto/                # API数据传输对象
│   ├── entity/             # 数据实体
│   └── repository/         # 数据仓库
├── network/                # 网络层
│   └── api/               # Retrofit API和Mock实现
├── ui/                     # UI层
│   ├── fragment/          # 所有Fragment
│   ├── view/              # 自定义视图
│   ├── adapter/           # RecyclerView适配器
│   └── viewmodel/         # 所有ViewModel
└── util/                   # 工具类
```

---

## 如何运行项目

### 步骤1：打开项目
```bash
cd d:\soft\workspace\Alive
# 用Android Studio打开项目
```

### 步骤2：同步Gradle
- Android Studio会自动下载依赖
- 或手动运行：`./gradlew build`

### 步骤3：运行应用
- 点击Run按钮或按下 `Shift + F10`
- 选择设备或模拟器
- 应用会启动到HomeFragment

---

## 主要界面流程

```
HomeFragment
    ↓
Fragment1 (选择图片/视频)
    ↓
Fragment2 (提取8帧，自动)
    ↓
Fragment3 (手动圈选)
    ↓
Fragment4 (等待处理)
    ↓
Fragment5 (显示结果)

同时可访问：
TaskListFragment (查看历史任务)
```

---

## 核心功能说明

### 1. 图片/视频选择 (Fragment1)
- **打开相册**：点击图片区域
- **验证规则**：
  - ALIVE图片需通过`BitmapFactory.decode`验证
  - 视频必须小于3秒
- **错误提示**：不符合条件会显示Toast提示

### 2. 帧提取 (Fragment2)
- **自动过程**：进入后立即开始
- **API调用**：发送alive图路径到后端
- **响应**：获取8帧路径和marked图路径
- **自动跳转**：完成后自动进入Fragment3

### 3. 圆圈圈选 (Fragment3)
- **绘制方式**：手指点击并拖动绘制红色圆圈
- **切换帧**：下方列表可选择要编辑的帧
- **按钮功能**：
  - **Undo**：删除当前帧最后一个圆圈
  - **Clear All**：清除当前帧所有圆圈
  - **Submit**：提交所有圈选数据
- **提交**：圈选数据以JSON格式发送到后端

### 4. 任务等待 (Fragment4)
- **轮询机制**：每2秒查询一次任务状态
- **状态转换**：pending → processing → completed
- **自动跳转**：完成后自动进入Fragment5
- **手动取消**：点击Cancel返回主页

### 5. 结果展示 (Fragment5)
- **显示结果**：展示处理后的alive图
- **分享**：调用系统分享功能
- **收藏**：调用API并在数据库中标记
- **下载**：保存到Pictures/Alive目录

### 6. 任务列表 (TaskListFragment)
- **显示所有任务**：按创建时间倒序
- **状态指示**：Pending/Processing/Completed/Failed
- **收藏标记**：已收藏任务显示星标
- **查看详情**：点击完成的任务查看结果

---

## API调用（Mock实现）

所有API调用都会返回模拟数据，延迟模拟真实网络：

```kotlin
// MockAliveApi.kt 中的实现
- extract8Frames()      // 延迟2秒，返回8个frame路径
- submitCircleRemoval() // 延迟1秒，返回success
- getTaskStatus()       // 延迟500ms，随机返回任务状态
- updateFavorite()      // 延迟500ms，返回收藏状态
```

**切换到真实API**：
1. 修改 `ApiClient.kt` 的 `getApi()` 方法
2. 更新BASE_URL为实际后端地址
3. 运行应用

---

## 数据库（Room）

### 存储的表

1. **alive_images**
   - id (PrimaryKey)
   - filePath
   - fileName
   - fileSize
   - mimeType
   - isVideo
   - duration
   - createdTime
   - thumbnailPath

2. **tasks**
   - id (PrimaryKey)
   - aliveImageId (外键)
   - markingImagePath
   - circleSelectionsJson
   - resultImagePath
   - status (PENDING/MARKING/PROCESSING/COMPLETED/FAILED)
   - createdTime
   - completedTime
   - isFavorite
   - errorMessage

### 查询数据
- Android Studio → Database Inspector
- 或使用adb shell访问SQLite

---

## 常见问题

### Q: 如何测试视频选择？
A: 使用Android Studio模拟器中的相册，或使用命令行推送测试视频：
```bash
adb push test_video.mp4 /sdcard/DCIM/
```

### Q: 圆圈半径太小/太大？
A: 修改 `CircleDrawingImageView.kt` 中的最小半径判断：
```kotlin
if (currentRadius > 10) { // 改变这个值
```

### Q: 如何调试网络请求？
A:
1. 在 `ApiClient.kt` 添加OkHttp日志拦截器
2. 或在Logcat搜索API相关日志

### Q: 如何使用真实后端？
A:
1. 更新 `ApiClient.kt` 中的BASE_URL
2. 将 `getApi()` 改为返回 `retrofitApi`
3. 确保后端接口与 `AliveApi.kt` 签名一致

### Q: 应用崩溃怎么办？
A:
1. 检查logcat错误信息
2. 确保权限已申请（Android 11+需处理作用域存储）
3. 清除应用数据重新运行

---

## 权限处理

应用已在 `AndroidManifest.xml` 中声明：
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.CAMERA" />
```

对于**Android 6.0+**，运行时权限已在Fragment中处理。

---

## 下一步改进建议

1. **完善圆圈编辑**
   - 支持拖拽调整已绘制的圆圈
   - 显示圆圈大小信息
   - 支持删除指定圆圈

2. **优化图片处理**
   - 添加图片预处理（缩放、压缩）
   - 支持更多视频格式
   - 实现视频缩略图提取

3. **增强用户体验**
   - 添加加载进度百分比
   - 实现结果预览动画
   - 添加操作历史记录

4. **安全性**
   - 添加API密钥验证
   - 实现请求签名机制
   - 添加数据加密存储

5. **性能优化**
   - 实现图片缓存策略
   - 优化大视频处理
   - 添加内存监测

---

## 文件清单

### Kotlin源文件 (27个)
- 2个Activity/Fragment容器
- 7个业务Fragment
- 1个自定义View
- 2个RecyclerView Adapter
- 1个API接口 + 1个Mock实现
- 7个ViewModel
- 1个Repository
- 2个DAO
- 2个数据库文件
- 2个工具类
- 3个Entity/DTO数据类

### XML资源文件 (18个)
- 1个Activity布局
- 7个Fragment布局
- 2个Item布局
- 2个Drawable (背景、图标)
- 1个Navigation图
- 1个Theme配置
- 2个字符串资源

---

## 项目完成情况总结

| 功能项 | 状态 | 备注 |
|--------|------|------|
| 基础架构 | ✅ | MVVM + Navigation |
| Fragment1 | ✅ | 图片/视频选择和验证 |
| Fragment2 | ✅ | 自动8帧提取 |
| Fragment3 | ✅ | 手绘圆圈圈选 |
| Fragment4 | ✅ | 任务状态轮询 |
| Fragment5 | ✅ | 结果展示+分享收藏下载 |
| TaskList | ✅ | 完整的任务管理 |
| 数据库 | ✅ | Room集成 |
| 网络请求 | ✅ | Retrofit + Mock |
| 权限处理 | ✅ | 已配置 |

---

## 联系与支持

- 项目文档：`PROJECT_SUMMARY.md`
- 问题排查：查看logcat输出
- 扩展开发：参考MVVM架构模板

**祝你开发愉快！** 🎉
