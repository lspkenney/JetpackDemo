package top.kenney.baselibrary.net

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import top.kenney.baselibrary.BaseApplication
import java.util.concurrent.TimeUnit

/**
 * Created by Kenney on 2018-12-07 14:22
 */
class RetrofitFactory private constructor() {
    companion object {
        val instance: RetrofitFactory by lazy { RetrofitFactory() }
    }

    private val retrofit: Retrofit
    private val interceptor: Interceptor

    init {

        interceptor = Interceptor { chain ->
            /*val isLoginUrl = chain.request().url().url().toString().contains("account/login")
            if(!isLoginUrl){
                val token = AppPrefsUtils.getString(BaseConstant.KEY_SP_TOKEN)
                Log.i("OkHttp", "token = $token")
                val request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", token)
                    .addHeader("DeviceType", "PDA")
                    .build()
                chain.proceed(request)
            }else{*/
                val request = chain.request()
                    .newBuilder()
                    .addHeader("DeviceType", "PDA")
                    .build()
                chain.proceed(request)
            //}

        }
        retrofit = Retrofit.Builder()
                .baseUrl(BaseApplication.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(initClient())
                .build()
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .dns(IpV4FirstDns())
                .addInterceptor(initLogInterceptor())
                .addInterceptor(interceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()
    }

    private fun initLogInterceptor(): Interceptor {
        val logging = HttpLoggingInterceptor(OkHttpLogger())
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

}