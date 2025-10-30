package com.example.alive.data.repository

import com.example.alive.data.dao.AliveImageDao
import com.example.alive.data.dao.TaskDao
import com.example.alive.data.entity.AliveImage
import com.example.alive.data.entity.Task
import com.example.alive.data.entity.TaskStatus
import com.example.alive.network.api.AliveApi
import com.example.alive.network.api.ApiClient
import com.example.alive.network.dto.FavoriteResponse
import com.example.alive.network.dto.TaskStatusResponse
import com.example.alive.network.dto.UploadUrlResponse
import com.example.alive.network.dto.ImageUploadResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

/**
 * AliveRepository - 数据仓库，单一的数据访问入口
 *
 * 职责：
 * - 聚合所有数据来源（本地数据库 + 远程API）
 * - 为ViewModel提供统一的数据访问接口
 * - 管理数据一致性（本地优先，必要时同步远程）
 * - 隐藏数据来源的具体细节（DAO、API等）
 *
 * ViewModel不应该直接访问DAO或API，而是通过Repository
 * 这样做的好处：
 * - 便于测试（可以Mock Repository）
 * - 便于修改数据来源（如从API改为本地或反之）
 * - 集中管理数据一致性和错误处理
 *
 * @param aliveImageDao AliveImage表的DAO
 * @param taskDao Task表的DAO
 */
