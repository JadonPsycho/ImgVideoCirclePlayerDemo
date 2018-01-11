package cn.psycho.moviebeauty.http.retrofit

import cn.psycho.moviebeauty.api.AppUrlConfig
import cn.psycho.moviebeauty.http.okhttp.OkhttpHelper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Created by Psycho on 2018/1/10.
 */
object RetrofitHelper {

    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder().baseUrl(AppUrlConfig.BaseUrl)
                .client(OkhttpHelper.getClient())
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .validateEagerly(true)
                .build()
    }

    fun getRetrofit():Retrofit?{
        return retrofit
    }
}