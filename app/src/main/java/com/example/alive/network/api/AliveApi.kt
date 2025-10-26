package com.example.alive.network.api

import com.example.alive.network.dto.CircleRemovalResponse
import com.example.alive.network.dto.FavoriteResponse
import com.example.alive.network.dto.TaskStatusResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * AliveApi - REST API接口定义
 *
 * 定义了与后端服务器通信的所有API端点
 * 使用Retrofit库实现HTTP请求
 * 所有方法都是suspend函数，支持协程调用
 *
 * 主要端点：
 * 1. submitCircleRemoval - 提交圈选数据，创建处理任务
 * 2. getTaskStatus - 获取任务处理状态（用于轮询）
 * 3. updateFavorite - 更新任务收藏状态
 *
 * 实现类：
 * - MockAliveApi: 开发/测试用Mock实现
 * - Retrofit代理: 生产环境用真实HTTP实现
 */
interface AliveApi {

    /**
     * 提交圈选数据到服务器
     *
     * 流程：
     * 1. Fragment3收集用户绘制的圆圈数据
     * 2. 转换为JSON字符串
     * 3. 调用此API提交到后端
     * 4. 后端返回新创建的任务ID
     * 5. Repository保存Task到本地数据库
     * 6. Fragment跳转到Fragment4进行轮询
     *
     * @param aliveImageId 关联的AliveImage ID
     * @param circleSelectionsJson 圆圈数据JSON字符串
     *                            格式：[{"centerX":100.0,"centerY":150.0,"radius":50.0},...]
     * @return CircleRemovalResponse，包含新创建的taskId
     */
    @POST("/api/circle-removal/submit")
    @FormUrlEncoded
    suspend fun submitCircleRemoval(
        @Field("aliveImageId") aliveImageId: Long,
        @Field("circleSelections") circleSelectionsJson: String
    ): CircleRemovalResponse

    /**
     * 轮询获取任务处理状态
     *
     * Fragment4定时调用此接口（每2秒一次）获取任务状态
     * 当status=COMPLETED时跳转到Fragment5
     *
     * @param taskId 任务ID
     * @return TaskStatusResponse，包含当前状态和处理进度
     */
    @GET("/api/tasks/status")
    suspend fun getTaskStatus(
        @Query("taskId") taskId: Long
    ): TaskStatusResponse

    /**
     * 更新任务的收藏状态
     *
     * Fragment5或TaskListFragment中用户点击收藏按钮时调用
     *
     * @param taskId 任务ID
     * @param isFavorite 新的收藏状态（true=收藏, false=取消收藏）
     * @return FavoriteResponse
     */
    @POST("/api/tasks/favorite")
    @FormUrlEncoded
    suspend fun updateFavorite(
        @Field("taskId") taskId: Long,
        @Field("isFavorite") isFavorite: Boolean
    ): FavoriteResponse
}