class AliveRepository(
    private val aliveImageDao: AliveImageDao,
    private val taskDao: TaskDao,
    private val api: AliveApi = ApiClient.getApi()
) {

    // ==================== AliveImage操作 ====================

    /**
     * 插入AliveImage记录
     * @param aliveImage 要插入的AliveImage对象
     * @return 插入成功后的记录ID
     */
    suspend fun insertAliveImage(aliveImage: AliveImage): Long {
        return aliveImageDao.insert(aliveImage)
    }

    /**
     * 更新AliveImage记录
     * @param aliveImage 要更新的AliveImage对象
     */
    suspend fun updateAliveImage(aliveImage: AliveImage) {
        aliveImageDao.update(aliveImage)
    }

    /**
     * 删除AliveImage记录
     * @param aliveImage 要删除的AliveImage对象
     */
    suspend fun deleteAliveImage(aliveImage: AliveImage) {
        aliveImageDao.delete(aliveImage)
    }

    /**
     * 根据ID查询AliveImage
     * @param id AliveImage的ID
     * @return 查询到的AliveImage对象，若不存在返回null
     */
    suspend fun getAliveImageById(id: Long): AliveImage? {
        return aliveImageDao.getById(id)
    }

    /**
     * 获取所有AliveImage的Flow
     * 自动响应数据库变化
     * @return Flow<List<AliveImage>>
     */
    fun getAllAliveImages(): Flow<List<AliveImage>> {
        return aliveImageDao.getAllAsFlow()
    }

    /**
     * 获取所有AliveImage（挂起函数）
     * @return 所有AliveImage的列表
     */
    suspend fun getAllAliveImagesSync(): List<AliveImage> {
        return aliveImageDao.getAll()
    }

    /**
     * 更新AliveImage的收藏状态
     * @param id AliveImage的ID
     * @param isFavorite 新的收藏状态
     */
    suspend fun updateAliveImageFavorite(id: Long, isFavorite: Boolean) {
        aliveImageDao.updateFavorite(id, isFavorite)
    }

    // ==================== Task操作 ====================

    /**
     * 插入Task记录
     * @param task 要插入的Task对象
     * @return 插入成功后的记录ID
     */
    suspend fun insertTask(task: Task): Long {
        return taskDao.insert(task)
    }

    /**
     * 更新Task记录
     * @param task 要更新的Task对象
     */
    suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    /**
     * 删除Task记录
     * @param task 要删除的Task对象
     */
    suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    /**
     * 根据ID查询Task
     * @param id Task的ID
     * @return 查询到的Task对象，若不存在返回null
     */
    suspend fun getTaskById(id: Long): Task? {
        return taskDao.getById(id)
    }

    /**
     * 获取所有Task的Flow
     * 自动响应数据库变化
     * @return Flow<List<Task>>
     */
    fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllAsFlow()
    }

    /**
     * 获取所有Task（挂起函数）
     * @return 所有Task的列表
     */
    suspend fun getAllTasksSync(): List<Task> {
        return taskDao.getAll()
    }

    /**
     * 根据状态查询Task
     * @param status 要查询的任务状态
     * @return Flow<List<Task>>
     */
    fun getTasksByStatus(status: TaskStatus): Flow<List<Task>> {
        return taskDao.getByStatusAsFlow(status)
    }

    /**
     * 获取已完成的Task列表
     * @return Flow<List<Task>>
     */
    fun getCompletedTasks(): Flow<List<Task>> {
        return taskDao.getCompletedTasksAsFlow()
    }

    /**
     * 获取未完成的Task列表
     * @return Flow<List<Task>>
     */
    fun getPendingTasks(): Flow<List<Task>> {
        return taskDao.getPendingTasksAsFlow()
    }

    /**
     * 更新Task的状态
     * @param id Task的ID
     * @param status 新的状态
     */
    suspend fun updateTaskStatus(id: Long, status: TaskStatus) {
        taskDao.updateStatus(id, status)
    }

    /**
     * 更新Task的结果图路径
     * @param id Task的ID
     * @param resultImagePath 结果图的路径
     */
    suspend fun updateTaskResultImagePath(id: Long, resultImagePath: String) {
        taskDao.updateResultImagePath(id, resultImagePath)
    }

    /**
     * 更新Task的收藏状态
     * @param taskId Task的ID
     * @param isFavorite 新的收藏状态
     */
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean) {
        taskDao.updateFavorite(taskId, isFavorite)
    }

    // ==================== API操作 ====================

    /**
     * 提交圈选数据到服务器，创建处理任务
     * @param aliveImageId 关联的AliveImage ID
     * @param circleSelectionsJson 圆圈数据的JSON字符串
     * @return 服务器返回的任务ID
     */
    suspend fun submitCircleRemoval(
        aliveImageId: Long,
        circleSelectionsJson: String
    ): Long {
        val response = api.submitCircleRemoval(aliveImageId, circleSelectionsJson)
        // 同时在本地创建Task记录
        val task = Task(
            id = response.taskId,
            aliveImageId = aliveImageId,
            status = TaskStatus.PENDING,
            circleSelectionsJson = circleSelectionsJson
        )
        insertTask(task)
        return response.taskId
    }

    /**
     * 轮询获取任务状态
     * @param taskId 任务ID
     * @return 任务状态Response
     */
    suspend fun getTaskStatus(taskId: Long): TaskStatusResponse {
        val response = api.getTaskStatus(taskId)
        // 更新本地数据库中的任务状态
        updateTaskStatus(taskId, response.status)
        // 如果有结果图路径，也更新本地记录
        if (!response.resultImagePath.isNullOrEmpty()) {
            updateTaskResultImagePath(taskId, response.resultImagePath)
        }
        return response
    }

    /**
     * 更新任务的收藏状态（调用API）
     * @param taskId 任务ID
     * @param isFavorite 新的收藏状态
     * @return API返回的FavoriteResponse
     */
    suspend fun updateFavorite(taskId: Long, isFavorite: Boolean): FavoriteResponse {
        val response = api.updateFavorite(taskId, isFavorite)
        // 同时更新本地数据库
        updateTaskFavorite(taskId, response.isFavorite == 1)
        return response
    }

    // ==================== 图片上传操作 ====================

    /**
     * 获取图片上传地址
     *
     * 调用API获取临时的上传URL
     * Fragment1在用户选择图片后调用
     *
     * @return UploadUrlResponse，包含上传地址和有效期
     */
    suspend fun getUploadUrl(): UploadUrlResponse {
        return api.getUploadUrl()
    }

    /**
     * 上传图片到服务器
     *
     * 使用获取的上传地址上传实际的图片文件
     * 支持多种图片格式和大小
     *
     * @param imageBody 包含图片文件的MultipartBody
     * @return ImageUploadResponse，包含上传结果和图片URL
     */
    suspend fun uploadImage(imageBody: MultipartBody): ImageUploadResponse {
        return api.uploadImage(imageBody)
    }
}
