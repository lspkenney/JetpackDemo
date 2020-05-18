package top.kenney.baselibrary.update

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.lib_update_app_dialog.*
import top.kenney.baselibrary.R
import top.kenney.baselibrary.ext.setVisible
import kotlin.Exception


/**
 * Created by Kenney on 2019-02-28 11:27
 */
class UpdateDialogFragment: DialogFragment(), View.OnClickListener {

    private var mDefaultColor = R.color.update_btn_normal
    private var mDefaultPicResId = R.drawable.lib_update_app_top_bg
    private var mUpdateVersion: UpdateVersion?=null
    private var mUpdateDialogFragmentListener: UpdateDialogFragmentListener? = null
    private var isForceUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShow = true
        setStyle(STYLE_NO_TITLE, R.style.UpdateAppDialog)
    }

    fun setUpdateDialogFragmentListener(mUpdateDialogFragmentListener: UpdateDialogFragmentListener){
        this.mUpdateDialogFragmentListener = mUpdateDialogFragmentListener
    }

    override fun onStart() {
        super.onStart()
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
        val dialogWindow = dialog?.window
        dialogWindow?.setGravity(Gravity.CENTER)
        val lp = dialogWindow?.attributes
        val displayMetrics = context!!.resources.displayMetrics
        lp?.height = (displayMetrics.heightPixels * 0.8f).toInt()
        lp?.width = (displayMetrics.widthPixels * 0.9f).toInt()
        dialogWindow?.attributes = lp
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.lib_update_app_dialog, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    private fun initData() {
        mUpdateVersion = arguments?.getParcelable<UpdateVersion>(INTENT_KEY)
        isForceUpdate = arguments?.getBoolean("forceUpdate")?:false
        //设置主题色
        initTheme()

        if(mUpdateVersion != null){

            if(!isForceUpdate){
                tv_title.text = "是否升级到${mUpdateVersion!!.verNo}版本？"
            }else{
                tv_title.text = "正在升级到${mUpdateVersion!!.verNo}版本"
            }
            tv_update_info.text = mUpdateVersion!!.content
            initEvents()
        }
    }

    private fun initEvents() {
        btn_ok.setOnClickListener(this)
        iv_close.setOnClickListener(this)
        if(isForceUpdate){
            npb.setVisible(true)
            btn_ok.setVisible(false)
            ll_close.setVisible(false)
            mUpdateDialogFragmentListener?.onUpdate(npb)
        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_ok -> {
                //dismiss()
                tv_title.text = "正在升级到${mUpdateVersion!!.verNo}版本"
                npb.setVisible(true)
                btn_ok.setVisible(false)
                ll_close.setVisible(false)
                mUpdateDialogFragmentListener?.onUpdate(npb)
            }
            R.id.iv_close -> {
                dismiss()
                mUpdateDialogFragmentListener?.onCancel()
            }
        }
    }

    fun setForceUpdate(){
        npb.setVisible(true)
        btn_ok.setVisible(false)
        ll_close.setVisible(false)
        mUpdateDialogFragmentListener?.onUpdate(npb)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            if(manager.isDestroyed) return
        }
        try {
            super.show(manager, tag)
        }catch (e:Exception){

        }
    }

    override fun onDestroyView() {
        isShow = false
        super.onDestroyView()
    }

    private fun initTheme() {
        setDialogTheme(mDefaultColor, mDefaultPicResId)
    }

    private fun setDialogTheme(color: Int, topResId: Int) {
        iv_top.setImageResource(topResId)
        //btn_ok.setBackgroundDrawable(DrawableUtil.getDrawable(DensityUtil.dip2px(activity, 4.0f), color))
        //随背景颜色变化
        //btn_ok.setTextColor(if (ColorUtil.isTextColorDark(color)) Color.BLACK else Color.WHITE)
    }

    companion object {
        val INTENT_KEY = "UpdateDialogFragment"
        var isShow = false
        fun newInstance(args: Bundle?): UpdateDialogFragment {
            val fragment = UpdateDialogFragment()
            if (args != null) {
                fragment.arguments = args
            }
            return fragment
        }
    }
}