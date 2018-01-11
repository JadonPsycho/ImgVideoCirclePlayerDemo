package cn.psycho.moviebeauty.http.okhttp

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * OkHttpClient的配置
 * Created by Psycho on 2018/1/10.
 */
object OkhttpHelper {

    private var okHttpClient: OkHttpClient? = null
    /**
     * 连接超时
     */
    private val CONNECT_TIMEOUT = 10
    /**
     * 读取超时
     */
    private val READ_TIMEOUT = 10
    /**
     * 写入超时
     */
    private val WRITE_TIMEOUT = 10

    init {

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        okHttpClient = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT.toLong(),TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT.toLong(),TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT.toLong(),TimeUnit.SECONDS)
                .addInterceptor(logging)
//                .addInterceptor(HeaderIntercepter())
                .build()
    }

    fun getClient(): OkHttpClient? {
        return okHttpClient
    }

}