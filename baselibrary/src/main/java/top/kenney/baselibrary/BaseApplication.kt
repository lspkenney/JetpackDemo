package top.kenney.baselibrary

import android.app.Application


/**
 * Created by Kenney on 2018-12-11 17:13
 */
abstract class BaseApplication : Application() {

    companion object {
        var BASE_URL = ""
        var UPDATE_VERSION_URL = ""
    }
    override fun onCreate() {
        super.onCreate()
        BASE_URL = initBaseUrl()
        UPDATE_VERSION_URL = initUpdateVersionUrl()
        initKoin()
    }

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
}