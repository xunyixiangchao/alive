package com.example.alive.ui.viewmodel;

/**
 * Fragment2ViewModel - 8帧提取界面的ViewModel
 *
 * 职责：
 * - 在本地从ALIVE图像或视频中提取8帧关键帧（不调用API）
 * - 管理帧提取过程中的加载状态
 * - 处理提取结果并转换为FrameData列表
 * - 处理提取过程中的错误情况
 *
 * 实现方式：
 * - 使用VideoUtils工具类进行本地帧提取
 * - 提取的帧文件保存到应用缓存目录
 * - 使用协程在后台线程执行提取操作，避免阻塞UI
 *
 * @param repository AliveRepository实例（本实现中主要用于后续数据库操作）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u0011\u001a\u00020\u0012J\u0016\u0010\u0013\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0015\u001a\u00020\u0016R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001f\u0010\n\u001a\u0010\u0012\f\u0012\n\u0012\u0004\u0012\u00020\f\u0018\u00010\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\tR\u001f\u0010\u000e\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u000f0\u000f0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/example/alive/ui/viewmodel/Fragment2ViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/alive/data/repository/AliveRepository;", "(Lcom/example/alive/data/repository/AliveRepository;)V", "errorMessage", "Landroidx/lifecycle/MutableLiveData;", "", "getErrorMessage", "()Landroidx/lifecycle/MutableLiveData;", "framesExtracted", "", "Lcom/example/alive/data/entity/FrameData;", "getFramesExtracted", "isLoading", "", "kotlin.jvm.PlatformType", "clearError", "", "extract8Frames", "filePath", "context", "Landroid/content/Context;", "app_debug"})
public final class Fragment2ViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.repository.AliveRepository repository = null;
    
    /**
     * 加载状态标志
     * true表示正在提取帧，false表示提取完成或空闲
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isLoading = null;
    
    /**
     * 错误消息
     * 当帧提取失败时存储错误信息，成功时为null
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> errorMessage = null;
    
    /**
     * 提取的帧数据列表
     * 成功提取后包含8个FrameData对象，每个对象包含帧索引和图片路径
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.alive.data.entity.FrameData>> framesExtracted = null;
    
    public Fragment2ViewModel(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.repository.AliveRepository repository) {
        super();
    }
    
    /**
     * 加载状态标志
     * true表示正在提取帧，false表示提取完成或空闲
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isLoading() {
        return null;
    }
    
    /**
     * 错误消息
     * 当帧提取失败时存储错误信息，成功时为null
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getErrorMessage() {
        return null;
    }
    
    /**
     * 提取的帧数据列表
     * 成功提取后包含8个FrameData对象，每个对象包含帧索引和图片路径
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.example.alive.data.entity.FrameData>> getFramesExtracted() {
        return null;
    }
    
    /**
     * 从ALIVE图像或视频中在本地提取8帧关键帧
     *
     * 实现细节：
     * - 如果是视频文件，使用MediaMetadataRetriever提取首帧、末帧、中间6帧（均匀分布）
     * - 如果是图像文件，视为单帧数据
     * - 提取的帧临时保存到应用缓存目录（应用卸载时自动清理）
     * - 整个提取过程在协程中运行，不阻塞UI线程
     *
     * @param filePath ALIVE图像或视频的本地文件路径
     * @param context Android上下文，用于获取缓存目录
     */
    public final void extract8Frames(@org.jetbrains.annotations.NotNull()
    java.lang.String filePath, @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
    }
    
    /**
     * 清除错误消息
     * 通常在用户确认看到错误提示后调用
     */
    public final void clearError() {
    }
}