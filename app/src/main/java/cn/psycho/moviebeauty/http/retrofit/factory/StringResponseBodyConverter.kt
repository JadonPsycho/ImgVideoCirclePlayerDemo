package cn.psycho.moviebeauty.http.retrofit.factory

import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Created by Psycho on 2018/1/10.
 */
object StringResponseBodyConverter : Converter<ResponseBody, String> {

    override fun convert(value: ResponseBody?): String {
        try {
            return value!!.string()
        } finally {
            value!!.close()
        }
    }

}