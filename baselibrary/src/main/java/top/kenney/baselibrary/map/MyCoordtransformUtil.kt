package top.kenney.baselibrary.map

import java.text.DecimalFormat

/**
 * 坐标转换
 * 一个提供了百度坐标（BD09）、国测局坐标（火星坐标，GCJ02）、和WGS84坐标系之间的转换的工具类
 * 参考https://github.com/wandergis/coordtransform 写的Java版本
 */
object MyCoordtransformUtil {
    private const val x_PI = 3.14159265358979324 * 3000.0 / 180.0
    private const val PI = 3.1415926535897932384626
    private const val a = 6378245.0
    private const val ee = 0.00669342162296594323

    /**
     * 百度坐标系 (BD-09) 与 火星坐标系 (GCJ-02)的转换
     * 即 百度 转 谷歌、高德
     * @param bd_lon
     * @param bd_lat
     * @return Double[lon,lat]
     */
    fun BD09ToGCJ02(bd_lon: Double, bd_lat: Double): Array<Double?> {
        val x = bd_lon - 0.0065
        val y = bd_lat - 0.006
        val z =
            Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_PI)
        val theta =
            Math.atan2(y, x) - 0.000003 * Math.cos(x * x_PI)
        val arr = arrayOfNulls<Double>(2)
        arr[0] = z * Math.cos(theta)
        arr[1] = z * Math.sin(theta)
        return arr
    }

    /**
     * 火星坐标系 (GCJ-02) 与百度坐标系 (BD-09) 的转换
     * 即谷歌、高德 转 百度
     * @param gcj_lon
     * @param gcj_lat
     * @return Double[lon,lat]
     */
    fun GCJ02ToBD09(gcj_lon: Double, gcj_lat: Double): Array<Double?> {
        val z =
            Math.sqrt(gcj_lon * gcj_lon + gcj_lat * gcj_lat) + 0.00002 * Math.sin(
                gcj_lat * x_PI
            )
        val theta = Math.atan2(
            gcj_lat,
            gcj_lon
        ) + 0.000003 * Math.cos(gcj_lon * x_PI)
        val arr = arrayOfNulls<Double>(2)
        arr[0] = z * Math.cos(theta) + 0.0065
        arr[1] = z * Math.sin(theta) + 0.006
        return arr
    }

    /**
     * WGS84转GCJ02
     * @param wgs_lon
     * @param wgs_lat
     * @return Double[lon,lat]
     */
    fun WGS84ToGCJ02(wgs_lon: Double, wgs_lat: Double): Array<Double?> {
        if (outOfChina(wgs_lon, wgs_lat)) {
            return arrayOf(wgs_lon, wgs_lat)
        }
        var dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0)
        var dlng = transformlng(wgs_lon - 105.0, wgs_lat - 35.0)
        val radlat = wgs_lat / 180.0 * PI
        var magic = Math.sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = Math.sqrt(magic)
        dlat =
            dlat * 180.0 / (a * (1 - ee) / (magic * sqrtmagic) * PI)
        dlng =
            dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI)
        val arr = arrayOfNulls<Double>(2)
        val decimalFormat = DecimalFormat("#.000000")
        arr[0] = decimalFormat.format(wgs_lon + dlng).toDouble()
        arr[1] = decimalFormat.format(wgs_lat + dlat).toDouble()
        return arr
    }

    /**
     * WGS84转GCJ02
     * @param wgs_lon
     * @param wgs_lat
     * @return String lon,lat
     */
    fun WGS84ToGCJ02String(wgs_lon: Double, wgs_lat: Double): String {
        if (outOfChina(wgs_lon, wgs_lat)) {
            return "$wgs_lon,$wgs_lat"
        }
        var dlat = transformlat(wgs_lon - 105.0, wgs_lat - 35.0)
        var dlng = transformlng(wgs_lon - 105.0, wgs_lat - 35.0)
        val radlat = wgs_lat / 180.0 * PI
        var magic = Math.sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = Math.sqrt(magic)
        dlat =
            dlat * 180.0 / (a * (1 - ee) / (magic * sqrtmagic) * PI)
        dlng =
            dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI)
        val arr = arrayOfNulls<Double>(2)
        val decimalFormat = DecimalFormat("#.000000")
        arr[0] = decimalFormat.format(wgs_lon + dlng).toDouble()
        arr[1] = decimalFormat.format(wgs_lat + dlat).toDouble()
        return return "${arr[0]},${arr[1]}"
    }

    /**
     * GCJ02转WGS84
     * @param gcj_lon
     * @param gcj_lat
     * @return Double[lon,lat]
     */
    fun GCJ02ToWGS84(gcj_lon: Double, gcj_lat: Double): Array<Double> {
        if (outOfChina(gcj_lon, gcj_lat)) {
            return arrayOf(gcj_lon, gcj_lat)
        }
        var dlat = transformlat(gcj_lon - 105.0, gcj_lat - 35.0)
        var dlng = transformlng(gcj_lon - 105.0, gcj_lat - 35.0)
        val radlat = gcj_lat / 180.0 * PI
        var magic = Math.sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = Math.sqrt(magic)
        dlat =
            dlat * 180.0 / (a * (1 - ee) / (magic * sqrtmagic) * PI)
        dlng =
            dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI)
        val mglat = gcj_lat + dlat
        val mglng = gcj_lon + dlng
        val decimalFormat = DecimalFormat("#.000000")
        return arrayOf(decimalFormat.format(gcj_lon * 2 - mglng).toDouble(),
            decimalFormat.format(gcj_lat * 2 - mglat).toDouble())
    }

    /**
     * GCJ02转WGS84
     * @param gcj_lon
     * @param gcj_lat
     * @return String lon,lat
     */
    fun GCJ02ToWGS84String(gcj_lon: Double, gcj_lat: Double): String {
        if (outOfChina(gcj_lon, gcj_lat)) {
            return "$gcj_lon,$gcj_lat"
        }
        var dlat = transformlat(gcj_lon - 105.0, gcj_lat - 35.0)
        var dlng = transformlng(gcj_lon - 105.0, gcj_lat - 35.0)
        val radlat = gcj_lat / 180.0 * PI
        var magic = Math.sin(radlat)
        magic = 1 - ee * magic * magic
        val sqrtmagic = Math.sqrt(magic)
        dlat =
            dlat * 180.0 / (a * (1 - ee) / (magic * sqrtmagic) * PI)
        dlng =
            dlng * 180.0 / (a / sqrtmagic * Math.cos(radlat) * PI)
        val mglat = gcj_lat + dlat
        val mglng = gcj_lon + dlng
        val decimalFormat = DecimalFormat("#.000000")
        return "${decimalFormat.format(gcj_lon * 2 - mglng).toDouble()},${decimalFormat.format(gcj_lat * 2 - mglat).toDouble()}"
    }

    private fun transformlat(lng: Double, lat: Double): Double {
        var ret =
            -100.0 + 2.0 * lng + 3.0 * lat + 0.2 * lat * lat + 0.1 * lng * lat + 0.2 * Math.sqrt(
                Math.abs(lng)
            )
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(
            2.0 * lng * PI
        )) * 2.0 / 3.0
        ret += (20.0 * Math.sin(lat * PI) + 40.0 * Math.sin(lat / 3.0 * PI)) * 2.0 / 3.0
        ret += (160.0 * Math.sin(lat / 12.0 * PI) + 320 * Math.sin(
            lat * PI / 30.0
        )) * 2.0 / 3.0
        return ret
    }

    private fun transformlng(lng: Double, lat: Double): Double {
        var ret =
            300.0 + lng + 2.0 * lat + 0.1 * lng * lng + 0.1 * lng * lat + 0.1 * Math.sqrt(
                Math.abs(lng)
            )
        ret += (20.0 * Math.sin(6.0 * lng * PI) + 20.0 * Math.sin(
            2.0 * lng * PI
        )) * 2.0 / 3.0
        ret += (20.0 * Math.sin(lng * PI) + 40.0 * Math.sin(lng / 3.0 * PI)) * 2.0 / 3.0
        ret += (150.0 * Math.sin(lng / 12.0 * PI) + 300.0 * Math.sin(
            lng / 30.0 * PI
        )) * 2.0 / 3.0
        return ret
    }

    /**
     * outOfChina
     * @描述: 判断是否在国内，不在国内则不做偏移
     * @param lng
     * @param lat
     * @return {boolean}
     */
    private fun outOfChina(lng: Double, lat: Double): Boolean {
        return lng < 72.004 || lng > 137.8347 || lat < 0.8293 || lat > 55.8271
    }
}
