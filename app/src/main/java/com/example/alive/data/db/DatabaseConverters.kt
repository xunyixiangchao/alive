package com.example.alive.data.db

import androidx.room.TypeConverter
import com.example.alive.data.entity.TaskStatus

/**
 * DatabaseConverters - Room数据库类型转换器
 *
 * 用于在Room数据库和Kotlin对象之间进行类型转换
 * 当Entity中包含Room无法直接存储的复杂类型时需要使用转换器
 *
 * 使用方式：
 * 在AliveDatabase中使用@TypeConverters(DatabaseConverters::class)注解
 *
 * 转换的类型：
 * - TaskStatus枚举 <-> String
 */
class DatabaseConverters {

    /**
     * 将TaskStatus枚举转换为数据库存储的String
     * @param value TaskStatus枚举值
     * @return 枚举的名称字符串
     */
    @TypeConverter
    fun taskStatusToString(value: TaskStatus?): String? {
        return value?.name
    }

    /**
     * 从数据库存储的String转换回TaskStatus枚举
     * @param value 数据库中存储的字符串
     * @return 对应的TaskStatus枚举值
     * @throws IllegalArgumentException 如果字符串不对应任何枚举值
     */
    @TypeConverter
    fun stringToTaskStatus(value: String?): TaskStatus? {
        return value?.let { TaskStatus.valueOf(it) }
    }
}
