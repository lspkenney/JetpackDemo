package top.kenney.mvvmdemo

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.gyf.immersionbar.ktx.immersionBar
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.getViewModel
import top.kenney.baselibrary.activity.BaseVMActivity
import top.kenney.mvvmdemo.adapter.UserAdapter
import top.kenney.mvvmdemo.base.LoadCompleteState
import top.kenney.mvvmdemo.base.LoadStartState
import top.kenney.mvvmdemo.databinding.ActivityMainBinding
import top.kenney.mvvmdemo.viewmodel.MainViewModel

class MainActivity:
    BaseVMActivity<ActivityMainBinding, MainViewModel>(), OnRefreshListener,
    OnLoadMoreListener {
    private lateinit var mAdapter:UserAdapter
    override fun observeData() {

        mViewModel.mStateLiveData.observe(this, Observer{
            if(it is LoadStartState){
                //showLoading(it.msg)
            }else if(it is LoadCompleteState){
                completeLoad()
            }else{
                completeLoad()
                showError(it.msg)
            }
        })

        mViewModel.mDataList.observe(this, Observer {
            if(null == it)
                return@Observer
            mAdapter.setList(it)
            toast("size = ${it.size}")
        })
    }

    override fun initViews() {
        mAdapter = UserAdapter()
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        mBinding.mRecyclerView.addItemDecoration(itemDecoration)
        mBinding.mRecyclerView.adapter = mAdapter

        mBinding.mRefreshLayout.setEnableRefresh(true)
        mBinding.mRefreshLayout.setEnableLoadMore(true)
        mBinding.mRefreshLayout.setRefreshHeader(MaterialHeader(this))
        mBinding.mRefreshLayout.setRefreshFooter(ClassicsFooter(this))
        mBinding.mRefreshLayout.setEnableLoadMore(true)
        mBinding.mRefreshLayout.setOnRefreshListener(this)
        mBinding.mRefreshLayout.setOnLoadMoreListener(this)

    }

    override fun loadData() {
        mBinding.mRefreshLayout.autoRefresh()
    }

    override fun getLayoutId() = R.layout.activity_main
    override fun initViewModel() = getViewModel<MainViewModel>()

    override fun initBindingViewModel() {

    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.loadAllUserData()
        //refreshLayout.finishRefresh()
    }

    private fun completeLoad(){
        mBinding.mRefreshLayout.finishRefresh()
        mBinding.mRefreshLayout.finishLoadMore()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        mViewModel.loadAllUserData(isLoadMore = true)
        //refreshLayout.finishLoadMore()
    }

    override fun getTitleBar() = mBinding.mAppBarLayout
}