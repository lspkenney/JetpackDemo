package top.kenney.mvvmdemo.base

sealed class StateActionEvent(val msg:String?)

class LoadStartState(msg:String):StateActionEvent(msg)
class LoadCompleteState(msg:String? = null, val requestCode:Int = 0):StateActionEvent(msg)
class LoadErrorState(msg:String?, requestCode:Int = 0):StateActionEvent(msg)


