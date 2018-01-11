package cn.psycho.moviebeauty.moudle

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import cn.psycho.moviebeauty.R
import cn.psycho.moviebeauty.api.AppUrlConfig
import cn.psycho.moviebeauty.api.manager.ApiService
import cn.psycho.moviebeauty.http.rxjava.observable.SchedulerTransformer
import cn.psycho.moviebeauty.http.rxjava.observer.BaseObserver

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        AppUrlConfig.setType(AppUrlConfig.DOUBAN)
        ApiService.getApiService()?.loadTopW250Movies(0,10)
                ?.compose(SchedulerTransformer.transformer())
                ?.subscribe(object :BaseObserver<String>(){
                    override fun onSuccess(t: String) {
                        Log.e("TAG", t)
                    }
                })
    }



}
