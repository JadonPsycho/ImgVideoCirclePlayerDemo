package cn.psycho.moviebeauty.utils

import java.io.File
import java.io.FilenameFilter

/**
 * Created by Psycho on 2018/1/4.
 */
class DisPlayFileFilter:FilenameFilter {

    override fun accept(dir: File?, name: String?): Boolean {
        return name!!.endsWith(".mp4")||name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".png")
    }
}