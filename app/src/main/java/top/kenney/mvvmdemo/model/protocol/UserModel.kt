package top.kenney.mvvmdemo.model.protocol

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class UserModel(
    @PrimaryKey
    var id:String,
    var employeeCode:String,
    var employeeName:String,
    var token:String,
    var fid:String
){
    @Ignore
    var password:String? = null
}