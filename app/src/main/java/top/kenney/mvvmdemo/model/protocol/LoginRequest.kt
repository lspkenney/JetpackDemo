package top.kenney.mvvmdemo.model.protocol


data class LoginRequest(
    var username:String = "",
    var password:String = ""
)