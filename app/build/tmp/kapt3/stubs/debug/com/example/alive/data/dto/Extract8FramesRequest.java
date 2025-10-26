package com.example.alive.data.dto;

/**
 * 提取8帧的请求
 * 由Fragment2发送给服务器，请求从ALIVE图像中提取8个关键帧
 *
 * @property aliveImagePath ALIVE图像在本地设备的文件路径
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0087\b\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\u0003H\u00c6\u0003J\u0013\u0010\b\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u0003H\u00c6\u0001J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\f\u001a\u00020\rH\u00d6\u0001J\t\u0010\u000e\u001a\u00020\u0003H\u00d6\u0001R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u000f"}, d2 = {"Lcom/example/alive/data/dto/Extract8FramesRequest;", "", "aliveImagePath", "", "(Ljava/lang/String;)V", "getAliveImagePath", "()Ljava/lang/String;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"})
public final class Extract8FramesRequest {
    @com.google.gson.annotations.SerializedName(value = "alive_image_path")
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String aliveImagePath = null;
    
    public Extract8FramesRequest(@org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getAliveImagePath() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.data.dto.Extract8FramesRequest copy(@org.jetbrains.annotations.NotNull()
    java.lang.String aliveImagePath) {
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