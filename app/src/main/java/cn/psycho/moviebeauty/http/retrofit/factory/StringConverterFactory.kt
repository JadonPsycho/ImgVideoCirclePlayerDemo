package cn.psycho.moviebeauty.http.retrofit.factory

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by Psycho on 2018/1/10.
 */
class StringConverterFactory : Converter.Factory() {

    companion object {
        fun create(): StringConverterFactory {
            return StringConverterFactory()
        }
    }

    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        if (String::class != type){
            return null
        }
        return StringResponseBodyConverter
    }


    override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
        return if (String::class.java != type) {
            null
        } else StringRequestBodyConverter
    }

}