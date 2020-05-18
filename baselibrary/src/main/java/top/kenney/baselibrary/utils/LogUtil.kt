package top.kenney.baselibrary.utils

import android.util.Log
import top.kenney.baselibrary.BuildConfig

/**
 * Created by Administrator on 2016/9/21 0021.
 */
object LogUtil {
    fun i(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun v(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun e(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun d(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun w(tag: String?, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg)
        }
    }

    fun w(tag: String?, tr: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, tr)
        }
    }

    fun w(tag: String?, msg: String?, tr: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, msg, tr)
        }
    }
}