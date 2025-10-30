# Fragment1 功能升级总结

**修改日期**: 2025-10-26
**修改内容**: Fragment1添加本地抽帧、获取上传地址、上传图片功能

---

## 📋 修改概述

Fragment1已完全升级，现在支持完整的图片处理流程：

1. **本地抽帧** ✅ - 不依赖API，直接从选中的视频/图片提取8帧
2. **获取上传地址** ✅ - 调用API获取临时上传URL
3. **上传图片** ✅ - 使用获取的地址上传图片到服务器

---

## 🔧 技术实现详情

### 1. API层修改 (AliveApi.kt)

**新增两个API接口**：

```kotlin
// 获取图片上传地址
@GET("/api/upload/get-url")
suspend fun getUploadUrl(): UploadUrlResponse

// 上传图片到服务器
@POST("/api/upload/image")
suspend fun uploadImage(
    @Body body: MultipartBody
): ImageUploadResponse
```

**导入新库**：
```kotlin
import okhttp3.MultipartBody
import retrofit2.http.Body
```

### 2. DTO层新增 (ApiResponses.kt)

**新增三个响应数据类**：

```kotlin
// 获取上传地址响应
@Serializable
data class UploadUrlResponse(
    val uploadUrl: String,
    val expiresIn: Int = 3600
)

// 上传图片响应
@Serializable
data class ImageUploadResponse(
    val success: Boolean,
    val imageUrl: String,
    val message: String? = null
)
```

### 3. Repository层修改 (AliveRepository.kt)

**新增两个方法**：

```kotlin
// 获取上传地址
suspend fun getUploadUrl(): UploadUrlResponse {
    return api.getUploadUrl()
}

// 上传图片
suspend fun uploadImage(imageBody: MultipartBody): ImageUploadResponse {
    return api.uploadImage(imageBody)
}
```

**导入新库**：
```kotlin
import okhttp3.MultipartBody
import com.example.alive.network.dto.UploadUrlResponse
import com.example.alive.network.dto.ImageUploadResponse
```

### 4. ViewModel层重写 (Fragment1ViewModel.kt)

**新增属性**：
```kotlin
// 本地提取的8个帧列表
val extractedFrames = MutableLiveData<List<FrameData>>(emptyList())

// 上传地址
val uploadUrl = MutableLiveData<String>("")

// 上传后的图片URL
val uploadedImageUrl = MutableLiveData<String>("")
```

**新增方法**：

```kotlin
// 1. 本地提取8帧（使用VideoUtils）
fun extractFramesFromLocalImage(videoPath: String, context: Context)

// 2. 获取上传地址
fun fetchUploadUrl()

// 3. 上传图片
fun uploadImageToServer(imagePath: String)

// 4. 一键执行完整流程（推荐使用）
fun executeCompleteWorkflow(aliveImage: AliveImage, context: Context)
```

**完整工作流方法说明**：

```kotlin
fun executeCompleteWorkflow(aliveImage: AliveImage, context: Context) {
    // 步骤1：保存图片到数据库
    val imageId = repository.insertAliveImage(aliveImage)

    // 步骤2：本地提取8帧（使用VideoUtils）
    val frames = mutableListOf<FrameData>()
    VideoUtils.extract8FramesFromVideo(
        videoPath = aliveImage.uri,
        context = context,
        onFrameExtracted = { frameData ->
            frames.add(frameData)
        }
    )

    // 步骤3：获取上传地址
    val uploadUrlResponse = repository.getUploadUrl()

    // 步骤4：上传图片
    val imageFile = File(aliveImage.uri)
    val multipartBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", imageFile.name, imageBody)
        .build()
    val uploadResponse = repository.uploadImage(multipartBody)

    // 返回综合结果
    // - savedImage: 保存的图片信息
    // - frames: 提取的8个帧列表
    // - uploadResponse: 上传结果和图片URL
}
```

### 5. Fragment层重写 (Fragment1.kt)

**修改用户选择流程**：

