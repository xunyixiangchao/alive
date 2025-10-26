package com.example.alive.network.api;

/**
 * Retrofit网络API接口
 * 定义与后端ALIVE服务通信的所有HTTP端点
 * 所有方法都是suspend函数，支持Kotlin协程
 *
 * 实现类包括：
 * - MockAliveApi：用于开发/测试，模拟API响应，延迟返回
 * - RealAliveApi：生产环境实际的Retrofit HTTP请求
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\b\b\u0001\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\u0007\u001a\u00020\b2\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00a2\u0006\u0002\u0010\u000bJ\u0018\u0010\f\u001a\u00020\r2\b\b\u0001\u0010\u0004\u001a\u00020\u000eH\u00a7@\u00a2\u0006\u0002\u0010\u000fJ\u0018\u0010\u0010\u001a\u00020\u00112\b\b\u0001\u0010\u0004\u001a\u00020\u0012H\u00a7@\u00a2\u0006\u0002\u0010\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/example/alive/network/api/AliveApi;", "", "extract8Frames", "Lcom/example/alive/data/dto/Extract8FramesResponse;", "request", "Lcom/example/alive/data/dto/Extract8FramesRequest;", "(Lcom/example/alive/data/dto/Extract8FramesRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getTaskStatus", "Lcom/example/alive/data/dto/TaskStatusResponse;", "taskId", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "submitCircleRemoval", "Lcom/example/alive/data/dto/CircleRemovalResponse;", "Lcom/example/alive/data/dto/CircleRemovalRequest;", "(Lcom/example/alive/data/dto/CircleRemovalRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateFavorite", "Lcom/example/alive/data/dto/FavoriteResponse;", "Lcom/example/alive/data/dto/FavoriteRequest;", "(Lcom/example/alive/data/dto/FavoriteRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public abstract interface AliveApi {
    
    /**
     * 提取8帧并标记
     * 上传ALIVE图像到服务器，服务器提取8帧关键帧并进行初步标记
     * 这是任务流程的第一步
     *
     * @param request 包含ALIVE图像文件路径的请求
     * @return Extract8FramesResponse包含提取的8帧图像路径和任务ID
     */
    @retrofit2.http.POST(value = "/api/extract8frames")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object extract8Frames(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.Extract8FramesRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.Extract8FramesResponse> $completion);
    
    /**
     * 提交圈选消除任务
     * 用户完成在8帧上的圈选后，提交给服务器进行人物消除处理
     * 这是任务流程的第二步
     *
     * @param request 包含任务ID、marked图像路径、圈选信息JSON的请求
     * @return CircleRemovalResponse包含提交结果和处理进度
     */
    @retrofit2.http.POST(value = "/api/circleremoval")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object submitCircleRemoval(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.CircleRemovalRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.CircleRemovalResponse> $completion);
    
    /**
     * 查询任务状态
     * 定期调用获取任务的处理状态，直到完成或失败
     * Fragment4会每2秒调用一次此方法进行轮询
     *
     * @param taskId 要查询的任务ID
     * @return TaskStatusResponse包含当前任务状态、进度、错误信息等
     */
    @retrofit2.http.GET(value = "/api/taskstatus")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getTaskStatus(@retrofit2.http.Query(value = "task_id")
    long taskId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.TaskStatusResponse> $completion);
    
    /**
     * 更新收藏状态
     * 同步用户的收藏/取消收藏操作到服务器
     * 用户在Fragment5点击收藏按钮时调用
     *
     * @param request 包含任务ID和收藏状态的请求
     * @return FavoriteResponse包含操作结果
     */
    @retrofit2.http.POST(value = "/api/favorite")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateFavorite(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.alive.data.dto.FavoriteRequest request, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.example.alive.data.dto.FavoriteResponse> $completion);
}