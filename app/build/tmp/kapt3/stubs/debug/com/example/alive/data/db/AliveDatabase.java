package com.example.alive.data.db;

/**
 * Room数据库主类
 * 定义数据库结构、表、版本、DAOs等
 * 使用单例模式确保应用中只有一个数据库实例
 *
 * @property entities 数据库包含的表实体类数组
 * @property version 数据库版本号，更新表结构时需要递增
 * @property exportSchema 是否导出Schema到JSON文件用于版本控制（开发时为false）
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\'\u0018\u0000 \u00072\u00020\u0001:\u0001\u0007B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H&J\b\u0010\u0005\u001a\u00020\u0006H&\u00a8\u0006\b"}, d2 = {"Lcom/example/alive/data/db/AliveDatabase;", "Landroidx/room/RoomDatabase;", "()V", "aliveImageDao", "Lcom/example/alive/data/dao/AliveImageDao;", "taskDao", "Lcom/example/alive/data/dao/TaskDao;", "Companion", "app_debug"})
@androidx.room.Database(entities = {com.example.alive.data.entity.AliveImage.class, com.example.alive.data.entity.Task.class}, version = 1, exportSchema = false)
@androidx.room.TypeConverters(value = {com.example.alive.data.db.DatabaseConverters.class})
public abstract class AliveDatabase extends androidx.room.RoomDatabase {
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private static volatile com.example.alive.data.db.AliveDatabase INSTANCE;
    @org.jetbrains.annotations.NotNull()
    public static final com.example.alive.data.db.AliveDatabase.Companion Companion = null;
    
    public AliveDatabase() {
        super();
    }
    
    /**
     * 获取AliveImage表的DAO
     * @return AliveImageDao实例
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.alive.data.dao.AliveImageDao aliveImageDao();
    
    /**
     * 获取Task表的DAO
     * @return TaskDao实例
     */
    @org.jetbrains.annotations.NotNull()
    public abstract com.example.alive.data.dao.TaskDao taskDao();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/example/alive/data/db/AliveDatabase$Companion;", "", "()V", "INSTANCE", "Lcom/example/alive/data/db/AliveDatabase;", "getInstance", "context", "Landroid/content/Context;", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * 获取数据库单例实例
         * 使用双检锁定模式确保线程安全
         *
         * @param context Android上下文，用于数据库初始化
         * @return 单例AliveDatabase实例
         */
        @org.jetbrains.annotations.NotNull()
        public final com.example.alive.data.db.AliveDatabase getInstance(@org.jetbrains.annotations.NotNull()
        android.content.Context context) {
            return null;
        }
    }
}