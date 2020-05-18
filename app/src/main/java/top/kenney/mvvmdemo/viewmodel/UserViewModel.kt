package top.kenney.mvvmdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import top.kenney.mvvmdemo.base.*
import top.kenney.mvvmdemo.model.protocol.LoginRequest
import top.kenney.mvvmdemo.model.protocol.UserModel
import top.kenney.mvvmdemo.model.repository.UserRepository

class UserViewModel(private val mUserRepository: UserRepository):ViewModel() {

    val loginRequest = MutableLiveData(LoginRequest("admin-1", "123456"))

    val mUserModel = MutableLiveData<BaseResp<UserModel>?>()

    val mStateLiveData = MutableLiveData<StateActionEvent>()

    fun login(){
        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadStartState("登录中...")
                mUserModel.value = mUserRepository.login(loginRequest.value!!.username, loginRequest.value!!.password).body()
                mStateLiveData.value = LoadCompleteState()
            }catch (e: Exception){
                mStateLiveData.value = LoadErrorState(e.message)
            }
        }
    }

    fun saveUser(user:UserModel){
        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadStartState("保存用户信息中...")
                mUserRepository.saveUser(user)
                mStateLiveData.value = LoadCompleteState(requestCode = 2)
            }catch (e: Exception){
                mStateLiveData.value = LoadErrorState(e.message)
            }
        }
    }
}