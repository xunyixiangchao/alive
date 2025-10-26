package com.example.alive.data.dao;

/**
 * ALIVE图像数据访问对象(DAO)
 * 负责AliveImage表的所有数据库操作，提供增删改查接口
 * 所有方法都使用Kotlin协程，支持非阻塞异步操作
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\u00032\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00050\fH\u00a7@\u00a2\u0006\u0002\u0010\rJ\u0014\u0010\u000e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\f0\u000fH\'J\u0018\u0010\u0010\u001a\u0004\u0018\u00010\u00052\u0006\u0010\b\u001a\u00020\tH\u00a7@\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u0011\u001a\u00020\t2\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0013"}, d2 = {"Lcom/example/alive/data/dao/AliveImageDao;", "", "delete", "", "aliveImage", "Lcom/example/alive/data/entity/AliveImage;", "(Lcom/example/alive/data/entity/AliveImage;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteById", "id", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAll", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getAllFlow", "Lkotlinx/coroutines/flow/Flow;", "getById", "insert", "update", "app_debug"})
@androidx.room.Dao()
public abstract interface AliveImageDao {
    
    /**
     * 插入一张ALIVE图像记录
     * 如果主键冲突则替换旧记录
     *
     * @param aliveImage 要插入的AliveImage对象
     * @return 插入后的行ID
     */
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.AliveImage aliveImage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    /**
     * 更新一张ALIVE图像记录
     *
     * @param aliveImage 包含要更新数据的AliveImage对象
     */
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.AliveImage aliveImage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 删除一张ALIVE图像记录
     *
     * @param aliveImage 要删除的AliveImage对象
     */
    @androidx.room.Delete()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object delete(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.AliveImage aliveImage, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    /**
     * 根据ID查询单张ALIVE图像记录
     *
     * @param id 图像ID
     * @return AliveImage对象，若不存在则返回null
     */
    @androidx.room.Query(value = "SELECT * FROM alive_images WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.entity.AliveImage> $completion);
    
    /**
     * 获取所有ALIVE图像的Flow，按创建时间降序排列
     * Flow是可观察的数据流，一旦表数据更新会自动发出新数据
     *
     * @return 包含所有AliveImage的Flow<List<AliveImage>>
     */
    @androidx.room.Query(value = "SELECT * FROM alive_images ORDER BY createdTime DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.example.alive.data.entity.AliveImage>> getAllFlow();
    
    /**
     * 查询所有ALIVE图像记录，按创建时间降序排列
     * 这是非Flow版本，只查询一次
     *
     * @return AliveImage列表
     */
    @androidx.room.Query(value = "SELECT * FROM alive_images ORDER BY createdTime DESC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAll(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.example.alive.data.entity.AliveImage>> $completion);
    
    /**
     * 根据ID删除一张ALIVE图像记录
     *
     * @param id 要删除的图像ID
     */
    @androidx.room.Query(value = "DELETE FROM alive_images WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteById(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
}