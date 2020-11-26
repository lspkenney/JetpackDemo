package top.kenney.baselibrary.net.base

data class Pagination(
    val current: Int,
    val pageSize: Int,
    val total: Int
)