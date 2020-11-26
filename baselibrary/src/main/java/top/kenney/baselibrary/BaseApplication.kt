package top.kenney.baselibrary

import android.app.Application
import android.os.Process
import android.text.TextUtils
import com.lxj.xpopup.XPopup
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


/**
 * Created by Kenney on 2018-12-11 17:13
 */
abstract class BaseApplication : Application() {

    companion object {
        lateinit var context: Application
        var BASE_URL = ""
        var UPDATE_VERSION_URL = ""
        var TOKEN = ""
    }
    override fun onCreate() {
        super.onCreate()
        context = this
        BASE_URL = initBaseUrl()
        UPDATE_VERSION_URL = initUpdateVersionUrl()
        initKoin()

        XPopup.setPrimaryColor(resources.getColor(R.color.colorPrimary))

        initBugly()
    }

    private fun initBugly() {
        // 获取当前包名
        val packageName: String = applicationContext.packageName
        // 获取当前进程名
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(applicationContext)
        strategy.isUploadProcess = processName == null || processName == packageName
        // 初始化Bugly
        CrashReport.initCrashReport(applicationContext, initBuglyAppId(), BuildConfig.DEBUG, strategy)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

    /**
     * bugly appid
     */
    abstract fun initBuglyAppId(): String

    /**
     * 设置baseUrl
     */
    abstract fun initBaseUrl(): String
    /**
     * 设置版本更新url
     */
    abstract fun initUpdateVersionUrl(): String

    /**
     * 初始化依赖注入框架koin
     */
    abstract fun initKoin()

    /**
     * 设置请求Token
     */
    fun setRequestToken(token: String = ""){
        TOKEN = token
    }
}