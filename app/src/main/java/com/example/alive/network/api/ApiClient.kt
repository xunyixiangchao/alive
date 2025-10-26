package com.example.alive.network.api

import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

/**
 * ApiClient - API客户端工厂
 *
 * 职责：
 * - 管理AliveApi的实例创建
 * - 在Mock和真实API之间切换
 * - 配置Retrofit网络框架
 *
 * 使用方式：
 * 1. 开发/测试: 使用MockAliveApi（无需后端）
 * 2. 生产: 使用Retrofit真实API（连接后端服务）
 *
 * 切换方式：
 * 在getApi()方法中修改return语句：
 * - 开发: return mockApi
 * - 生产: return retrofitApi
 */
object ApiClient {

    /**
     * Mock API实例
     * 用于开发和测试，模拟服务器响应
     */
    private val mockApi = MockAliveApi()

    /**
     * 真实Retrofit API实例
     * 用于生产环境，连接真实的后端服务
     */
    private lateinit var retrofitApi: AliveApi

    /**
     * Retrofit实例
     * 配置JSON序列化和API基础URL
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://your-server-address.com/") // 替换为实际的后端地址
            .addConverterFactory(
                Json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    /**
     * 初始化真实API
     */
    private fun initRetrofitApi() {
        if (!::retrofitApi.isInitialized) {
            retrofitApi = retrofit.create(AliveApi::class.java)
        }
    }

    /**
     * 获取API实例
     *
     * 当前配置：使用Mock API（开发阶段）
     * 生产环境修改为：return getRetrofitApi()
     *
     * @return AliveApi实例
     */
    fun getApi(): AliveApi {
        // 开发环境：使用Mock API
        return mockApi

        // 生产环境：取消注释下行
        // return getRetrofitApi()
    }

    /**
     * 获取真实Retrofit API
     * @return AliveApi实例
     */
    fun getRetrofitApi(): AliveApi {
        initRetrofitApi()
        return retrofitApi
    }

    /**
     * 获取Mock API（便于测试）
     * @return MockAliveApi实例
     */
    fun getMockApi(): MockAliveApi {
        return mockApi
    }
}
