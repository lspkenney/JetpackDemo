package top.kenney.baselibrary.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import top.kenney.baselibrary.ext.HttpStatusException
import top.kenney.baselibrary.ext.UnauthorizedException
import java.net.ConnectException
import java.net.SocketTimeoutException

open class BaseApplicationVM(application: Application): AndroidViewModel(application){
    val mStateLiveData = MutableLiveData<StateActionEvent>()

    open fun onError(mStateLiveData: MutableLiveData<StateActionEvent>, e: Exception, requestCode:Int = 0) {
        when(e){
            is ConnectException -> mStateLiveData.value = LoadErrorState("网络连接失败，请检查网络连接或稍后重试", ERROR_CODE_CONNECT_EXCEPTION, requestCode)
            is SocketTimeoutException -> mStateLiveData.value = LoadErrorState("连接后台服务超时，请检查网络连接或稍后重试", ERROR_CODE_SOCKET_TIMEOUT_EXCEPTION, requestCode)
            is HttpStatusException -> mStateLiveData.value = LoadErrorState(e.message, ERROR_CODE_HTTP_STATUS_EXCEPTION, requestCode)
            is UnauthorizedException -> mStateLiveData.value = LoadErrorState("登录信息已过期或无权限访问", ERROR_CODE_UNAUTHORIZED_EXCEPTION, requestCode)
            else  -> mStateLiveData.value = LoadErrorState(e.message, ERROR_CODE_OTHER_EXCEPTION, requestCode)
        }
    }

    companion object{
        const val ERROR_CODE_CONNECT_EXCEPTION = 1
        const val ERROR_CODE_SOCKET_TIMEOUT_EXCEPTION = 2
        const val ERROR_CODE_HTTP_STATUS_EXCEPTION = 3
        const val ERROR_CODE_UNAUTHORIZED_EXCEPTION = 4
        const val ERROR_CODE_OTHER_EXCEPTION = 5
    }
}