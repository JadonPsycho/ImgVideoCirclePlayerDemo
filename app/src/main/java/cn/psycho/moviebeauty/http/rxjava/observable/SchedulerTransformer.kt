package cn.psycho.moviebeauty.http.rxjava.observable

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by Psycho on 2018/1/10.
 */
object SchedulerTransformer {
    fun <T> transformer(): ObservableTransformer<T, T> {
        val otf = ObservableTransformer<T, T> { upstream ->
            upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }

        return otf
    }
}