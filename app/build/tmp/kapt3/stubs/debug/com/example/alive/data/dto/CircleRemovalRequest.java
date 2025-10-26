package com.example.alive.data.dto;

/**
 * 圈选消除的请求
 * 由Fragment3发送给服务器，提交用户的圈选信息和marked alive图，请求服务器进行人物消除处理
 *
 * @property taskId 与该请求相关联的任务ID
 * @property aliveImagePath 标记后的alive图的文件路径
 * @property selectionsJson 圈选信息的JSON字符串，包含所有圆圈的坐标和半径
 *                         格式示例：[{frameIndex:0, centerX:100, centerY:100, radius:50}, ...]
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\t\u0010\r\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000e\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u000f\u001a\u00020\u0005H\u00c6\u0003J\'\u0010\u0010\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0014\u001a\u00020\u0015H\u00d6\u0001J\t\u0010\u0016\u001a\u00020\u0005H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\tR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0017"}, d2 = {"Lcom/example/alive/data/dto/CircleRemovalRequest;", "", "taskId", "", "aliveImagePath", "", "selectionsJson", "(JLjava/lang/String;Ljava/lang/String;)V", "getAliveImagePath", "()Ljava/lang/String;", "getSelectionsJson", "getTaskId", "()J", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class CircleRemovalRequest {
    @com.google.gson.annotations.SerializedName(value = "task_id")
    private final long taskId = 0L;
    @com.google.gson.annotations.SerializedName(value = "alive_image_path")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String aliveImagePath = null;
    @com.google.gson.annotations.SerializedName(value = "selections_json")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String selectionsJson = null;
    
    public CircleRemovalRequest(long taskId, @org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath, @org.jetbrains.annotations.NotNull()
    java.lang.String selectionsJson) {
        super();
    }
    
    public final long getTaskId() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAliveImagePath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getSelectionsJson() {
        return null;
    }
    
    public final long component1() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.dto.CircleRemovalRequest copy(long taskId, @org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath, @org.jetbrains.annotations.NotNull()
    java.lang.String selectionsJson) {
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