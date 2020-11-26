package top.kenney.baselibrary.ext

import java.lang.Exception

/**
 * Created by Kenney on 2019-01-08 16:35
 */
class HttpStatusException(status: Int): Exception("错误码($status)")