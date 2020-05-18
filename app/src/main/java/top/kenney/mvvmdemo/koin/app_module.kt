package top.kenney.mvvmdemo.koin

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import top.kenney.baselibrary.net.RetrofitFactory
import top.kenney.mvvmdemo.common.Constants
import top.kenney.mvvmdemo.model.api.UserApi
import top.kenney.mvvmdemo.model.repository.UserRepository
import top.kenney.mvvmdemo.room.MyRoomDataBase
import top.kenney.mvvmdemo.viewmodel.MainViewModel
import top.kenney.mvvmdemo.viewmodel.UserViewModel

//所有ViewModel
val viewModelModule = module {
    viewModel { UserViewModel(get()) }
    viewModel { MainViewModel(get()) }
}

//所有Repository
val repoModule = module {
    factory { UserRepository(get(), get()) }
}

//所有Api相关
val apiModule = module {
    single { RetrofitFactory.instance.create(UserApi::class.java) }
}

//数据库相关
val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), MyRoomDataBase::class.java, Constants.BD_NAME).build()
    }

    single { get<MyRoomDataBase>().getUserDao() }
}

val appModule = listOf(viewModelModule, repoModule, apiModule, databaseModule)