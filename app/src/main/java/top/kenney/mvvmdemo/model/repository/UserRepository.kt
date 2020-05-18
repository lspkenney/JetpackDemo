package top.kenney.mvvmdemo.model.repository

import top.kenney.mvvmdemo.model.api.UserApi
import top.kenney.mvvmdemo.model.protocol.UserModel
import top.kenney.mvvmdemo.model.repository.dao.UserModelDao

/**
 * Created by Kenney on 2020-03-10 10:56
 * 用户信息
 */
class UserRepository(private val mUserApi:UserApi, private val mUserDao: UserModelDao){
    /**
     * 登录
     */
    suspend fun login(username:String, password:String)
            = mUserApi.login(username, password)

    /**
     * 保存用户信息
     */
    suspend fun saveUser(user:UserModel) = mUserDao.save(user)

    /**
     * 查询所有用户信息
     */
    suspend fun loadAll() = mUserDao.loadAll()
}