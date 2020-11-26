package top.kenney.baselibrary.net.base

/**
 * current 当前页码
 * pages  总页码
 * size    pageSize
 * total    总记录数
 */
class PageRecordData<T>(val current:Int, val pages:Int, val size:Int, val total:Int, val records:List<T>)