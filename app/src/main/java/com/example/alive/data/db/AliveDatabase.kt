package com.example.alive.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alive.data.dao.AliveImageDao
import com.example.alive.data.dao.TaskDao
import com.example.alive.data.entity.AliveImage
import com.example.alive.data.entity.Task

/**
 * AliveDatabase - Room数据库定义
 *
 * 这是应用的本地SQLite数据库的定义和管理中心
 * 包含的表：
 * 1. alive_images - 存储用户选择的ALIVE图像/视频
 * 2. tasks - 存储处理任务的信息和状态
 *
 * 数据库特性：
 * - 单例模式：整个应用只有一个数据库实例
 * - 自动迁移：初始版本为1，如需修改Entity需要升级版本号
 * - 外键支持：Task表有外键约束指向AliveImage表
 *
 * 使用方式：
 * 1. 初始化：AliveDatabase.getInstance(context)
 * 2. 获取DAO：database.aliveImageDao()、database.taskDao()
 * 3. 数据库操作：通过DAO的suspend函数在协程中进行
 *
 * @param entities 数据库包含的Entity类列表
 * @param version 数据库版本号，修改Entity需要升级此号并创建迁移
 * @param exportSchema 是否导出schema用于版本管理（true）
 */
@Database(
    entities = [AliveImage::class, Task::class],
    version = 1,
    exportSchema = true
)
@androidx.room.TypeConverters(DatabaseConverters::class)
abstract class AliveDatabase : RoomDatabase() {

    /**
     * 获取AliveImageDao接口
     * @return AliveImageDao实例，用于访问alive_images表
     */
    abstract fun aliveImageDao(): AliveImageDao

    /**
     * 获取TaskDao接口
     * @return TaskDao实例，用于访问tasks表
     */
    abstract fun taskDao(): TaskDao

    companion object {
        /**
         * 数据库单例实例
         * 使用volatile关键字确保多线程可见性
         */
        @Volatile
        private var INSTANCE: AliveDatabase? = null

        /**
         * 获取或创建数据库实例
         * 采用单例模式，确保整个应用只有一个数据库实例
         *
         * 初始化过程：
         * 1. 检查单例是否已存在，存在则直接返回
         * 2. 使用synchronized同步块进行双重检查锁定
         * 3. 使用Room.databaseBuilder创建数据库
         * 4. 数据库文件存储位置：/data/data/com.example.alive/databases/alive_database
         *
         * @param context Android上下文，用于访问文件系统
         * @return AliveDatabase单例实例
         */
        fun getInstance(context: Context): AliveDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AliveDatabase::class.java,
                    "alive_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }

        /**
         * 销毁数据库实例（主要用于测试）
         */
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
