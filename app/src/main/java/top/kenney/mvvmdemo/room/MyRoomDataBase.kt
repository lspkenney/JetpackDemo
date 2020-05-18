package top.kenney.mvvmdemo.room

import androidx.room.Database
import androidx.room.RoomDatabase
import top.kenney.mvvmdemo.model.protocol.UserModel
import top.kenney.mvvmdemo.model.repository.dao.UserModelDao

@Database(entities = [UserModel::class], version = 1)
abstract class MyRoomDataBase: RoomDatabase() {
    abstract fun getUserDao():UserModelDao
}