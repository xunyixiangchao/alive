package com.example.alive.network.api;

/**
 * Mock API实现
 * 用于开发和测试，模拟真实的后端API服务
 * 所有方法都会模拟网络延迟，并返回逼真的模拟数据
 *
 * 开发时使用此实现可以：
 * 1. 在没有后端服务的情况下开发前端UI
 * 2. 测试各种场景（成功、失败、超时等）
 * 3. 快速迭代前端功能而无需等待后端API
 *
 * 生产环境应切换到真实的Retrofit实现
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0096@\u00a2\u0006\u0002\u0010\u0007J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0096@\u00a2\u0006\u0002\u0010\fJ\u0016\u0010\r\u001a\u00020\u000e2\u0006\u0010\u0005\u001a\u00020\u000fH\u0096@\u00a2\u0006\u0002\u0010\u0010J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0005\u001a\u00020\u0013H\u0096@\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2 = {"Lcom/example/alive/network/api/MockAliveApi;", "Lcom/example/alive/network/api/AliveApi;", "()V", "extract8Frames", "Lcom/example/alive/data/dto/Extract8FramesResponse;", "request", "Lcom/example/alive/data/dto/Extract8FramesRequest;", "(Lcom/example/alive/data/dto/Extract8FramesRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTaskStatus", "Lcom/example/alive/data/dto/TaskStatusResponse;", "taskId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "submitCircleRemoval", "Lcom/example/alive/data/dto/CircleRemovalResponse;", "Lcom/example/alive/data/dto/CircleRemovalRequest;", "(Lcom/example/alive/data/dto/CircleRemovalRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavorite", "Lcom/example/alive/data/dto/FavoriteResponse;", "Lcom/example/alive/data/dto/FavoriteRequest;", "(Lcom/example/alive/data/dto/FavoriteRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class MockAliveApi implements com.example.alive.network.api.AliveApi {
    
    public MockAliveApi() {
        super();
    }
    
    /**
     * 模拟提取8帧API
     * 返回8个帧图像路径和标记后的alive图
     *
     * @param request 包含ALIVE图像文件路径的请求
     * @return 包含成功状态、8个帧路径和标记图路径的响应
     */
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object extract8Frames(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.Extract8FramesRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.Extract8FramesResponse> $completion) {
        return null;
    }
    
    /**
     * 模拟圈选消除任务提交API
     * 收到圈选信息后，返回任务ID和成功状态
     * 实际服务器会将圈选信息存储并队列化处理
     *
     * @param request 包含任务ID、marked图像路径、圈选JSON信息的请求
     * @return 包含成功状态、任务ID、消息的响应
     */
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object submitCircleRemoval(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.CircleRemovalRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.CircleRemovalResponse> $completion) {
        return null;
    }
    
    /**
     * 模拟查询任务状态API
     * 每次调用都随机返回不同的处理状态
     * 实际应用中会返回真实的任务处理进度
     *
     * @param taskId 要查询的任务ID
     * @return 包含任务ID、当前状态、结果路径或错误信息的响应
     */
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object getTaskStatus(long taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.TaskStatusResponse> $completion) {
        return null;
    }
    
    /**
     * 模拟更新收藏状态API
     * 同步用户的收藏操作到服务器
     *
     * @param request 包含任务ID和收藏状态的请求
     * @return 包含操作结果、收藏状态（1为收藏，0为未收藏）、消息的响应
     */
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object updateFavorite(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.FavoriteRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.FavoriteResponse> $completion) {
        return null;
    }
}