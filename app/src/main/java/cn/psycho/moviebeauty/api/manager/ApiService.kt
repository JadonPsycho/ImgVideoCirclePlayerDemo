package cn.psycho.moviebeauty.api.manager

import cn.psycho.moviebeauty.api.Api
import cn.psycho.moviebeauty.http.retrofit.RetrofitHelper

/**
 * Created by Psycho on 2018/1/10.
 */
object ApiService {

    fun getApiService(): Api? {
        return RetrofitHelper.getRetrofit()?.create(Api::class.java)
    }
}