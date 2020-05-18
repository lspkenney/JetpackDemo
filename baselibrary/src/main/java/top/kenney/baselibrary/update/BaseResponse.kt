package top.kenney.baselibrary.update

/**
 * Created by Administrator on 2016/7/28 0028.
 */
data class  BaseResponse<T>(var code:Int, var msg:String, var data:T){
    companion object{
        const val SUCCESS = 0
    }
}