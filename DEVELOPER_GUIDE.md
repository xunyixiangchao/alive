# 开发者指南

本文档为参与本项目开发的工程师提供详细的开发规范和贡献指南。

---

## 📋 目录

1. [开发环境配置](#开发环境配置)
2. [代码规范](#代码规范)
3. [Git工作流](#git工作流)
4. [调试技巧](#调试技巧)
5. [常见问题](#常见问题)
6. [性能优化](#性能优化)
7. [安全建议](#安全建议)

---

## 🖥️ 开发环境配置

### IDE设置
```
Android Studio Hedgehog或更新版本
- Java 11+
- Android SDK 35
- Gradle 8.0+
```

### 项目配置
```bash
# 克隆项目
git clone <repository>
cd Alive

# 更新submodules（如有）
git submodule update --init --recursive

# 同步Gradle
./gradlew sync

# 运行构建
./gradlew build
```

### 快速开发设置
```bash
# 配置IDE代码风格
- File → Settings → Editor → Code Style → Kotlin
- 导入 .idea/codeStyleSettings.xml

# 配置Run配置
- Run → Edit Configurations
- 选择emulator或device
```

---

## 📐 代码规范

### Kotlin命名规范

```kotlin
// 类名 - PascalCase
class AliveDatabase { }
class Fragment1ViewModel { }

// 函数名 - camelCase
fun extractFrames() { }
fun getTaskStatus() { }

// 常量 - UPPER_SNAKE_CASE
const val DEFAULT_TIMEOUT = 5000
const val MAX_FRAME_COUNT = 8

// 变量 - camelCase
val aliveImage: AliveImage
var isLoading: Boolean

// 私有成员 - 前缀_
private val _circleSelections = MutableLiveData<List<CircleSelection>>()
val circleSelections: LiveData<List<CircleSelection>> = _circleSelections
```

### 函数规范

```kotlin
// 明确的参数类型
fun submitTask(taskId: Long, aliveImagePath: String): Boolean

// 合理的函数长度（<50行）
// 复杂逻辑分解为多个函数

// Suspend函数必须有clear的名称
suspend fun extract8Frames(path: String): Extract8FramesResponse

// 使用@Throws明确异常
@Throws(IOException::class)
fun saveImage(bitmap: Bitmap, path: String)
```

### 类设计规范

```kotlin
// 单一职责原则
class CircleRemovalViewModel(repository: AliveRepository) : ViewModel() {
    // 只负责与圆圈移除相关的业务逻辑
}

// 依赖注入
class TaskRepository(
    private val taskDao: TaskDao,
    private val api: AliveApi
) { }

// 懒加载
private val _tasks = MutableStateFlow<List<Task>>(emptyList())
val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()
```

### 注释规范

```kotlin
// 使用KDoc注释public方法
/**
 * 提交圆圈移除任务
 *
 * @param taskId 任务ID
 * @param selections 圆圈圈选数据
 * @return 是否提交成功
 * @throws IOException 如果网络请求失败
 */
suspend fun submitRemovalTask(
    taskId: Long,
    selections: Map<Int, List<CircleSelection>>
): Boolean

// 使用行注释解释复杂逻辑
// 如果半径小于10px，认为无效圆圈
if (radius < 10) {
    isValid = false
}
```

### 格式化

```bash
# Android Studio内置格式化
Ctrl + Alt + L (Windows/Linux)
Cmd + Alt + L (macOS)

# 自动优化导入
Ctrl + Alt + O (Windows/Linux)
Cmd + Alt + O (macOS)
```

---

## 🔀 Git工作流

### 分支命名规范

```
feature/add-xxx     - 新功能
fix/xxx-bug         - 修复bug
refactor/xxx        - 代码重构
docs/xxx            - 文档更新
test/xxx            - 测试相关
chore/xxx           - 构建、配置等
```

### Commit消息规范

```
<type>: <subject>

<body>

<footer>

类型:
- feat: 新功能
- fix: 修复bug
- refactor: 代码重构
- docs: 文档更新
- style: 格式调整
- test: 测试相关
- chore: 构建、配置等

示例:
feat: add circle drawing feature for frame selection

Implement custom CircleDrawingImageView that supports:
- Drawing circles with touch input
- Multiple circle selections per frame
- Undo and clear functionality

Closes #123

Co-Authored-By: Developer <dev@example.com>
```

### 工作流程

```bash
# 1. 创建feature分支
git checkout -b feature/add-xxx

# 2. 进行开发，定期commit
git add .
git commit -m "feat: implement xxx"

# 3. 推送到远程
git push origin feature/add-xxx

# 4. 发起Pull Request
# 在GitHub/GitLab上创建PR
# - 清晰的title和description
# - 关联相关issue
# - 等待code review

# 5. 代码审核后merge
git checkout develop
git pull origin develop
git merge feature/add-xxx
git push origin develop

# 6. 删除本地和远程分支
git branch -d feature/add-xxx
git push origin --delete feature/add-xxx
```

---

## 🔧 调试技巧

### Android Studio调试

```
F8     - Step over
F7     - Step into
Ctrl+8 - Step out
Ctrl+Shift+D - Evaluate expression
Ctrl+F9 - Run to cursor
```

### Logcat过滤

```bash
# 过滤应用日志
adb logcat | grep com.example.alive

# 过滤特定TAG
adb logcat | grep Fragment3ViewModel

# 保存日志到文件
adb logcat > logfile.txt

# 实时查看日志
adb logcat -f output.log
```

### 数据库检查

```
View → Tool Windows → Database Inspector
选择 alive_database 查看表结构和数据
```

### 网络请求调试

```kotlin
// 在ApiClient中添加日志拦截器
private val httpClient = OkHttpClient.Builder()
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()
```

### 内存分析

```
View → Tool Windows → Profiler
- Memory: 检查内存泄漏
- CPU: 检查性能问题
- Network: 监控网络请求
```

---

## ❓ 常见问题

### Q: 如何添加新的ViewModel？

A: 遵循以下步骤：
```kotlin
// 1. 创建ViewModel类
class NewFeatureViewModel(repository: AliveRepository) : ViewModel() {
    // 实现业务逻辑
}

// 2. 在AliveViewModelFactory中添加
override fun <T : ViewModel> create(modelClass: Class<T>): T {
    return when {
        modelClass.isAssignableFrom(NewFeatureViewModel::class.java) -> {
            NewFeatureViewModel(repository) as T
        }
        else -> super.create(modelClass)
    }
}

// 3. 在Fragment中使用
val viewModel = ViewModelProvider(this, factory)[NewFeatureViewModel::class.java]
```

### Q: 如何添加新的API接口？

A:
```kotlin
// 1. 在AliveApi中添加方法
interface AliveApi {
    @POST("/api/newfeature")
    suspend fun newFeatureAPI(@Body request: NewRequest): NewResponse
}

// 2. 在MockAliveApi中实现
override suspend fun newFeatureAPI(request: NewRequest): NewResponse {
    delay(1000)
    return NewResponse(status = "success")
}

// 3. 在Repository中添加wrapper
suspend fun callNewFeature(request: NewRequest): NewResponse {
    return api.newFeatureAPI(request)
}

// 4. 在ViewModel中使用
viewModelScope.launch {
    val response = repository.callNewFeature(request)
}
```

### Q: 如何处理网络异常？

A:
```kotlin
viewModelScope.launch {
    try {
        val response = repository.extract8Frames(imagePath)
        if (response.status == "success") {
            // 处理成功
        } else {
            errorMessage.value = response.message ?: "Unknown error"
        }
    } catch (e: IOException) {
        errorMessage.value = "Network error: ${e.message}"
    } catch (e: Exception) {
        errorMessage.value = "Error: ${e.message}"
    }
}
```

### Q: 如何优化Fragment切换性能？

A:
```kotlin
// 1. 使用setMaxLifecycle防止Fragment重新创建
supportFragmentManager.beginTransaction()
    .replace(R.id.container, fragment)
    .setMaxLifecycle(oldFragment, Lifecycle.State.STARTED)
    .addToBackStack(null)
    .commit()

// 2. 使用instantiateViewBinding以避免重复创建
private val binding by viewBinding(FragmentBinding::bind)

// 3. 在onDestroyView中清理资源
override fun onDestroyView() {
    adapter = null
    super.onDestroyView()
}
```

---

## ⚡ 性能优化

### 内存优化

```kotlin
// 1. 及时释放大对象引用
override fun onDestroyView() {
    _binding = null
    viewModel.clearData()
    super.onDestroyView()
}

// 2. 使用WeakReference for callbacks
private val callback = WeakReference(object : Callback { })

// 3. 图片处理
Glide.with(this)
    .load(imagePath)
    .override(300, 300)  // 限制图片大小
    .into(imageView)
```

### 网络优化

```kotlin
// 1. 使用连接池
val client = OkHttpClient.Builder()
    .connectionPool(ConnectionPool(5, 5, TimeUnit.MINUTES))
    .build()

// 2. 请求超时
val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(20, TimeUnit.SECONDS)
    .writeTimeout(20, TimeUnit.SECONDS)
    .build()

// 3. 缓存
// 使用Retrofit的缓存拦截器
```

### UI优化

```kotlin
// 1. 避免创建过多对象
// ✗ 错误：在onBindViewHolder中创建对象
override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val paint = Paint() // 每次都创建
    holder.draw(paint)
}

// ✓ 正确：复用对象
private val paint = Paint()

// 2. 异步加载
// 使用协程在后台加载数据
viewModelScope.launch(Dispatchers.Default) {
    val result = expensiveOperation()
    withContext(Dispatchers.Main) {
        updateUI(result)
    }
}

// 3. 列表优化
// 使用setHasFixedSize加速RecyclerView
binding.recyclerView.apply {
    setHasFixedSize(true)
    layoutManager = LinearLayoutManager(context)
    adapter = adapter
}
```

---

## 🔐 安全建议

### API安全

```kotlin
// 1. 不要在代码中硬编码API密钥
// ✗ 错误
const val API_KEY = "sk_live_xxxxx"

// ✓ 正确：从BuildConfig或远程配置获取
val apiKey = BuildConfig.API_KEY
```

### 数据安全

```kotlin
// 1. 敏感数据加密存储
class SecureSharedPreferences {
    fun saveEncrypted(key: String, value: String) {
        val encrypted = encrypt(value)
        preferences.edit().putString(key, encrypted).apply()
    }
}

// 2. 避免在日志中输出敏感信息
// ✗ 错误
Log.d("API", "Sending request with token: $token")

// ✓ 正确
Log.d("API", "Sending request")
```

### 输入验证

```kotlin
// 1. 验证用户输入
fun validateImagePath(path: String): Boolean {
    return path.isNotEmpty() &&
           path.endsWith(".jpg") || path.endsWith(".png")
}

// 2. 验证API响应
if (response.status != "success") {
    return Result.failure(Exception(response.message))
}
```

---

## 📚 推荐阅读

- [Android Architecture Components Guide](https://developer.android.com/topic/architecture)
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-overview.html)
- [Room Database Best Practices](https://developer.android.com/training/data-storage/room/async-queries)
- [Clean Architecture in Android](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

## 🤝 代码审查清单

在提交Pull Request之前，请确认：

- [ ] 代码遵循命名规范
- [ ] 函数长度合理（<50行）
- [ ] 添加了必要的错误处理
- [ ] 没有硬编码的常量
- [ ] 没有过多的日志输出
- [ ] 没有内存泄漏风险
- [ ] 性能指标可接受
- [ ] 单元测试覆盖关键路径
- [ ] 文档已更新
- [ ] Commit消息清晰明确

---

## 📞 获取帮助

遇到问题？
1. 查看日志输出
2. 检查这个开发者指南
3. 在项目Issue中搜索
4. 创建新的Issue并附加日志

---

## 版本历史

| 版本 | 日期 | 主要内容 |
|------|------|--------|
| 1.0 | 2025-10-25 | 初始版本 |

---

**祝你开发愉快！** 🚀
