package com.example.alive.network;

/**
 * API客户端工厂
 * 单例模式的API提供者，负责创建和管理AliveApi实例
 * 支持在Mock实现和真实Retrofit实现之间切换
 *
 * 使用方式：
 * val api = ApiClient.getApi()
 * 然后直接调用api的方法进行网络请求
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u000e\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0005\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\t\u0010\n\u001a\u0004\b\u0007\u0010\bR\u001b\u0010\u000b\u001a\u00020\u00068BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\r\u0010\n\u001a\u0004\b\f\u0010\b\u00a8\u0006\u000f"}, d2 = {"Lcom/example/alive/network/ApiClient;", "", "()V", "BASE_URL", "", "mockApi", "Lcom/example/alive/network/api/AliveApi;", "getMockApi", "()Lcom/example/alive/network/api/AliveApi;", "mockApi$delegate", "Lkotlin/Lazy;", "retrofitApi", "getRetrofitApi", "retrofitApi$delegate", "getApi", "app_debug"})
public final class ApiClient {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BASE_URL = "https://api.alive-demo.com/";
    
    /**
     * Mock API实现
     * 使用lazy委托延迟初始化，只在第一次调用时创建实例
     * Mock实现会模拟网络延迟并返回模拟数据，适合开发和测试
     */
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy mockApi$delegate = null;
    
    /**
     * 真实的Retrofit API实现
     * 连接到真实的后端服务器进行HTTP请求
     * 使用GsonConverterFactory将JSON自动转换为Kotlin数据类
     * 当需要切换到生产环境时，可改为返回此实例
     */
    @org.jetbrains.annotations.NotNull()
    private static final kotlin.Lazy retrofitApi$delegate = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.alive.network.ApiClient INSTANCE = null;
    
    private ApiClient() {
        super();
    }
    
    /**
     * Mock API实现
     * 使用lazy委托延迟初始化，只在第一次调用时创建实例
     * Mock实现会模拟网络延迟并返回模拟数据，适合开发和测试
     */
    private final com.example.alive.network.api.AliveApi getMockApi() {
        return null;
    }
    
    /**
     * 真实的Retrofit API实现
     * 连接到真实的后端服务器进行HTTP请求
     * 使用GsonConverterFactory将JSON自动转换为Kotlin数据类
     * 当需要切换到生产环境时，可改为返回此实例
     */
    private final com.example.alive.network.api.AliveApi getRetrofitApi() {
        return null;
    }
    
    /**
     * 获取API实例
     * 目前返回Mock实现用于开发/测试
     * 生产环境需要将返回值改为retrofitApi
     *
     * @return AliveApi实例
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.network.api.AliveApi getApi() {
        return null;
    }
}