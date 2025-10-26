package com.example.alive.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.alive.R
import com.example.alive.data.db.AliveDatabase
import com.example.alive.data.repository.AliveRepository
import com.example.alive.databinding.ActivityMainBinding
import com.example.alive.ui.viewmodel.SharedViewModel
import com.example.alive.ui.viewmodel.SharedViewModelFactory

/**
 * MainActivity - 应用的主Activity
 *
 * 职责：
 * 1. 初始化数据库和Repository
 * 2. 创建SharedViewModel（跨Fragment通信）
 * 3. 管理Navigation框架和Fragment容器
 * 4. 处理返回键和Navigation栈
 *
 * 生命周期：
 * 1. onCreate: 初始化数据库、Repository、SharedViewModel
 * 2. Activity始终运行，直到应用关闭
 * 3. 所有Fragment在此Activity的NavHostFragment容器中运行
 * 4. SharedViewModel的生命周期与Activity相同
 *
 * 关键点：
 * - SharedViewModel是Activity级别的，所有Fragment都可以访问
 * - Navigation组件自动管理Fragment的生命周期和返回栈
 * - 数据库和Repository是单例，在此初始化并在Fragment中使用
 */
class MainActivity : AppCompatActivity() {

    /**
     * ViewBinding实例，用于类型安全的View访问
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Navigation控制器，用于管理Fragment跳转
     */
    private lateinit var navController: NavController

    /**
     * SharedViewModel实例
     * 生命周期：Activity级别
     * 作用：在所有Fragment之间共享数据
     */
    private val sharedViewModel: SharedViewModel by viewModels {
        SharedViewModelFactory(getRepository())
    }

    /**
     * 数据库实例（延迟初始化）
     */
    private var database: AliveDatabase? = null

    /**
     * Repository实例（延迟初始化）
     */
    private var repository: AliveRepository? = null

    /**
     * Activity创建时的初始化
     *
     * 流程：
     * 1. 初始化ViewBinding
     * 2. 初始化数据库和Repository
     * 3. 设置root view
     * 4. 获取Navigation控制器
     * 5. 设置Navigation事件监听（可选，用于调试）
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 初始化数据库和Repository
        initializeDatabase()

        // 获取Navigation Host Fragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // 获取Navigation控制器
        navController = navHostFragment.navController

        // 设置返回键行为（Navigation组件自动处理）
        // 不需要额外配置，Navigation已处理返回栈

        // 可选：监听导航事件（用于日志或分析）
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // 当Fragment改变时触发
            // 可在此进行日志记录或分析
            logNavigationEvent(destination.label?.toString() ?: "Unknown")
        }
    }

    /**
     * 初始化数据库和Repository
     *
     * 创建数据库单例和Repository
     * 这些实例会在SharedViewModel和所有Fragment中使用
     */
    private fun initializeDatabase() {
        // 获取或创建数据库单例
        database = AliveDatabase.getInstance(this)

        // 创建Repository实例
        repository = getRepository()
    }

    /**
     * 获取Repository实例
     *
     * 此方法可被多次调用，每次都返回同一个Repository实例
     * Repository包装了数据库的DAO，为ViewModel提供统一的数据访问接口
     *
     * @return AliveRepository实例
     */
    fun getRepository(): AliveRepository {
        return repository ?: run {
            val db = database ?: AliveDatabase.getInstance(this)
            AliveRepository(
                aliveImageDao = db.aliveImageDao(),
                taskDao = db.taskDao()
            ).also { repository = it }
        }
    }

    /**
     * 获取SharedViewModel
     *
     * Fragment可通过此方法获取Activity级别的SharedViewModel
     * 用于Fragment之间的数据共享
     *
     * @return SharedViewModel实例
     */
    fun getSharedViewModel(): SharedViewModel = sharedViewModel

    /**
     * 获取Navigation控制器
     *
     * Fragment可通过此方法获取Navigation控制器
     * 用于Fragment跳转
     *
     * @return NavController实例
     */
    fun getNavController(): NavController = navController

    /**
     * 处理返回键事件
     *
     * Navigation组件自动处理返回栈，此方法通常不需要重写
     * 但如需自定义返回行为，可在此添加逻辑
     */
    override fun onBackPressed() {
        // Navigation组件会自动处理返回事件
        // 如果返回栈为空，会调用此方法
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }

    /**
     * 记录导航事件（用于调试）
     *
     * @param destination 目标Fragment的标签
     */
    private fun logNavigationEvent(destination: String) {
        android.util.Log.d("Navigation", "Navigated to: $destination")
    }

    /**
     * Activity销毁时的清理
     *
     * 通常不需要手动释放资源，Android系统会自动清理
     * 但如果有长期运行的操作（如Service），需在此取消
     */
    override fun onDestroy() {
        super.onDestroy()
        // ViewModel和Repository会自动被垃圾回收
        // 不需要手动清理
    }
}
