package com.example.alive.ui.viewmodel;

/**
 * AliveViewModelFactory - ViewModel工厂类
 *
 * 职责：
 * - 为所有ViewModel提供统一的创建入口
 * - 管理ViewModel的依赖注入（Repository实例）
 * - 根据ViewModel类型创建相应的实例
 *
 * 使用方式：
 * ```
 * val factory = AliveViewModelFactory(repository)
 * val viewModel = ViewModelProvider(this, factory)[SharedViewModel::class.java]
 * ```
 *
 * @param repository AliveRepository实例，将被注入到所有ViewModel中
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J%\u0010\u0005\u001a\u0002H\u0006\"\b\b\u0000\u0010\u0006*\u00020\u00072\f\u0010\b\u001a\b\u0012\u0004\u0012\u0002H\u00060\tH\u0016\u00a2\u0006\u0002\u0010\nR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000b"}, d2 = {"Lcom/example/alive/ui/viewmodel/AliveViewModelFactory;", "Landroidx/lifecycle/ViewModelProvider$Factory;", "repository", "Lcom/example/alive/data/repository/AliveRepository;", "(Lcom/example/alive/data/repository/AliveRepository;)V", "create", "T", "Landroidx/lifecycle/ViewModel;", "modelClass", "Ljava/lang/Class;", "(Ljava/lang/Class;)Landroidx/lifecycle/ViewModel;", "app_debug"})
public final class AliveViewModelFactory implements androidx.lifecycle.ViewModelProvider.Factory {
    @org.jetbrains.annotations.NotNull()
    private final com.example.alive.data.repository.AliveRepository repository = null;
    
    public AliveViewModelFactory(@org.jetbrains.annotations.NotNull()
    com.example.alive.data.repository.AliveRepository repository) {
        super();
    }
    
    /**
     * 创建ViewModel实例
     * @param modelClass 要创建的ViewModel类型
     * @return 创建的ViewModel实例
     * @throws IllegalArgumentException 当传入未知的ViewModel类型时抛出
     *
     * 支持的ViewModel类型：
     * - SharedViewModel: 共享数据ViewModel
     * - Fragment1ViewModel: 图片选择界面
     * - Fragment2ViewModel: 8帧提取界面
     * - Fragment3ViewModel: 手动圈选界面
     * - Fragment4ViewModel: 任务等待界面
     * - Fragment5ViewModel: 结果展示界面
     * - TaskListViewModel: 任务列表界面
     */
    @java.lang.Override()
    @kotlin.Suppress(names = {"UNCHECKED_CAST"})
    @org.jetbrains.annotations.NotNull()
    public <T extends androidx.lifecycle.ViewModel>T create(@org.jetbrains.annotations.NotNull()
    java.lang.Class<T> modelClass) {
        return null;
    }
}