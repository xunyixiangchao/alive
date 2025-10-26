package com.example.alive.ui.viewmodel;

/**
 * SharedViewModel - 在所有Fragment之间共享数据的ViewModel
 *
 * 职责：
 * - 管理用户选择的Alive图像及其相关数据
 * - 维护当前任务的状态和信息
 * - 管理8帧提取后的帧数据
 * - 跟踪用户在帧上的圈选操作
 * - 存储标记图和处理结果图的路径
 * - 提供任务的保存和更新功能
 *
 * @param repository AliveRepository实例，用于数据持久化操作
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0014\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\b2\u0006\u0010\"\u001a\u00020\nJ\u0006\u0010#\u001a\u00020 J\u0014\u0010$\u001a\b\u0012\u0004\u0012\u00020\n0\u00172\u0006\u0010!\u001a\u00020\bJ\u0016\u0010%\u001a\u00020 2\u0006\u0010!\u001a\u00020\b2\u0006\u0010&\u001a\u00020\bJ\u000e\u0010\'\u001a\u00020 2\u0006\u0010(\u001a\u00020\u0014J\u0010\u0010)\u001a\u00020 2\b\u0010*\u001a\u0004\u0018\u00010\u000eJ\u000e\u0010+\u001a\u00020 2\u0006\u0010,\u001a\u00020\bJ\u0014\u0010-\u001a\u00020 2\f\u0010.\u001a\b\u0012\u0004\u0012\u00020\u00180\u0017J\u0010\u0010/\u001a\u00020 2\b\u00100\u001a\u0004\u0018\u00010\u001bJ\u0010\u00101\u001a\u00020 2\b\u00100\u001a\u0004\u0018\u00010\u001bJ\u0010\u00102\u001a\u00020 2\b\u0010(\u001a\u0004\u0018\u00010\u0014J\u000e\u00103\u001a\u00020 2\u0006\u0010(\u001a\u00020\u0014R)\u0010\u0005\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\b\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u00070\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000e0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\fR\u001f\u0010\u0010\u001a\u0010\u0012\f\u0012\n \u0011*\u0004\u0018\u00010\b0\b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\fR\u0019\u0010\u0013\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00140\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\fR\u001d\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00180\u00170\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\fR\u0019\u0010\u001a\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\fR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u001d\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\f\u00a8\u00064"}, d2 = {"Lcom/example/alive/ui/viewmodel/SharedViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/example/alive/data/repository/AliveRepository;", "(Lcom/example/alive/data/repository/AliveRepository;)V", "circleSelections", "Landroidx/lifecycle/MutableLiveData;", "", "", "", "Lcom/example/alive/data/entity/CircleSelection;", "getCircleSelections", "()Landroidx/lifecycle/MutableLiveData;", "currentAliveImage", "Lcom/example/alive/data/entity/AliveImage;", "getCurrentAliveImage", "currentFrameIndex", "kotlin.jvm.PlatformType", "getCurrentFrameIndex", "currentTask", "Lcom/example/alive/data/entity/Task;", "getCurrentTask", "framesData", "", "Lcom/example/alive/data/entity/FrameData;", "getFramesData", "markedAliveImagePath", "", "getMarkedAliveImagePath", "resultAliveImagePath", "getResultAliveImagePath", "addCircleSelection", "", "frameIndex", "selection", "clearCircleSelections", "getCircleSelectionsForFrame", "removeCircleSelection", "selectionIndex", "saveTask", "task", "setAliveImage", "image", "setCurrentFrameIndex", "index", "setFramesData", "frames", "setMarkedAliveImagePath", "path", "setResultAliveImagePath", "setTask", "updateTask", "app_debug"})
public final class SharedViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.repository.AliveRepository repository = null;
    
    /**
     * 当前选择的AliveImage对象
     * 存储用户从图库或相机选择的Alive图像的完整信息
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.alive.data.entity.AliveImage> currentAliveImage = null;
    
    /**
     * 当前正在处理的任务对象
     * 包含任务ID、状态、时间戳等完整任务信息
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.example.alive.data.entity.Task> currentTask = null;
    
    /**
     * 从Alive图中提取的8帧数据列表
     * 每个FrameData包含帧索引和对应的图片路径
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.example.alive.data.entity.FrameData>> framesData = null;
    
    /**
     * 当前正在显示的帧索引（0-7）
     * 用于在圈选界面中标识当前操作的是哪一帧
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.Integer> currentFrameIndex = null;
    
    /**
     * 圆圈圈选数据映射
     * Key: 帧索引（0-7）
     * Value: 该帧上的所有圈选对象列表
     * 用于存储用户在每一帧上标记的需要去除的目标区域
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.Map<java.lang.Integer, java.util.List<com.example.alive.data.entity.CircleSelection>>> circleSelections = null;
    
    /**
     * 标记后的Alive图路径
     * 存储用户完成圈选后生成的带标记的Alive图的文件路径
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> markedAliveImagePath = null;
    
    /**
     * 处理后的Alive图路径
     * 存储服务端完成目标去除处理后的最终结果图的文件路径
     */
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.lang.String> resultAliveImagePath = null;
    
    public SharedViewModel(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.repository.AliveRepository repository) {
        super();
    }
    
    /**
     * 当前选择的AliveImage对象
     * 存储用户从图库或相机选择的Alive图像的完整信息
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.example.alive.data.entity.AliveImage> getCurrentAliveImage() {
        return null;
    }
    
    /**
     * 当前正在处理的任务对象
     * 包含任务ID、状态、时间戳等完整任务信息
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.example.alive.data.entity.Task> getCurrentTask() {
        return null;
    }
    
    /**
     * 从Alive图中提取的8帧数据列表
     * 每个FrameData包含帧索引和对应的图片路径
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.example.alive.data.entity.FrameData>> getFramesData() {
        return null;
    }
    
    /**
     * 当前正在显示的帧索引（0-7）
     * 用于在圈选界面中标识当前操作的是哪一帧
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.Integer> getCurrentFrameIndex() {
        return null;
    }
    
    /**
     * 圆圈圈选数据映射
     * Key: 帧索引（0-7）
     * Value: 该帧上的所有圈选对象列表
     * 用于存储用户在每一帧上标记的需要去除的目标区域
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.Map<java.lang.Integer, java.util.List<com.example.alive.data.entity.CircleSelection>>> getCircleSelections() {
        return null;
    }
    
    /**
     * 标记后的Alive图路径
     * 存储用户完成圈选后生成的带标记的Alive图的文件路径
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getMarkedAliveImagePath() {
        return null;
    }
    
    /**
     * 处理后的Alive图路径
     * 存储服务端完成目标去除处理后的最终结果图的文件路径
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.lang.String> getResultAliveImagePath() {
        return null;
    }
    
    /**
     * 设置当前选择的AliveImage对象
     * @param image AliveImage对象，可为null表示清空当前选择
     */
    public final void setAliveImage(@org.jetbrains.annotations.Nullable()
    com.example.alive.data.entity.AliveImage image) {
    }
    
    /**
     * 设置当前任务对象
     * @param task Task对象，可为null表示清空当前任务
     */
    public final void setTask(@org.jetbrains.annotations.Nullable()
    com.example.alive.data.entity.Task task) {
    }
    
    /**
     * 设置提取的8帧数据列表
     * @param frames 帧数据列表，通常包含8个元素
     */
    public final void setFramesData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.example.alive.data.entity.FrameData> frames) {
    }
    
    /**
     * 设置当前显示的帧索引
     * @param index 帧索引，范围应为0-7
     */
    public final void setCurrentFrameIndex(int index) {
    }
    
    /**
     * 在指定帧上添加一个圆圈圈选
     * @param frameIndex 帧索引，指定要添加圈选的帧
     * @param selection CircleSelection对象，包含圆圈的位置和大小信息
     */
    public final void addCircleSelection(int frameIndex, @org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.CircleSelection selection) {
    }
    
    /**
     * 删除指定帧上的某个圆圈圈选
     * @param frameIndex 帧索引
     * @param selectionIndex 要删除的圈选在列表中的索引
     */
    public final void removeCircleSelection(int frameIndex, int selectionIndex) {
    }
    
    /**
     * 获取指定帧上的所有圆圈圈选
     * @param frameIndex 帧索引
     * @return 该帧上的圈选列表，如果该帧没有圈选则返回空列表
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.example.alive.data.entity.CircleSelection> getCircleSelectionsForFrame(int frameIndex) {
        return null;
    }
    
    /**
     * 清空所有帧上的圆圈圈选数据
     * 通常在开始新任务时调用
     */
    public final void clearCircleSelections() {
    }
    
    /**
     * 设置标记后的Alive图路径
     * @param path 标记图文件的完整路径，null表示清空
     */
    public final void setMarkedAliveImagePath(@org.jetbrains.annotations.Nullable()
    java.lang.String path) {
    }
    
    /**
     * 设置处理后的Alive图路径
     * @param path 结果图文件的完整路径，null表示清空
     */
    public final void setResultAliveImagePath(@org.jetbrains.annotations.Nullable()
    java.lang.String path) {
    }
    
    /**
     * 保存新任务到数据库
     * @param task 要保存的Task对象
     *
     * 操作流程：
     * 1. 在协程中执行异步插入操作
     * 2. 获取数据库生成的任务ID
     * 3. 更新currentTask为带ID的完整任务对象
     */
    public final void saveTask(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task) {
    }
    
    /**
     * 更新已存在的任务
     * @param task 要更新的Task对象，必须包含有效的ID
     *
     * 操作流程：
     * 1. 在协程中执行异步更新操作
     * 2. 同步更新currentTask的值
     */
    public final void updateTask(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task) {
    }
}