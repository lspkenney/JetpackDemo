package top.kenney.baselibrary.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseVMActivity<BD: ViewDataBinding, VM:ViewModel>:
    BaseActivity() {
    lateinit var mViewModel:VM
    lateinit var mBinding:BD

    override fun setLayoutContent() {
        initPopupView()
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initImmersionBar()
        //初始化ViewModel
        mViewModel = initViewModel()
        //如果DataBinding 有ViewModel则初始化
        initBindingViewModel()
        //观察数据
        observeData()
        //初始化UI
        initViews()
        //加载数据
        loadData()
    }

    /**
     * 初始化ViewModel
     */
    abstract fun initViewModel():VM
    /**
     * 如果DataBinding 有ViewModel则初始化
     */
    abstract fun initBindingViewModel()
    /**
     * 观察数据
     */
    abstract fun observeData()
}