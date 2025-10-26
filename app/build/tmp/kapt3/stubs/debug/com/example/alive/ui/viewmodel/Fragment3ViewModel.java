package com.example.alive.ui.viewmodel;

/**
 * Fragment3ViewModel - 手动圈选界面的ViewModel
 *
 * 职责：
 * - 提交用户在各帧上标记的圆圈圈选数据到后端
 * - 管理圈选提交过程中的加载状态
 * - 处理提交结果和错误情况
 * - 将圈选数据转换为JSON格式供后端处理
 *
 * @param repository AliveRepository实例，用于网络请求操作
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0010\b\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0006\u0010\u000f\u001a\u00020\u0010J0\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00072\u0018\u0010\u0015\u001a\u0014\u0012\u0004\u0012\u00020\u0017\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00190\u00180\u0016R\u0019\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001f\u0010\n\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001f\u0010\r\u001a\u0010\u0012\f\u0012\n \f*\u0004\u0018\u00010\u000b0\u000b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\t\u00a8\u0006\u001a"}, d2 = {"Lcom/example/alive/ui/viewmodel/Fragment3ViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/alive/data/repository/AliveRepository;", "(Lcom/example/alive/data/repository/AliveRepository;)V", "errorMessage", "Landroidx/lifecycle/MutableLiveData;", "", "getErrorMessage", "()Landroidx/lifecycle/MutableLiveData;", "isLoading", "", "kotlin.jvm.PlatformType", "submissionSuccess", "getSubmissionSuccess", "clearError", "", "submitCircleRemoval", "taskId", "", "markedAliveImagePath", "circleSelectionsMap", "", "", "", "Lcom/example/alive/data/entity/CircleSelection;", "app_debug"})
public final class Fragment3ViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.repository.AliveRepository repository = null;
    
    /**
     * 加载状态标志
     * true表示正在提交圈选数据，false表示提交完成或空闲
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isLoading = null;
    
    /**
     * 错误消息
     * 当提交失败时存储错误信息，成功时为null
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> errorMessage = null;
    
    /**
     * 提交成功标志
     * true表示圈选数据已成功提交到后端并开始处理
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Boolean> submissionSuccess = null;
    
    public Fragment3ViewModel(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.repository.AliveRepository repository) {
        super();
    }
    
    /**
     * 加载状态标志
     * true表示正在提交圈选数据，false表示提交完成或空闲
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> isLoading() {
        return null;
    }
    
    /**
     * 错误消息
     * 当提交失败时存储错误信息，成功时为null
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getErrorMessage() {
        return null;
    }
    
    /**
     * 提交成功标志
     * true表示圈选数据已成功提交到后端并开始处理
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Boolean> getSubmissionSuccess() {
        return null;
    }
    
    /**
     * 提交圆圈圈选数据到后端进行目标去除处理
     * @param taskId 当前任务的ID
     * @param markedAliveImagePath 标记后的Alive图路径
     * @param circleSelectionsMap 圈选数据映射，Key为帧索引，Value为该帧上的圈选列表
     *
     * 操作流程：
     * 1. 设置加载状态为true并清空之前的错误消息
     * 2. 将圈选数据Map转换为JSON字符串
     * 3. 调用后端API提交圈选数据
     * 4. 检查API响应：
     *   - 成功：设置submissionSuccess为true
     *   - 失败：设置相应的错误消息
     * 5. 捕获并处理可能的网络异常
     * 6. 无论成功或失败，最后都将加载状态设为false
     */
    public final void submitCircleRemoval(long taskId, @org.jetbrains.annotations.NotNull()
    java.lang.String markedAliveImagePath, @org.jetbrains.annotations.NotNull()
    java.util.Map<java.lang.Integer, ? extends java.util.List<com.example.alive.data.entity.CircleSelection>> circleSelectionsMap) {
    }
    
    /**
     * 清除错误消息
     * 通常在用户确认看到错误提示后调用
     */
    public final void clearError() {
    }
}