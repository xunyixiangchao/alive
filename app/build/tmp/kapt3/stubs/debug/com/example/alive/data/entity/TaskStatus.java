package com.example.alive.data.entity;

/**
 * 任务状态枚举
 *
 * @property PENDING 待处理 - 任务刚创建，还未开始处理
 * @property MARKING 标记中 - 用户正在Fragment3中圈选圆圈
 * @property PROCESSING 处理中 - 已提交到服务器，等待处理结果
 * @property COMPLETED 已完成 - 服务器返回了处理结果
 * @property FAILED 失败 - 处理过程中发生错误
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0007\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006j\u0002\b\u0007\u00a8\u0006\b"}, d2 = {"Lcom/example/alive/data/entity/TaskStatus;", "", "(Ljava/lang/String;I)V", "PENDING", "MARKING", "PROCESSING", "COMPLETED", "FAILED", "app_debug"})
public enum TaskStatus {
    /*public static final*/ PENDING /* = new PENDING() */,
    /*public static final*/ MARKING /* = new MARKING() */,
    /*public static final*/ PROCESSING /* = new PROCESSING() */,
    /*public static final*/ COMPLETED /* = new COMPLETED() */,
    /*public static final*/ FAILED /* = new FAILED() */;
    
    TaskStatus() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.example.alive.data.entity.TaskStatus> getEntries() {
        return null;
    }
}