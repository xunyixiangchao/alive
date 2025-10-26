package com.example.alive.data.repository;

/**
 * Repository仓库层
 * 数据层的核心，整合本地数据库(DAO)和网络API
 * 负责提供统一的数据访问接口，隐藏本地/网络数据源的具体实现
 * 所有数据操作都会通过Repository进行，ViewModel无需关心数据来源
 *
 * @property aliveImageDao AliveImage表的DAO，用于本地数据库操作
 * @property taskDao Task表的DAO，用于本地数据库操作
 * @property api Retrofit API接口，用于网络请求，默认使用ApiClient提供的实现
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000|\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010\u0016J\u0018\u0010\u0017\u001a\u0004\u0018\u00010\f2\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aJ\u0012\u0010\u001b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u001d0\u001cJ\u0012\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001d0\u001cJ\u0012\u0010\u001f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001d0\u001cJ\u0018\u0010 \u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0018\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aJ\u0016\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u0019H\u0086@\u00a2\u0006\u0002\u0010\u001aJ\u001a\u0010$\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u001d0\u001c2\u0006\u0010%\u001a\u00020&J\u0016\u0010\'\u001a\u00020\u00192\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010(\u001a\u00020\u00192\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J&\u0010)\u001a\u00020*2\u0006\u0010#\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010+\u001a\u00020\u0015H\u0086@\u00a2\u0006\u0002\u0010,J\u001e\u0010-\u001a\u00020.2\u0006\u0010#\u001a\u00020\u00192\u0006\u0010/\u001a\u000200H\u0086@\u00a2\u0006\u0002\u00101J\u0016\u00102\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J\u001e\u00103\u001a\u00020\n2\u0006\u0010\u0018\u001a\u00020\u00192\u0006\u0010/\u001a\u000200H\u0086@\u00a2\u0006\u0002\u00101R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u00064"}, d2 = {"Lcom/example/alive/data/repository/AliveRepository;", "", "aliveImageDao", "Lcom/example/alive/data/dao/AliveImageDao;", "taskDao", "Lcom/example/alive/data/dao/TaskDao;", "api", "Lcom/example/alive/network/api/AliveApi;", "(Lcom/example/alive/data/dao/AliveImageDao;Lcom/example/alive/data/dao/TaskDao;Lcom/example/alive/network/api/AliveApi;)V", "deleteAliveImage", "", "aliveImage", "Lcom/example/alive/data/entity/AliveImage;", "(Lcom/example/alive/data/entity/AliveImage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteTask", "task", "Lcom/example/alive/data/entity/Task;", "(Lcom/example/alive/data/entity/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "extract8Frames", "Lcom/example/alive/data/dto/Extract8FramesResponse;", "aliveImagePath", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAliveImageById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllAliveImages", "Lkotlinx/coroutines/flow/Flow;", "", "getAllTasks", "getFavoriteTasks", "getTaskById", "getTaskStatus", "Lcom/example/alive/data/dto/TaskStatusResponse;", "taskId", "getTasksByStatus", "status", "Lcom/example/alive/data/entity/TaskStatus;", "insertAliveImage", "insertTask", "submitCircleRemoval", "Lcom/example/alive/data/dto/CircleRemovalResponse;", "selectionsJson", "(JLjava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavorite", "Lcom/example/alive/data/dto/FavoriteResponse;", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateTask", "updateTaskFavorite", "app_debug"})
public final class AliveRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.dao.AliveImageDao aliveImageDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.dao.TaskDao taskDao = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.network.api.AliveApi api = null;
    
    public AliveRepository(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.dao.AliveImageDao aliveImageDao, @org.jetbrains.annotations.NotNull()
    com.example.alive.data.dao.TaskDao taskDao, @org.jetbrains.annotations.NotNull()
    com.example.alive.network.api.AliveApi api) {
        super();
    }
    
    /**
     * 获取所有ALIVE图像的Flow流
     * 可用于ViewModel中订阅数据更新
     *
     * @return 包含所有AliveImage的Flow<List<AliveImage>>
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.AliveImage>> getAllAliveImages() {
        return null;
    }
    
    /**
     * 插入一张ALIVE图像记录
     *
     * @param aliveImage 要插入的AliveImage对象
     * @return 插入后的行ID
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertAliveImage(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.AliveImage aliveImage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * 根据ID查询单张ALIVE图像
     *
     * @param id 图像ID
     * @return AliveImage对象或null
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getAliveImageById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.entity.AliveImage> $completion) {
        return null;
    }
    
    /**
     * 删除一张ALIVE图像记录
     *
     * @param aliveImage 要删除的AliveImage对象
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteAliveImage(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.AliveImage aliveImage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 获取所有任务的Flow流
     * 用于任务列表页面显示所有任务
     *
     * @return 包含所有Task的Flow<List<Task>>
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getAllTasks() {
        return null;
    }
    
    /**
     * 根据状态获取任务的Flow流
     * 用于筛选特定状态的任务（如进行中、已完成等）
     *
     * @param status 要筛选的任务状态
     * @return 匹配该状态的Task列表Flow
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getTasksByStatus(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status) {
        return null;
    }
    
    /**
     * 获取所有收藏任务的Flow流
     * 用于显示用户标记为喜欢的任务
     *
     * @return 包含所有收藏Task的Flow<List<Task>>
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getFavoriteTasks() {
        return null;
    }
    
    /**
     * 插入一个任务记录
     *
     * @param task 要插入的Task对象
     * @return 插入后的行ID
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object insertTask(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }
    
    /**
     * 根据ID查询单个任务
     *
     * @param id 任务ID
     * @return Task对象或null
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTaskById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.entity.Task> $completion) {
        return null;
    }
    
    /**
     * 更新一个任务记录
     *
     * @param task 包含要更新数据的Task对象
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateTask(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 更新任务的收藏状态
     * 用于用户点击收藏/取消收藏按钮
     *
     * @param id 任务ID
     * @param isFavorite 是否收藏
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateTaskFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 删除一个任务记录
     *
     * @param task 要删除的Task对象
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object deleteTask(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * 提交ALIVE图像并请求提取8帧
     * 调用后端API获取标记后的8帧图像
     *
     * @param aliveImagePath ALIVE图像的本地文件路径
     * @return Extract8FramesResponse包含提取结果和帧图像路径
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object extract8Frames(@org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.Extract8FramesResponse> $completion) {
        return null;
    }
    
    /**
     * 提交圈选消除任务到服务器
     * 用户完成圈选后，调用此方法提交任务给后端处理
     *
     * @param taskId 任务ID
     * @param aliveImagePath 标记后的ALIVE图像路径
     * @param selectionsJson 圈选信息JSON字符串（包含所有圆圈坐标和半径）
     * @return CircleRemovalResponse包含任务提交结果和任务ID
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object submitCircleRemoval(long taskId, @org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath, @org.jetbrains.annotations.NotNull()
    java.lang.String selectionsJson, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.CircleRemovalResponse> $completion) {
        return null;
    }
    
    /**
     * 查询任务处理状态
     * Fragment4会定期调用此方法轮询任务状态，获取处理进度或结果
     *
     * @param taskId 任务ID
     * @return TaskStatusResponse包含当前任务状态、进度、处理结果等
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object getTaskStatus(long taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.TaskStatusResponse> $completion) {
        return null;
    }
    
    /**
     * 更新任务的收藏状态到服务器
     * 用于同步用户的收藏选择到后端
     *
     * @param taskId 任务ID
     * @param isFavorite 是否收藏
     * @return FavoriteResponse包含操作结果
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateFavorite(long taskId, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.FavoriteResponse> $completion) {
        return null;
    }
}