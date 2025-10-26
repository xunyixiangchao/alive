package com.example.alive.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.alive.data.entity.AliveImage
import kotlinx.coroutines.flow.Flow

/**
 * AliveImageDao - AliveImage表的数据访问对象
 *
 * 提供对alive_images表的CRUD操作
 * 使用Room库自动生成SQL实现
 * 所有操作都是suspend函数，需在协程中调用
 *
 * Room会根据此接口自动生成SQL语句：
 * - @Query: 自定义查询
 * - @Insert: 插入单条或多条记录
 * - @Update: 更新记录
 * - @Delete: 删除记录
 */
@Dao
interface AliveImageDao {

    /**
     * 插入单个AliveImage记录
     * @param aliveImage 要插入的AliveImage对象
     * @return 插入成功后的记录ID（如果ID已存在则返回-1）
     */
    @Insert
    suspend fun insert(aliveImage: AliveImage): Long

    /**
     * 插入多个AliveImage记录
     * @param aliveImages AliveImage对象列表
     * @return 插入的ID列表
     */
    @Insert
    suspend fun insertAll(aliveImages: List<AliveImage>): List<Long>

    /**
     * 更新AliveImage记录
     * @param aliveImage 要更新的AliveImage对象
     */
    @Update
    suspend fun update(aliveImage: AliveImage)

    /**
     * 删除AliveImage记录
     * @param aliveImage 要删除的AliveImage对象
     */
    @Delete
    suspend fun delete(aliveImage: AliveImage)

    /**
     * 根据ID查询AliveImage
     * @param id AliveImage的ID
     * @return 查询到的AliveImage对象，若不存在返回null
     */
    @Query("SELECT * FROM alive_images WHERE id = :id")
    suspend fun getById(id: Long): AliveImage?

    /**
     * 查询所有AliveImage记录
     * 返回Flow，自动响应数据库变化
     * @return Flow<List<AliveImage>> 所有AliveImage的Flow，按创建时间倒序
     */
    @Query("SELECT * FROM alive_images ORDER BY timestamp DESC")
    fun getAllAsFlow(): Flow<List<AliveImage>>

    /**
     * 查询所有AliveImage记录（挂起函数版）
     * @return 所有AliveImage的列表
     */
    @Query("SELECT * FROM alive_images ORDER BY timestamp DESC")
    suspend fun getAll(): List<AliveImage>

    /**
     * 查询收藏的AliveImage记录
     * @return Flow<List<AliveImage>> 所有收藏的AliveImage
     */
    @Query("SELECT * FROM alive_images WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoritesAsFlow(): Flow<List<AliveImage>>

    /**
     * 更新收藏状态
     * @param id AliveImage的ID
     * @param isFavorite 新的收藏状态
     */
    @Query("UPDATE alive_images SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Long, isFavorite: Boolean)

    /**
     * 删除所有记录
     */
    @Query("DELETE FROM alive_images")
    suspend fun deleteAll()

    /**
     * 获取总记录数
     */
    @Query("SELECT COUNT(*) FROM alive_images")
    suspend fun getCount(): Int
}
