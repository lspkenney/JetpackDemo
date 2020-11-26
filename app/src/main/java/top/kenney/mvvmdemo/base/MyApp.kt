package top.kenney.mvvmdemo.base

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import top.kenney.baselibrary.BaseApplication
import top.kenney.mvvmdemo.BuildConfig
import top.kenney.mvvmdemo.koin.appModule
class MyApp:BaseApplication() {
    override fun initBuglyAppId(): String {
        return  ""
    }

    override fun initBaseUrl(): String {
        return BuildConfig.baseUrl
    }

    override fun initUpdateVersionUrl(): String {
        return BuildConfig.updateVersionUrl
    }

    override fun initKoin() {
        startKoin {
            androidContext(this@MyApp)
            modules(appModule)
        }
    }
}