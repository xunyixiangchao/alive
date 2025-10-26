package com.example.alive.data.dao;

/**
 * 任务数据访问对象(DAO)
 * 负责Task表的所有数据库操作，提供增删改查接口
 * 所有方法都使用Kotlin协程，支持非阻塞异步操作
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000fH\'J\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u001c\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00050\f2\u0006\u0010\u0012\u001a\u00020\u0013H\u00a7@\u00a2\u0006\u0002\u0010\u0014J\u001c\u0010\u0015\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000f2\u0006\u0010\u0012\u001a\u00020\u0013H\'J\u0014\u0010\u0016\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000fH\'J\u0016\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0018\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0019\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u001a\u001a\u00020\u001bH\u00a7@\u00a2\u0006\u0002\u0010\u001cJ2\u0010\u001d\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u001e\u001a\u0004\u0018\u00010\t2\b\u0010\u001f\u001a\u0004\u0018\u00010 H\u00a7@\u00a2\u0006\u0002\u0010!\u00a8\u0006\""}, d2 = {"Lcom/example/alive/data/dao/TaskDao;", "", "delete", "", "task", "Lcom/example/alive/data/entity/Task;", "(Lcom/example/alive/data/entity/Task;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getById", "getByStatus", "status", "Lcom/example/alive/data/entity/TaskStatus;", "(Lcom/example/alive/data/entity/TaskStatus;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getByStatusFlow", "getFavoritesFlow", "insert", "update", "updateFavorite", "isFavorite", "", "(JZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateStatus", "completedTime", "resultImagePath", "", "(JLcom/example/alive/data/entity/TaskStatus;Ljava/lang/Long;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface TaskDao {
    
    /**
     * 插入一个任务记录
     * 如果主键冲突则替换旧记录
     *
     * @param task 要插入的Task对象
     * @return 插入后的行ID
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * 更新一个任务记录
     *
     * @param task 包含要更新数据的Task对象
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 删除一个任务记录
     *
     * @param task 要删除的Task对象
     */
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.Task task, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 根据ID查询单个任务记录
     *
     * @param id 任务ID
     * @return Task对象，若不存在则返回null
     */
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.entity.Task> $completion);
    
    /**
     * 获取所有任务的Flow，按创建时间降序排列
     * Flow是可观察的数据流，一旦表数据更新会自动发出新数据
     *
     * @return 包含所有Task的Flow<List<Task>>
     */
    @androidx.room.Query(value = "SELECT * FROM tasks ORDER BY createdTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getAllFlow();
    
    /**
     * 查询所有任务记录，按创建时间降序排列
     * 这是非Flow版本，只查询一次
     *
     * @return Task列表
     */
    @androidx.room.Query(value = "SELECT * FROM tasks ORDER BY createdTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.alive.data.entity.Task>> $completion);
    
    /**
     * 根据任务状态获取任务Flow
     * 用于按状态筛选任务（如获取所有进行中的任务）
     *
     * @param status 要查询的任务状态
     * @return 匹配该状态的Task列表Flow
     */
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE status = :status ORDER BY createdTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getByStatusFlow(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status);
    
    /**
     * 根据任务状态查询任务列表（非Flow版本）
     *
     * @param status 要查询的任务状态
     * @return 匹配该状态的Task列表
     */
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE status = :status ORDER BY createdTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getByStatus(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.alive.data.entity.Task>> $completion);
    
    /**
     * 获取所有被收藏的任务的Flow
     * 用于任务列表页面展示用户的喜欢任务
     *
     * @return 包含所有收藏任务的Flow<List<Task>>
     */
    @androidx.room.Query(value = "SELECT * FROM tasks WHERE isFavorite = 1 ORDER BY createdTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.Task>> getFavoritesFlow();
    
    /**
     * 更新任务状态及相关信息
     * 用于任务完成或失败时更新数据库
     *
     * @param id 任务ID
     * @param status 新的任务状态
     * @param completedTime 完成时间戳（毫秒），完成时设置，其他时设为null
     * @param resultImagePath 处理结果的图像路径，成功时设置
     */
    @androidx.room.Query(value = "UPDATE tasks SET status = :status, completedTime = :completedTime, resultImagePath = :resultImagePath WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateStatus(long id, @org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedTime, @org.jetbrains.annotations.Nullable()
    java.lang.String resultImagePath, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 更新任务的收藏状态
     * 用于用户点击收藏/取消收藏按钮
     *
     * @param id 任务ID
     * @param isFavorite 是否收藏（true=收藏，false=取消收藏）
     */
    @androidx.room.Query(value = "UPDATE tasks SET isFavorite = :isFavorite WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavorite(long id, boolean isFavorite, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 根据ID删除一个任务记录
     *
     * @param id 要删除的任务ID
     */
    @androidx.room.Query(value = "DELETE FROM tasks WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}