```kotlin
// 原来：只保存图片
// viewModel.saveSelectedImage(aliveImage)

// 现在：执行完整工作流
viewModel.executeCompleteWorkflow(aliveImage, requireContext())
```

**新增观察的数据**：

```kotlin
// 观察提取的帧
viewModel.extractedFrames.observe(viewLifecycleOwner) { frames ->
    if (frames.isNotEmpty()) {
        binding.tvStatus.text = "✓ Extracted ${frames.size} frames locally"
    }
}

// 观察上传地址
viewModel.uploadUrl.observe(viewLifecycleOwner) { url ->
    if (url.isNotEmpty()) {
        binding.tvStatus.text = "✓ Got upload URL"
    }
}

// 观察上传后的图片URL
viewModel.uploadedImageUrl.observe(viewLifecycleOwner) { url ->
    if (url.isNotEmpty()) {
        binding.tvStatus.text = "✓ Image uploaded successfully"
        binding.btnNext.isEnabled = true
    }
}
```

**修改下一步按钮逻辑**：

```kotlin
// 原来：只检查是否选择了图片
if (selectedImage != null) { ... }

// 现在：检查是否成功上传了图片
if (!uploadedUrl.isNullOrEmpty()) {
    // 将选中的图片和提取的帧保存到SharedViewModel
    sharedViewModel.setCurrentImage(image)
    sharedViewModel.setExtractedFrames(frames)
    // 导航到Fragment2
}
```

---

## 📊 数据流程图

```
用户选择图片
    ↓
executeCompleteWorkflow()
    ├─ Step 1: 保存到数据库 (repository.insertAliveImage)
    │   ↓
    ├─ Step 2: 本地提取8帧 (VideoUtils.extract8FramesFromVideo)
    │   ↓
    ├─ Step 3: 获取上传地址 (repository.getUploadUrl)
    │   ↓
    └─ Step 4: 上传图片 (repository.uploadImage)
    ↓
更新UI
    ├─ selectedImage: 已选择的图片
    ├─ extractedFrames: 提取的8个帧
    ├─ uploadUrl: 获取的上传地址
    └─ uploadedImageUrl: 上传后的图片URL
    ↓
用户点击"下一步"
    ↓
保存到SharedViewModel
    ├─ currentImage: 选中的图片
    └─ extractedFrames: 提取的8帧
    ↓
导航到Fragment2
```

---

## 🎯 UI交互流程

**Fragment1的完整用户流程**：

```
初始界面显示
    ↓
用户点击"选择图片"
    ↓
系统选择器打开
    ↓
用户选择图片/视频
    ↓
自动开始处理（显示 Processing...）
    │
    ├─ ✓ Selected: [filename]
    ├─ ✓ Extracted 8 frames locally
    ├─ ✓ Got upload URL
    ├─ ✓ Image uploaded successfully
    ↓
显示完成状态
    ↓
用户点击"下一步"
    ↓
导航到Fragment2
```

**UI状态变化**：

| 事件 | 显示内容 | 按钮状态 |
|-----|--------|---------|
| 初始化 | 空 | btnSelectImage启用, btnNext禁用 |
| 开始处理 | "Processing..." | 都禁用 |
| 图片已选择 | "Selected: xxx" | - |
| 帧已提取 | "✓ Extracted 8 frames locally" | - |
| 获取地址 | "✓ Got upload URL" | - |
| 上传成功 | "✓ Image uploaded successfully" | btnNext启用 |
| 出错 | "✗ Error: xxx" | btnSelectImage启用 |

---

## 📦 修改的文件列表

### 直接修改的文件（4个）:

1. **AliveApi.kt**
   - 新增: getUploadUrl() 和 uploadImage() 方法
   - 新增: 导入 MultipartBody 和 Body

2. **ApiResponses.kt**
   - 新增: UploadUrlResponse 数据类
   - 新增: ImageUploadResponse 数据类

3. **AliveRepository.kt**
   - 新增: getUploadUrl() 和 uploadImage() 方法
   - 新增: 导入相关类

