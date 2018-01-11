package cn.psycho.moviebeauty.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Psycho on 2018/1/10.
 */
interface Api {
    @GET("/v2/movie/top250")
    fun loadTopW250Movies(@Query("start") start: Int,
                          @Query("count") count: Int): Observable<String>
}