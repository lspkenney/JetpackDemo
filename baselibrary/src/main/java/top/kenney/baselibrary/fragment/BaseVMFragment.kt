package top.kenney.baselibrary.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

abstract class BaseVMFragment<BD: ViewDataBinding, VM: ViewModel>: BaseFragment() {
    lateinit var mViewModel:VM
    lateinit var mBinding:BD
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mBinding.root
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

    override fun onLazyAfterView() {
        //初始化ViewModel
        mViewModel = initViewModel()
        //如果DataBinding 有ViewModel则初始化
        initBindingViewModel()
        //初始化UI
        initViews()
        //观察数据
        observeData()
        //加载数据
        loadData()
    }
}