package top.kenney.baselibrary.update

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.FragmentActivity
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.daimajia.numberprogressbar.NumberProgressBar
import okhttp3.*
import org.jetbrains.anko.toast
import top.kenney.baselibrary.BaseApplication
import top.kenney.baselibrary.activity.BaseActivity
import top.kenney.baselibrary.ext.getApkFileName
import top.kenney.baselibrary.ext.getVersionName
import top.kenney.baselibrary.net.IpV4FirstDns
import top.kenney.baselibrary.utils.NetWorkUtils
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * app版本更新
 */
class AppVersionUpdate(private val mActivity:BaseActivity, private val showNewestMessage:Boolean = false) {
    private lateinit var mUpdateVersionData:UpdateVersion
    private lateinit var mNumberProgressBar:NumberProgressBar
    private var mUpdateDialogFragment:UpdateDialogFragment? = null
    private val NEW_VERSION_APK_NAME = "newVersion.apk"
    //private val localApkPath = Environment.getext
    /**
     * 检查更新
     */
    fun checkVersion(){
        if(!NetWorkUtils.isNetWorkAvailable(mActivity)){
            return
        }
        mActivity.showLoading("正在检查版本更新")
        val mOkHttpClient = OkHttpClient.Builder()
            .dns(IpV4FirstDns())
            .build()
        val request = Request.Builder()
            .url(BaseApplication.UPDATE_VERSION_URL)
            .get()
            .build()
        val mCall = mOkHttpClient.newCall(request)
        mCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                mActivity.runOnUiThread {
                    mActivity.toast("检查版本更新失败：${e.message}")
                    mActivity.hideLoad()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()!!.string()
                mActivity.runOnUiThread {
                    try {
                        mActivity.hideLoad()
                        val base = JSON.parseObject(body, object :TypeReference<BaseResponse<UpdateVersion>>(){})
                        /*val gson = Gson()
                        val base = gson.fromJson<BaseResponse<UpdateVersion>>(body, object :TypeToken<BaseResponse<UpdateVersion>>(){}.type)*/
                        if(null != base && BaseResponse.SUCCESS == base.code){
                            //比对本地版本号
                            if(base.data == null){
                                mUpdateVersionData = UpdateVersion(mActivity.application.getVersionName(), "", "")
                            }else{
                                mUpdateVersionData = base.data
                            }
                            compareVersionWithLocal()
                        }else{
                            mActivity.runOnUiThread {
                                mActivity.toast("检查版本更新失败：${base.msg}")
                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                        mActivity.runOnUiThread {
                            mActivity.toast("检查版本更新失败：${e.message}")
                        }
                    }
                }
            }
        })
    }

    /**
     * 服务器版本与本地版本对比
     */
    private fun compareVersionWithLocal() {
        if(compareVersion(mActivity.application.getVersionName(), mUpdateVersionData.verNo) < 0){
            showUpdateDialog()
        }else{
            if(showNewestMessage){
                mActivity.runOnUiThread {
                    mActivity.toast("当前已是最新版本")
                }
            }
        }
    }

    private fun showUpdateDialog() {
        val data = Bundle()
        data.putBoolean("forceUpdate", false)
        data.putParcelable(UpdateDialogFragment.INTENT_KEY, mUpdateVersionData)
        mUpdateDialogFragment = UpdateDialogFragment.newInstance(data)
        mUpdateDialogFragment?.show(mActivity.supportFragmentManager, UpdateDialogFragment::class.java.simpleName)
        mUpdateDialogFragment?.setUpdateDialogFragmentListener(object :UpdateDialogFragmentListener{
            override fun onUpdate(nbp: NumberProgressBar) {
                mNumberProgressBar = nbp
                startDownload()
            }

            override fun onCancel() {

            }

        })
    }

