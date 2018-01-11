package cn.psycho.moviebeauty.utils

import java.io.File

/**
 * Created by Psycho on 2018/1/3.
 */
class PathUtils {

    companion object {
        val filePath = android.os.Environment
                .getExternalStorageDirectory().absolutePath + "/tripbePlayer/"


        fun getPath():String{
            val file = File(filePath)

            if (!file.exists()){
                file.mkdir()
            }

            return filePath
        }

    }


}