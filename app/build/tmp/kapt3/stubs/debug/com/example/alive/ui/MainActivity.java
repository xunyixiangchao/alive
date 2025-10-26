package com.example.alive.ui;

/**
 * 主Activity
 * 应用程序的入口点，负责：
 * 1. 初始化数据库、Repository、ViewModel等核心组件
 * 2. 设置Fragment导航框架
 * 3. 为所有Fragment提供访问SharedViewModel和NavController的接口
 *
 * 使用ViewBinding进行布局绑定
 * 使用Navigation Component管理Fragment间的导航
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\t\u001a\u00020\u0006J\u0006\u0010\n\u001a\u00020\bJ\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/example/alive/ui/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "binding", "Lcom/example/alive/databinding/ActivityMainBinding;", "navController", "Landroidx/navigation/NavController;", "sharedViewModel", "Lcom/example/alive/ui/viewmodel/SharedViewModel;", "getNavController", "getSharedViewModel", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_debug"})
public final class MainActivity extends androidx.appcompat.app.AppCompatActivity {
    private com.example.alive.databinding.ActivityMainBinding binding;
    private androidx.navigation.NavController navController;
    private com.example.alive.ui.viewmodel.SharedViewModel sharedViewModel;
    
    public MainActivity() {
        super();
    }
    
    @java.lang.Override()
    protected void onCreate(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    /**
     * 为Fragment提供SharedViewModel访问接口
     * Fragment通过此方法获取Activity级别的共享ViewModel
     *
     * @return SharedViewModel实例
     */
    @org.jetbrains.annotations.NotNull()
    public final com.example.alive.ui.viewmodel.SharedViewModel getSharedViewModel() {
        return null;
    }
    
    /**
     * 为Fragment提供NavController访问接口
     * Fragment通过此方法进行导航操作
     *
     * @return NavController实例
     */
    @org.jetbrains.annotations.NotNull()
    public final androidx.navigation.NavController getNavController() {
        return null;
    }
}