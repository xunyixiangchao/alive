package com.example.alive.data.dto;

/**
 * 收藏状态请求
 * Fragment5用户点击收藏按钮时发送，同步收藏状态到服务器
 *
 * @property taskId 要收藏或取消收藏的任务ID
 * @property isFavorite 是否收藏（true=收藏，false=取消收藏）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0087\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\n\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u000b\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\f\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\r\u001a\u00020\u00052\b\u0010\u000e\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0004\u0010\u0007R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\t\u00a8\u0006\u0013"}, d2 = {"Lcom/example/alive/data/dto/FavoriteRequest;", "", "taskId", "", "isFavorite", "", "(JZ)V", "()Z", "getTaskId", "()J", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "app_debug"})
public final class FavoriteRequest {
    @com.google.gson.annotations.SerializedName(value = "task_id")
    private final long taskId = 0L;
    @com.google.gson.annotations.SerializedName(value = "is_favorite")
    private final boolean isFavorite = false;
    
    public FavoriteRequest(long taskId, boolean isFavorite) {
        super();
    }
    
    public final long getTaskId() {
        return 0L;
    }
    
    public final boolean isFavorite() {
        return false;
    }
    
    public final long component1() {
        return 0L;
    }
    
    public final boolean component2() {
        return false;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.dto.FavoriteRequest copy(long taskId, boolean isFavorite) {
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