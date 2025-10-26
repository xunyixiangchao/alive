package com.example.alive.ui.viewmodel

import androidx.lifecycle.ViewModel

/**
 * 主菜单ViewModel
 *
 * 职责：
 * - 作为HomeFragment的ViewModel
 * - 由于HomeFragment是一个简单的菜单界面，此ViewModel不包含任何业务逻辑
 * - 主要目的是满足BaseFragment的泛型参数要求
 *
 * 技术说明：
 * - HomeFragment不需要与数据库或API交互
 * - 所有的导航逻辑都在Fragment中处理
 * - 此ViewModel为空实现，可在将来扩展功能时添加逻辑
 */
class HomeViewModel : ViewModel() {
    // 此ViewModel为空实现
}
