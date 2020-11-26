package top.kenney.baselibrary.vm

sealed class StateActionEvent(val msg:String?)

class LoadStartState(msg:String, val requestCode:Int = 0): StateActionEvent(msg)
class LoadCompleteState(msg:String? = null, val requestCode:Int = 0, val data:Any? = null): StateActionEvent(msg)
class LoadErrorState(msg:String?, errorCode:Int = 0,val requestCode:Int = 0): StateActionEvent(msg)


