package top.kenney.baselibrary.net.base

class BasePageRecordResp<T>(val code:Int, val message:String, val data:PageRecordData<T>?){
    fun success():Boolean{
        return code == OK_CODE
    }

    companion object{
        const val OK_CODE = 0
    }
}