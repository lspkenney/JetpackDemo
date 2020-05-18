package top.kenney.mvvmdemo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import top.kenney.mvvmdemo.base.LoadCompleteState
import top.kenney.mvvmdemo.base.LoadErrorState
import top.kenney.mvvmdemo.base.LoadStartState
import top.kenney.mvvmdemo.base.StateActionEvent
import top.kenney.mvvmdemo.model.protocol.UserModel
import top.kenney.mvvmdemo.model.repository.UserRepository

class MainViewModel(private val mUserRepository: UserRepository):ViewModel() {
    val mStateLiveData = MutableLiveData<StateActionEvent>()
    var mDataList = MutableLiveData<MutableList<UserModel>>()

    fun loadAllUserData(isLoadMore:Boolean = false){
        viewModelScope.launch {
            try {
                mStateLiveData.value = LoadStartState("查询数据中...")
                val data = mUserRepository.loadAll()
                if(isLoadMore){
                    val list = mDataList.value?: mutableListOf()
                    list.addAll(data)
                    mDataList.value = list
                }else{
                    mDataList.value = data
                }
                mStateLiveData.value = LoadCompleteState()
            }catch (e: Exception){
                mStateLiveData.value = LoadErrorState(e.message)
            }
        }
    }
}