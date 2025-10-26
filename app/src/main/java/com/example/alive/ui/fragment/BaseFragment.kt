package com.example.alive.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.alive.data.db.AliveDatabase
import com.example.alive.data.repository.AliveRepository
import com.example.alive.ui.MainActivity
import com.example.alive.ui.viewmodel.AliveViewModelFactory
import com.example.alive.ui.viewmodel.SharedViewModel
import androidx.lifecycle.ViewModel

/**
 * BaseFragment - 所有Fragment的抽象基类
 *
 * 职责：
 * - 提供通用的Fragment初始化逻辑
 * - 自动管理ViewBinding和ViewModel的创建
 * - 为所有Fragment提供统一的生命周期管理
 * - 消除Fragment中的重复初始化代码
 *
 * 泛型参数：
 * @param VB 该Fragment对应的ViewBinding类
 * @param VM 该Fragment对应的ViewModel类
 *
 * 抽象方法（子类必须实现）：
 * - createViewBinding(): 创建并返回ViewBinding实例
 * - getViewModelClass(): 返回ViewModel的Class对象
 * - setupUI(): 设置UI组件和事件监听器
 * - observeViewModel(): 观察ViewModel中的数据变化
 *
 * 自动提供（子类可直接使用）：
 * - binding: 类型安全的View绑定
 * - viewModel: 该Fragment的ViewModel实例
 * - sharedViewModel: 跨Fragment的SharedViewModel实例
 * - database: AliveDatabase实例
 * - repository: AliveRepository实例
 */
abstract class BaseFragment<VB : ViewBinding, VM : ViewModel> : Fragment() {

    /**
     * ViewBinding对象
     * 由子类通过createViewBinding()方法创建
     * 提供类型安全的View访问
     */
    protected lateinit var binding: VB

    /**
     * 该Fragment对应的ViewModel
     * 由BaseFragment通过ViewModelProvider自动创建和注入
     * 泛型类型由子类指定
     */
    protected lateinit var viewModel: VM

    /**
     * 跨Fragment共享的SharedViewModel
     * 从MainActivity中获取，用于Fragment间的数据传递
     */
    protected lateinit var sharedViewModel: SharedViewModel

    /**
     * 本地数据库实例
     * 由BaseFragment在onViewCreated中初始化
     */
    protected lateinit var database: AliveDatabase

    /**
     * 数据仓库实例
     * 由BaseFragment在onViewCreated中初始化
     * 作为ViewModel的数据访问层
     */
    protected lateinit var repository: AliveRepository

    /**
     * 创建Fragment的View
     *
     * 调用子类实现的createViewBinding()方法创建ViewBinding
     * 然后返回binding的根View
     *
     * @param inflater 布局填充器
     * @param container 父容器
     * @param savedInstanceState 保存的实例状态
     * @return binding.root
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 调用子类实现的createViewBinding()方法创建ViewBinding
        binding = createViewBinding()
        // 返回binding的根View
        return binding.root
    }

    /**
     * View创建完成后的回调
     *
     * 在此方法中执行Fragment的初始化工作：
     * 1. 初始化数据库和Repository
     * 2. 创建ViewModel并注入
     * 3. 获取SharedViewModel（用于Fragment间通信）
     * 4. 调用setupUI()设置UI
     * 5. 调用observeViewModel()观察数据
     *
     * @param view 创建的视图
     * @param savedInstanceState 保存的实例状态
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 初始化数据库和Repository
        initializeDatabase()

        // 初始化ViewModel
        initializeViewModel()

        // 从MainActivity获取SharedViewModel
        sharedViewModel = (activity as MainActivity).getSharedViewModel()

        // 调用子类实现的setupUI()设置UI
        setupUI()

        // 调用子类实现的observeViewModel()观察数据
        observeViewModel()
    }

    /**
     * 初始化数据库和Repository
     *
     * 私有方法，在onViewCreated中调用
     * 创建AliveDatabase实例和AliveRepository实例
     */
    private fun initializeDatabase() {
        database = AliveDatabase.getInstance(requireContext())
        repository = AliveRepository(
            database.aliveImageDao(),
            database.taskDao()
        )
    }

    /**
     * 初始化ViewModel
     *
     * 私有方法，在onViewCreated中调用
     * 使用AliveViewModelFactory创建指定类型的ViewModel
     * 泛型类型由子类的getViewModelClass()方法指定
     */
    private fun initializeViewModel() {
        val factory = AliveViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[getViewModelClass()]
    }

    /**
     * 创建ViewBinding实例
     *
     * 抽象方法，子类必须实现
     * 返回该Fragment对应的ViewBinding实例
     *
     * @return ViewBinding实例
     *
     * 示例实现：
     * ```kotlin
     * override fun createViewBinding(): Fragment1Binding {
     *     return Fragment1Binding.inflate(layoutInflater)
     * }
     * ```
     */
    abstract fun createViewBinding(): VB

    /**
     * 获取ViewModel的Class对象
     *
     * 抽象方法，子类必须实现
     * 返回该Fragment对应的ViewModel的Class对象
     * 用于ViewModelProvider创建ViewModel实例
     *
     * @return ViewModel的Class<VM>对象
     *
     * 示例实现：
     * ```kotlin
     * override fun getViewModelClass(): Class<Fragment1ViewModel> {
     *     return Fragment1ViewModel::class.java
     * }
     * ```
     */
    abstract fun getViewModelClass(): Class<VM>

    /**
     * 设置UI组件和事件监听器
     *
     * 抽象方法，子类必须实现
     * 在此方法中进行以下操作：
     * - 初始化UI组件
     * - 设置点击事件监听器
     * - 配置RecyclerView适配器等
     * - 其他UI相关的初始化工作
     *
     * 示例实现：
     * ```kotlin
     * override fun setupUI() {
     *     binding.btnNext.setOnClickListener {
     *         // 处理点击事件
     *     }
     *     binding.btnBack.setOnClickListener {
     *         findNavController().navigateUp()
     *     }
     * }
     * ```
     */
    abstract fun setupUI()

    /**
     * 观察ViewModel中的数据变化
     *
     * 抽象方法，子类必须实现
     * 在此方法中观察ViewModel中的LiveData或StateFlow
     * 根据数据变化更新UI或执行其他操作
     *
     * 示例实现：
     * ```kotlin
     * override fun observeViewModel() {
     *     viewModel.selectedImage.observe(viewLifecycleOwner) { image ->
     *         if (image != null) {
     *             // 更新UI
     *         }
     *     }
     *
     *     viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
     *         if (error != null) {
     *             Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
     *         }
     *     }
     * }
     * ```
     */
    abstract fun observeViewModel()
}
