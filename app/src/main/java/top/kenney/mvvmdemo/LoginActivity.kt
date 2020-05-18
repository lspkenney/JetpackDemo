package top.kenney.mvvmdemo

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.getViewModel
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import top.kenney.baselibrary.activity.BaseVMActivity
import top.kenney.baselibrary.update.AppVersionUpdate
import top.kenney.mvvmdemo.base.LoadCompleteState
import top.kenney.mvvmdemo.base.LoadStartState
import top.kenney.mvvmdemo.databinding.ActivityLoginBinding
import top.kenney.mvvmdemo.viewmodel.UserViewModel
import kotlin.random.Random

class LoginActivity : BaseVMActivity<ActivityLoginBinding, UserViewModel>(),EasyPermissions.PermissionCallbacks {
    override fun observeData() {
        mViewModel.mStateLiveData.observe(this, Observer{
            if(it is LoadStartState){
                showLoading(it.msg)
            }else if(it is LoadCompleteState){
                hideLoad()
                if(2 == it.requestCode){//保存成功
                    startActivity<MainActivity>()
                    finish()
                }
            }else{
                hideLoad()
                showError(it.msg)
            }
        })

        mViewModel.mUserModel.observe(this, Observer {
            if(null == it){
                showError("data is empty")
                return@Observer
            }
            if(it.success()){
                showError("登录成功")
                it.data.id = "${Random.nextInt(100)}"
                mViewModel.saveUser(it.data)
            }else{
                showError("登录失败：${it.message}")
            }
        })
    }

    override fun initViews() {
        mBinding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        if(mViewModel.loginRequest.value!!.username.isNullOrEmpty()){
            toast("请输入用户名")
            return
        }
        if(mViewModel.loginRequest.value!!.password.isNullOrEmpty()){
            toast("请输入密码")
            return
        }
        mViewModel.login()
    }

    override fun loadData() {
        AppVersionUpdate(this).checkVersion()
    }

    override fun initViewModel() = getViewModel<UserViewModel>()

    override fun initBindingViewModel() {
        mBinding.mViewModel = mViewModel
    }

    override fun getLayoutId() = R.layout.activity_login

    override fun getTitleBar() = mBinding.mAppBarLayout

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION)
    fun checkPermission(){
        if(!EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS)){
            EasyPermissions.requestPermissions(this, "请授予权限，否则影响部分使用功能", REQUEST_CODE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermission()
    }

    /**
     * 申请拒绝时调用
     */
    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        /**
         * 若是在权限弹窗中，用户勾选了'不在提示'，且拒绝权限。
         * 这时候，需要跳转到设置界面去，让用户手动开启。
         */
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setTitle("请授予权限")
                .setRationale("请授予权限，否则影响部分使用功能")
                .build().show();
        }
    }

    /**
     * 申请成功时调用
     */
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //从设置页面返回，判断权限是否申请。
            if (EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_SMS)) {
                toast("权限申请成功!")
            } else {
                toast("权限申请失败!")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    companion object{
        const val REQUEST_CODE_PERMISSION = 100
    }
}