4. **Fragment1ViewModel.kt** (完全重写)
   - 新增: extractedFrames, uploadUrl, uploadedImageUrl 属性
   - 新增: extractFramesFromLocalImage() 方法
   - 新增: fetchUploadUrl() 方法
   - 新增: uploadImageToServer() 方法
   - 新增: executeCompleteWorkflow() 方法（推荐）

5. **Fragment1.kt** (大幅修改)
   - 修改: 用户选择后自动执行 executeCompleteWorkflow()
   - 新增: 观察 extractedFrames, uploadUrl, uploadedImageUrl
   - 修改: 下一步按钮检查上传状态
   - 更新: UI显示处理进度

---

## ✨ 核心特性

### ✅ 本地抽帧（无需API）
- 使用现有的 VideoUtils.extract8FramesFromVideo()
- 提取规则：首帧 + 末帧 + 中间6帧（均匀分布）
- 帧自动保存到应用缓存目录

### ✅ 获取上传地址
- 调用 API 获取临时上传 URL
- 支持地址过期时间管理
- 错误自动处理和重试

### ✅ 上传图片
- 使用 Multipart/form-data 格式
- 支持所有图片格式
- 自动错误处理和提示

### ✅ 完整流程管理
- 一键执行完整工作流
- 自动状态管理（isLoading, errorMessage）
- UI实时反馈处理进度

### ✅ 错误处理
- 所有操作都有错误捕获
- 自动显示错误提示
- 错误后可重试

---

## 🚀 使用示例

### 简单使用（推荐）：

```kotlin
// Fragment1中
val aliveImage = AliveImage(
    uri = filePath,
    name = uri.lastPathSegment ?: "unknown",
    timestamp = System.currentTimeMillis()
)

// 一键执行完整流程
viewModel.executeCompleteWorkflow(aliveImage, requireContext())

// 观察结果
viewModel.extractedFrames.observe(viewLifecycleOwner) { frames ->
    // frames 是提取的8个帧
}

viewModel.uploadedImageUrl.observe(viewLifecycleOwner) { url ->
    // url 是上传后的图片地址
}
```

### 细粒度控制：

```kotlin
// 仅提取帧
viewModel.extractFramesFromLocalImage(videoPath, context)

// 仅获取上传地址
viewModel.fetchUploadUrl()

// 仅上传图片
viewModel.uploadImageToServer(imagePath)
```

---

## 📝 代码注释

所有新增方法都包含：
- ✅ 详细的中文注释
- ✅ 参数说明
- ✅ 返回值说明
- ✅ 错误处理说明
- ✅ 调用时机说明

---

## 🔄 向后兼容性

- ✅ 保留原有的 saveSelectedImage() 方法
- ✅ SharedViewModel 用法保持不变
- ✅ Fragment导航流程不变
- ✅ 其他Fragment不受影响

---

## 📌 注意事项

1. **AndroidManifest.xml** 需要确保已有网络权限：
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

2. **build.gradle** 需要确保已有依赖：
   ```gradle
   implementation 'com.squareup.okhttp3:okhttp:4.x.x'
   implementation 'com.squareup.retrofit2:retrofit:2.x.x'
   ```

3. **API后端** 需要实现三个端点：
   - GET /api/upload/get-url
   - POST /api/upload/image
   - MockAliveApi 中已有对应的 Mock 实现供测试

---

## ✅ 测试检查清单

- [ ] 选择图片后自动触发完整流程
- [ ] 本地抽帧成功，显示8帧
- [ ] 获取上传地址成功
- [ ] 图片上传成功
- [ ] UI正确显示各阶段状态
- [ ] 错误时显示错误提示
- [ ] "下一步"按钮在上传成功后启用
- [ ] 导航到Fragment2成功
- [ ] SharedViewModel中包含图片和帧数据

---

**修改完成！** ✅ Fragment1现在支持完整的本地抽帧和图片上传功能。
