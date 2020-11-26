package top.kenney.baselibrary.net.base

class BaseResp<T>(val code:Int, val message:String, val data:T?){
    fun success():Boolean{
        return code == OK_CODE
    }

    companion object{
        const val OK_CODE = 0
    }
}