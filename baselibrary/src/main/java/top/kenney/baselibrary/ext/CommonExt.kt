package top.kenney.baselibrary.ext

import android.view.View

fun View.setVisible(isVisible:Boolean){
    this.visibility = if(isVisible) View.VISIBLE else View.GONE
}

fun getApkFileName(path:String):String{
    if(path.isNullOrEmpty())
        return ""
    val lastIndexOf = path.lastIndexOf("/")
    if(-1 == lastIndexOf || lastIndexOf>= path.length)
        return ""
    return path.substring(lastIndexOf + 1)
}