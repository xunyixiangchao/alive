package com.example.alive.data.entity;

/**
 * 任务实体
 * 用于存储ALIVE图像处理任务的完整信息
 *
 * @property id 主键，自动递增
 * @property aliveImageId 关联的AliveImage主键，用于表示此任务处理的是哪张图像
 * @property markingImagePath 用户标记后的alive图路径（用户在Fragment3中圈选后保存）
 * @property circleSelectionsJson 圈选信息的JSON字符串，存储所有圆圈坐标和半径
 * @property resultImagePath 处理完成后的alive图路径（服务器返回）
 * @property status 任务当前状态
 * @property createdTime 任务创建时间戳（毫秒）
 * @property completedTime 任务完成时间戳（毫秒），仅当status=COMPLETED时有效
 * @property isFavorite 是否为收藏任务（用户可以标记喜欢的结果）
 * @property errorMessage 错误消息，仅当status=FAILED时包含错误信息
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u001f\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001Bq\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0006\u0012\b\b\u0002\u0010\t\u001a\u00020\n\u0012\b\b\u0002\u0010\u000b\u001a\u00020\u0003\u0012\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\r\u001a\u00020\u000e\u0012\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\u0002\u0010\u0010J\t\u0010 \u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010!\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010\"\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010#\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010$\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\u000b\u0010%\u001a\u0004\u0018\u00010\u0006H\u00c6\u0003J\t\u0010&\u001a\u00020\nH\u00c6\u0003J\t\u0010\'\u001a\u00020\u0003H\u00c6\u0003J\u0010\u0010(\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010\u0016J\t\u0010)\u001a\u00020\u000eH\u00c6\u0003J|\u0010*\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\u00062\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00062\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\u00032\n\b\u0002\u0010\f\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\r\u001a\u00020\u000e2\n\b\u0002\u0010\u000f\u001a\u0004\u0018\u00010\u0006H\u00c6\u0001\u00a2\u0006\u0002\u0010+J\u0013\u0010,\u001a\u00020\u000e2\b\u0010-\u001a\u0004\u0018\u00010.H\u00d6\u0003J\t\u0010/\u001a\u000200H\u00d6\u0001J\t\u00101\u001a\u00020\u0006H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0013\u0010\u0007\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0015\u0010\f\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\n\n\u0002\u0010\u0017\u001a\u0004\b\u0015\u0010\u0016R\u0011\u0010\u000b\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0012R\u0013\u0010\u000f\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0014R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0012R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u001bR\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0014R\u0013\u0010\b\u001a\u0004\u0018\u00010\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0014R\u0011\u0010\t\u001a\u00020\n\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001f\u00a8\u00062"}, d2 = {"Lcom/example/alive/data/entity/Task;", "Ljava/io/Serializable;", "id", "", "aliveImageId", "markingImagePath", "", "circleSelectionsJson", "resultImagePath", "status", "Lcom/example/alive/data/entity/TaskStatus;", "createdTime", "completedTime", "isFavorite", "", "errorMessage", "(JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/example/alive/data/entity/TaskStatus;JLjava/lang/Long;ZLjava/lang/String;)V", "getAliveImageId", "()J", "getCircleSelectionsJson", "()Ljava/lang/String;", "getCompletedTime", "()Ljava/lang/Long;", "Ljava/lang/Long;", "getCreatedTime", "getErrorMessage", "getId", "()Z", "getMarkingImagePath", "getResultImagePath", "getStatus", "()Lcom/example/alive/data/entity/TaskStatus;", "component1", "component10", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(JJLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/example/alive/data/entity/TaskStatus;JLjava/lang/Long;ZLjava/lang/String;)Lcom/example/alive/data/entity/Task;", "equals", "other", "", "hashCode", "", "toString", "app_debug"})
@androidx.room.Entity(tableName = "tasks")
public final class Task implements java.io.Serializable {
    @androidx.room.PrimaryKey(autoGenerate = true)
    private final long id = 0L;
    private final long aliveImageId = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String markingImagePath = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String circleSelectionsJson = null;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String resultImagePath = null;
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.entity.TaskStatus status = null;
    private final long createdTime = 0L;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.Long completedTime = null;
    private final boolean isFavorite = false;
    @org.jetbrains.annotations.Nullable()
    private final java.lang.String errorMessage = null;
    
    public Task(long id, long aliveImageId, @org.jetbrains.annotations.Nullable()
    java.lang.String markingImagePath, @org.jetbrains.annotations.Nullable()
    java.lang.String circleSelectionsJson, @org.jetbrains.annotations.Nullable()
    java.lang.String resultImagePath, @org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status, long createdTime, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedTime, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        super();
    }
    
    public final long getId() {
        return 0L;
    }
    
    public final long getAliveImageId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getMarkingImagePath() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getCircleSelectionsJson() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getResultImagePath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.entity.TaskStatus getStatus() {
        return null;
    }
    
    public final long getCreatedTime() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long getCompletedTime() {
        return null;
    }
    
    public final boolean isFavorite() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getErrorMessage() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component10() {
        return null;
    }
    
    public final long component2() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component5() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.entity.TaskStatus component6() {
        return null;
    }
    
    public final long component7() {
        return 0L;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Long component8() {
        return null;
    }
    
    public final boolean component9() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.entity.Task copy(long id, long aliveImageId, @org.jetbrains.annotations.Nullable()
    java.lang.String markingImagePath, @org.jetbrains.annotations.Nullable()
    java.lang.String circleSelectionsJson, @org.jetbrains.annotations.Nullable()
    java.lang.String resultImagePath, @org.jetbrains.annotations.NotNull()
    com.example.alive.data.entity.TaskStatus status, long createdTime, @org.jetbrains.annotations.Nullable()
    java.lang.Long completedTime, boolean isFavorite, @org.jetbrains.annotations.Nullable()
    java.lang.String errorMessage) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}