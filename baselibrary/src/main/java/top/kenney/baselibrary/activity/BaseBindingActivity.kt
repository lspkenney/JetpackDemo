package top.kenney.baselibrary.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBindingActivity<BD: ViewDataBinding>:
    BaseActivity() {
    lateinit var mBinding:BD

    override fun setLayoutContent(savedInstanceState: Bundle?) {
        initPopupView()
        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initImmersionBar()
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
     * 如果DataBinding 有ViewModel则初始化
     */
    abstract fun initBindingViewModel()
    /**
     * 观察数据
     */
    abstract fun observeData()
}