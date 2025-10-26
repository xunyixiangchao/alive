package com.example.alive.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.alive.data.entity.Task
import com.example.alive.data.entity.TaskStatus
import kotlinx.coroutines.flow.Flow

/**
 * TaskDao - Task表的数据访问对象
 *
 * 提供对tasks表的CRUD操作和复杂查询
 * 使用Room库自动生成SQL实现
 * 所有操作都是suspend函数或返回Flow，需在协程中调用
 *
 * 主要功能：
 * - 基础CRUD：插入、更新、删除、查询
 * - 状态查询：按状态过滤任务
 * - Flow查询：自动响应数据库变化
 */
@Dao
interface TaskDao {

    /**
     * 插入单个Task记录
     * 如果Task ID已存在，则使用REPLACE策略（覆盖原有记录）
     * @param task 要插入的Task对象
     * @return 插入成功后的记录ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    /**
     * 插入多个Task记录
     * @param tasks Task对象列表
     * @return 插入的ID列表
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tasks: List<Task>): List<Long>

    /**
     * 更新Task记录
     * @param task 要更新的Task对象
     */
    @Update
    suspend fun update(task: Task)

    /**
     * 删除Task记录
     * @param task 要删除的Task对象
     */
    @Delete
    suspend fun delete(task: Task)

    /**
     * 根据ID查询Task
     * @param id Task的ID
     * @return 查询到的Task对象，若不存在返回null
     */
    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): Task?

    /**
     * 查询所有Task记录
     * 返回Flow，自动响应数据库变化
     * @return Flow<List<Task>> 所有Task的Flow，按创建时间倒序
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAllAsFlow(): Flow<List<Task>>

    /**
     * 查询所有Task记录（挂起函数版）
     * @return 所有Task的列表
     */
    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    suspend fun getAll(): List<Task>

    /**
     * 根据特定状态查询Task
     * 返回Flow，自动响应数据库变化
     * @param status 要查询的任务状态
     * @return Flow<List<Task>> 指定状态的Task列表
     */
    @Query("SELECT * FROM tasks WHERE status = :status ORDER BY createdAt DESC")
    fun getByStatusAsFlow(status: TaskStatus): Flow<List<Task>>

    /**
     * 查询已完成的Task
     * @return Flow<List<Task>> 所有已完成的Task
     */
    @Query("SELECT * FROM tasks WHERE status = 'COMPLETED' ORDER BY createdAt DESC")
    fun getCompletedTasksAsFlow(): Flow<List<Task>>

    /**
     * 查询未完成的Task（PENDING、MARKING、PROCESSING状态）
     * @return Flow<List<Task>> 所有未完成的Task
     */
    @Query("SELECT * FROM tasks WHERE status IN ('PENDING', 'MARKING', 'PROCESSING') ORDER BY createdAt DESC")
    fun getPendingTasksAsFlow(): Flow<List<Task>>

    /**
     * 根据aliveImageId查询所有关联的Task
     * @param aliveImageId AliveImage的ID
     * @return Flow<List<Task>> 所有关联的Task
     */
    @Query("SELECT * FROM tasks WHERE aliveImageId = :aliveImageId ORDER BY createdAt DESC")
    fun getTasksByImageIdAsFlow(aliveImageId: Long): Flow<List<Task>>

    /**
     * 更新Task的状态
     * @param id Task的ID
     * @param status 新的状态
     */
    @Query("UPDATE tasks SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(id: Long, status: TaskStatus, updatedAt: Long = System.currentTimeMillis())

    /**
     * 更新Task的结果图路径
     * @param id Task的ID
     * @param resultImagePath 结果图的路径
     */
    @Query("UPDATE tasks SET resultImagePath = :resultImagePath, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateResultImagePath(id: Long, resultImagePath: String, updatedAt: Long = System.currentTimeMillis())

    /**
     * 更新Task的收藏状态
     * @param id Task的ID
     * @param isFavorite 新的收藏状态
     */
    @Query("UPDATE tasks SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    /**
     * 删除所有Task记录
     */
    @Query("DELETE FROM tasks")
    suspend fun deleteAll()

    /**
     * 获取Task总记录数
     */
    @Query("SELECT COUNT(*) FROM tasks")
    suspend fun getCount(): Int

    /**
     * 获取已完成的Task数量
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE status = 'COMPLETED'")
    suspend fun getCompletedCount(): Int

    /**
     * 获取未完成的Task数量
     */
    @Query("SELECT COUNT(*) FROM tasks WHERE status IN ('PENDING', 'MARKING', 'PROCESSING')")
    suspend fun getPendingCount(): Int
}