    /**
     * 开始下载
     */
    private fun startDownload() {
        val mOkHttpClient = OkHttpClient.Builder()
            .dns(IpV4FirstDns())
            .build()
        val request = Request.Builder()
            .url(mUpdateVersionData.url)
            .get()
            .build()
        val mCall = mOkHttpClient.newCall(request)
        mCall.enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                mActivity.runOnUiThread {
                    mActivity.toast("下载APK文件失败：${e.message}")
                    if (mUpdateDialogFragment != null && mUpdateDialogFragment!!.isVisible) {
                        mUpdateDialogFragment?.dismiss()
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                var inputStream:InputStream? = null
                var fos:FileOutputStream? = null
                try {
                    inputStream = response.body()?.byteStream()
                    val totalSize = response.body()?.contentLength()?:0
                    var progress = 0
                    if(null != inputStream){
                        val path = mActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
                        if(path.isNullOrEmpty()){
                            mActivity.runOnUiThread {
                                mActivity.toast("初始化下载路径失败")
                                if (mUpdateDialogFragment != null && mUpdateDialogFragment!!.isVisible) {
                                    mUpdateDialogFragment?.dismiss()
                                }
                            }
                            return
                        }
                        val dir = File(path)
                        if(!dir.exists()){
                            dir.mkdirs()
                        }
                        val file = File("$path${File.separator}$NEW_VERSION_APK_NAME")
                        fos = FileOutputStream(file)
                        var count = 0
                        val buf = ByteArray(1024)
                        var readLength = 0
                        while (-1 != readLength){
                            count += readLength

                            //计算进度
                            progress = ((count.toFloat() / totalSize) * 100).toInt()
                            mActivity.runOnUiThread {
                                // 更新进度情况
                                mNumberProgressBar.progress = progress
                            }

                            fos.write(buf, 0, readLength)

                            readLength = inputStream.read(buf)
                        }

                        mActivity.runOnUiThread {
                            if (mUpdateDialogFragment != null && mUpdateDialogFragment!!.isVisible) {
                                mUpdateDialogFragment?.dismiss()
                            }
                            // 安装apk文件
                            try {
                                installApk()
                            } catch (e: Exception) {
                                mActivity.toast("无法安装应用程序")
                            }
                        }
                    }
                }catch (e:Exception){
                    mActivity.runOnUiThread {
                        mActivity.toast("下载APK文件失败：${e.message}")
                        if (mUpdateDialogFragment != null && mUpdateDialogFragment!!.isVisible) {
                            mUpdateDialogFragment?.dismiss()
                        }
                    }
                }finally {
                    try {
                        fos?.close()
                        inputStream?.close()
                    }catch (e:Exception){

                    }
                }
            }
        })
    }

    private fun installApk() {
        // 获取当前sdcard存储路径
        val path = mActivity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
        val apkfile = File("$path${File.separator}$NEW_VERSION_APK_NAME")
        if (!apkfile.exists()) {
            return
        }
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW //异步任务

        intent.setDataAndType(
            Uri.fromFile(apkfile),
            "application/vnd.android.package-archive"
        )
        mActivity.startActivity(intent)
        exitProcess(0)
    }

    private fun compareVersion(
        localVersion: String,
        serverVersion: String
    ): Int {
        if ("" == localVersion && "" == serverVersion) {
            return 0
        } else if ("" == localVersion) {
            return -1
        } else if ("" == serverVersion) {
            return 1
        }
        val varr1 = getVersionArray(localVersion)
        val varr2 = getVersionArray(serverVersion)
        val lim = Math.min(varr1.size, varr2.size)
        var k = 0
        while (k < lim) {
            if (varr1[k] == varr2[k]) {
                k++
                continue
            }
            return if (varr1[k] > varr2[k]) 1 else -1
        }
        if (varr1.size == varr2.size) {
            return 0
        }
        return if (varr1.size > varr2.size) 1 else -1
    }

    private fun getVersionArray(version: String): IntArray {
        val sarray = version.split(".").toTypedArray()
        val varray = IntArray(sarray.size)
        var k = 0
        while (k < sarray.size) {
            varray[k] = sarray[k].toInt()
            k++
        }
        return varray
    }

    companion object{
        const val TAG = "AppVersionUpdate"
    }
}