package top.kenney.baselibrary.net

import android.util.Log
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by Kenney on 2020-03-27 11:10
 */
class OkHttpLogger: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        Log.i("OkHttp", message)
    }
}