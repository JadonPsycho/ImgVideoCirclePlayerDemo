package cn.psycho.moviebeauty.api

/**
 * Created by Psycho on 2018/1/10.
 */
object AppUrlConfig {

    var BaseUrl = ""

    val DOUBAN = 1
    val GANK = 2

    val DouBanUrl = "https://api.douban.com"
    val GankUrl = ""

    fun setType(type:Int){
        when(type){
            DOUBAN -> BaseUrl=DouBanUrl
            GANK -> BaseUrl=GankUrl
        }

    }
}