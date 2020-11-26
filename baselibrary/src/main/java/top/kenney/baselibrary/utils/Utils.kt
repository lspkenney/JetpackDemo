package top.kenney.baselibrary.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import java.lang.reflect.Method

object Utils {
    /**
     * 获取设备唯一标识
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    fun getSerialNumber():String {
        var serial: String? = null
        try {
            serial = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                // 9.0 +
                Build.getSerial()
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                // 8.0 +
                Build.SERIAL
            } else {
                // 8.0 -
                val c = Class.forName("android.os.SystemProperties")
                val get: Method = c.getMethod("get", String::class.java)
                get.invoke(c, "ro.serialno")?.toString()?:""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return serial?:""
    }
}