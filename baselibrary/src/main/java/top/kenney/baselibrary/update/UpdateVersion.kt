package top.kenney.baselibrary.update

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Kenney on 2019-02-28 11:58
 */
@Parcelize
data class UpdateVersion(val verNo: String, val content: String, val url:String) : Parcelable