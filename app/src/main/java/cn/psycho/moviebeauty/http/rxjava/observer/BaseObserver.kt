package cn.psycho.moviebeauty.http.rxjava.observer

import android.support.annotation.CallSuper
import android.util.Log
import android.widget.Toast
import cn.psycho.moviebeauty.MyApplication
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.HttpException

/**
 * Created by Psycho on 2018/1/10.
 */
abstract class BaseObserver<T>: Observer<T> {

    override fun onSubscribe(d: Disposable?) {
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable?) {
        val responseException: HttpResponseException
        if (e is HttpException) {
            responseException = HttpResponseException("网络请求出错", e.code())
        } else if (e is HttpResponseException) {
            responseException = e
        } else {//其他或者没网会走这里
            e?.printStackTrace()
            Log.e("TAG", "onError: " + e?.message + "==" + e?.localizedMessage + " +++" + e.toString() + "")
            responseException = HttpResponseException("网络异常,请稍后重试", -1024)
        }

        onFailed(responseException)
    }

    abstract fun onSuccess(t: T)



    @CallSuper private fun onFailed(responseException: HttpResponseException){
        Toast.makeText(MyApplication.getInstance(),
                "${responseException.message}(${responseException.status})",
                Toast.LENGTH_SHORT).show()
    }
}