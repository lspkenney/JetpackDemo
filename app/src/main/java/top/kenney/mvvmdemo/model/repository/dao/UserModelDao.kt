package top.kenney.mvvmdemo.model.repository.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import top.kenney.mvvmdemo.model.protocol.UserModel

@Dao
interface UserModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(user:UserModel)

    @Query("select * from usermodel")
    suspend fun loadAll():MutableList<UserModel>
}