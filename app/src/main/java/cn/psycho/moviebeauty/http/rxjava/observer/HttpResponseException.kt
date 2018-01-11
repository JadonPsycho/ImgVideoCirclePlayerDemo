package cn.psycho.moviebeauty.http.rxjava.observer

/**
 * Created by Psycho on 2018/1/10.
 */
class HttpResponseException(message:String,status:Int) :RuntimeException(message) {
    var status:Int?= null

    init {
        this.status = status
    }

}