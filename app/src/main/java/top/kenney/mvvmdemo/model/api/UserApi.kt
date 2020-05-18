package top.kenney.mvvmdemo.model.api

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import top.kenney.mvvmdemo.base.BaseResp
import top.kenney.mvvmdemo.model.protocol.UserModel

interface UserApi {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("account/login")
    suspend fun login(@Field("username") username:String, @Field("password") password:String)
            : Response<BaseResp<UserModel>>
}