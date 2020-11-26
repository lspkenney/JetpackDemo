package top.kenney.baselibrary.ext

import android.app.Application
import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import retrofit2.Response

fun <T> Response<T>.convert():T{
    if(401 == this.code()){
        throw UnauthorizedException()
    }
    val body = this.body()
    if(null != body) {
        return body
    }
    throw HttpStatusException(this.code())
}

fun View.setVisible(isVisible:Boolean){
    this.visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun EditText.itemContent():String{
    return this.text.toString().trim()
}

fun getApkFileName(path:String):String{
    if(path.isNullOrEmpty())
        return ""
    val lastIndexOf = path.lastIndexOf("/")
    if(-1 == lastIndexOf || lastIndexOf>= path.length)
        return ""
    return path.substring(lastIndexOf + 1)
}

/**
 * 获取版本号
 */
fun Context.getVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}
