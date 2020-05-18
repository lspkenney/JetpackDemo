package top.kenney.baselibrary.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ktx.immersionBar
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupStatus
import com.lxj.xpopup.impl.LoadingPopupView
import com.lxj.xpopup.util.KeyboardUtils
import org.jetbrains.anko.toast
import top.kenney.baselibrary.utils.LogUtil

abstract class BaseActivity:AppCompatActivity() {
    lateinit var loadingPopup: LoadingPopupView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLayoutContent()
    }

    open fun setLayoutContent(){
        setContentView(getLayoutId())
        initImmersionBar()
        initPopupView()
        //初始化UI
        initViews()
        //加载数据
        loadData()
    }

    /**
     * 沉浸式状态栏设置
     * 通过重写getTitleBar设置titlebar，默认getTitleBar返回null则不采用沉浸式
     */
    open fun initImmersionBar() {
        if(null != getTitleBar()){
            immersionBar {
                titleBar(getTitleBar())
            }
        }
    }

    fun initPopupView() {
        loadingPopup = XPopup.Builder(this)
            .dismissOnBackPressed(false)
            .dismissOnTouchOutside(false)
            .asLoading("加载数据中...")
    }

    /**
     * 初始化UI
     */
    abstract fun initViews()
    /**
     * 加载数据
     */
    abstract fun loadData()
    /**
     * layoutId
     */
    abstract fun getLayoutId():Int

    open fun getTitleBar(): View? = null

    open fun showError(msg:String?){
        toast("${msg?:"未返回错误信息"}")
    }

    open fun showLoading(msg:String?) {
        if(!msg.isNullOrBlank()){
            loadingPopup.setTitle(msg)
        }
        if(loadingPopup.popupStatus != PopupStatus.Showing){
            loadingPopup.show()
        }
    }

    open fun hideLoad() {
        try {
            loadingPopup.delayDismiss(0)
        }catch (e:Exception){
            e.printStackTrace()
            toast(e.message?:"exception")
        }

    }

    companion object{
        const val TAG = "BaseActivity"
    }
}