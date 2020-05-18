package top.kenney.baselibrary.ext

import android.app.Application

fun Application.getVersionName(): String {
    return packageManager.getPackageInfo(packageName, 0).versionName
}