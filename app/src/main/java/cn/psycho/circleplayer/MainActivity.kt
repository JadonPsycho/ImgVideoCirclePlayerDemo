package cn.psycho.circleplayer

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import cn.psycho.circleplayer.utils.PathUtils
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess
import java.io.File
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity(), SurfaceHolder.Callback, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {

    val filePath = PathUtils.getPath()

    var ScreenWidth: Int = 0
    var ScreenHeight: Int = 0

    var holder: SurfaceHolder? = null
    var player: MediaPlayer? = null

    var currentPath: String? = null

    var ImgDisplayTime: Int = 5 //默认图片展示时间

    var list: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getScreen()
        initEvent()
    }


    private fun getScreen() {
        val metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)
        ScreenWidth = metric.widthPixels
        ScreenHeight = metric.heightPixels
        Log.e("TAG", "Screen_width=$ScreenWidth,Screen_height=$ScreenHeight")
    }

    private fun initEvent() {
        PermissionGen.with(this).addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).request()
    }


    private fun goToPlay() {
        list = getFileList() //获取数据文件路径列表
        initPlayer()
    }


    private fun initPlayer() {
        holder = video_surface.holder
        holder?.addCallback(this)
        holder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        player = MediaPlayer()
        player?.setOnCompletionListener(this)
        player?.setOnPreparedListener(this)
        player?.setOnErrorListener(this)

        startPlay()

    }


    private fun startPlay() {
        var filePath = getNextFilePath()
        Log.e("TAG", "playPath=$filePath")
        if (filePath!!.endsWith(".mp4")) { //文件路径结尾是.mp4(视频)
            runOnUiThread {
                displayVideo(filePath)
            }
        } else if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {//文件路径结尾是图片
            runOnUiThread({
                displayImage(filePath)
            })
        }

    }

    /**
     * 获取下一条数据的路径
     */
    private fun getNextFilePath(): String? {
        if (list!!.size <= 0) {
            android.support.v7.app.AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("在该${filePath}文件夹下没有放入资源")
                    .setPositiveButton("确定", DialogInterface.OnClickListener { dialog, which ->
                        run {
                            finish()
                        }
                    }).show()
        }

        Log.e("TAG", "list.size= ${list!!.size}")

        if (currentPath == null) {
            currentPath = list!!.get(0)
        } else {
            var next = ""
            for (i in list!!.indices) {
                val s = list!![i]
                if (s == currentPath) {
                    if (i + 1 >= list!!.size) {
                        next = list!![0]
                        break
                    } else {
                        next = list!![i + 1]
                        break
                    }
                }
            }
            if (next !== "") {
                currentPath = next
            }
        }
        return currentPath
    }

    /**
     * 在指定路径中刷选数据 存入ArrayList
     */
    private fun getFileList(): ArrayList<String> {
        val list = java.util.ArrayList<String>()
        val DataDir = File(filePath)
        val files = DataDir.listFiles(DisPlayFileFilter())
        for (v in files) {
            list.add(v.absolutePath)
        }

        for (str in list!!) {
            Log.e("TAG_LIST", "list.name = $str")
        }

        Collections.sort(list)


        for (str in list!!) {
            Log.e("TAG_LIST_NEW", "list.name = $str")
        }

        return list
    }


    fun displayImage(imagepath: String) {

        display_image.visibility = View.VISIBLE
        video_surface.visibility = View.GONE

        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagepath, options)
        val outHeight = options.outHeight
        val outWidth = options.outWidth
        Log.e("通过Options获取到的图片大小", "width:" + outWidth + "height" + outHeight)
        var max = if (outHeight >= outWidth) outHeight else outWidth

        if (max <= 4096){
            try {
                val bitmap = BitmapFactory.decodeFile(imagepath)
                var img_width = bitmap.width
                var img_height = bitmap.height

                Log.e("TAG", "width=$img_width,height=$img_height")

                var scale: Float = img_width.toFloat() / img_height

                Log.e("TAG", "scale=$scale")

                if (img_width >= ScreenWidth) {
                    img_width = ScreenWidth
                    img_height = ((img_width / scale).toInt())
                    if (img_height > ScreenHeight) {
                        img_height = ScreenHeight
                        img_width = (img_height * scale).toInt()
                    }
                } else {
                    if (img_height > ScreenHeight) {
                        img_height = ScreenHeight
                        img_width = (img_height * scale).toInt()
                    }
                }

                Log.e("TAG", "new_width=$img_width,new_height=$img_height")

                val newBmp = Bitmap.createScaledBitmap(bitmap, img_width, img_height, true)

                display_image.setImageBitmap(newBmp)

                var displayTime = 0

                val file = File(imagepath)
                val name = file.name

                var strList: List<String> = name.split(".")
                var strResult: String = strList.get(0)

                if (!strResult.contains("-")) {
                    displayTime = ImgDisplayTime
                } else {
                    var stringlist: List<String> = strResult.split("-")
                    var setTimes: String = stringlist.get(1)

                    if (setTimes.contains("s")) {
                        setTimes = setTimes.replace("s", "")
                        try {
                            displayTime = setTimes.toInt()
                        } catch (e: NumberFormatException) {
                            e.printStackTrace()
                            displayTime = ImgDisplayTime
                        }

                    } else {
                        displayTime = ImgDisplayTime
                    }

                }

//        bitmap.recycle()
                val timer = Timer()
                val task = object : TimerTask() {
                    override fun run() {
                        // 需要做的事:发送消息
                        Log.e("TAG", "time is over")
                        timer.cancel()
                        bitmap.recycle()
                        newBmp.recycle()
                        startPlay()
                    }
                }

                if (displayTime < 5) {
                    displayTime = ImgDisplayTime
                } else if (displayTime > 24 * 60 * 60) {
                    displayTime = 24 * 60 * 60
                }

                Log.e("TAG", "displayTime=$displayTime")
                timer.schedule(task, (displayTime * 1000).toLong())
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                var mFile = File(imagepath)
                Toast.makeText(this, "${mFile.name}该图片资源错误", Toast.LENGTH_SHORT).show()
//            list?.remove(imagepath)
                startPlay()
            }
        }else{
            var mFile = File(imagepath)
            Toast.makeText(this, "${mFile.name}该图片尺寸过大", Toast.LENGTH_SHORT).show()
            startPlay()
        }

    }


    fun displayVideo(filepath: String) {
        val file = File(filepath)
        display_image.visibility = View.GONE
        video_surface.visibility = View.VISIBLE
        try {
            player?.reset()
            player?.setDataSource(filepath)
            player?.prepareAsync()
        } catch (e: IllegalArgumentException) { //catch 到异常直接播放下一条
            e.printStackTrace()
            Toast.makeText(this,"${file.name}播放异常",Toast.LENGTH_SHORT).show()
            startPlay()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            Toast.makeText(this,"${file.name}播放异常",Toast.LENGTH_SHORT).show()
            startPlay()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this,"${file.name}播放异常",Toast.LENGTH_SHORT).show()
            startPlay()
        }

    }

    override fun onCompletion(mp: MediaPlayer?) {
        Log.e("TAG", "onCompletion")
        startPlay()
    }

    override fun onError(mp: MediaPlayer?, whatErrorCode: Int, extra: Int): Boolean {
        when (whatErrorCode) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED")
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN")
            else -> {
            }
        }

        return false
    }

    override fun onPrepared(mp: MediaPlayer?) {
        var video_height: Int? = mp?.videoHeight // 视频高度
        var video_width: Int? = mp?.videoWidth// 视频宽度
        Log.e("TAG", "video_width=$video_width,video_height=$video_height")

        val scale: Float = video_width?.toFloat()!! / video_height!!

        if (video_width >= ScreenWidth) {
            video_width = ScreenWidth
            video_height = ((video_width / scale).toInt())
            if (video_height > ScreenHeight) {
                video_height = ScreenHeight
                video_width = (video_height * scale).toInt()
            }
        } else {
            if (video_height > ScreenHeight) {
                video_height = ScreenHeight
                video_width = (video_height * scale).toInt()
            } else { //两边都比屏幕小
                video_width = ScreenWidth
                video_height = ((video_width / scale).toInt())
                if (video_height > ScreenHeight) {
                    video_height = ScreenHeight
                    video_width = (video_height * scale).toInt()
                }
            }
        }
        Log.e("TAG", "new_video_width=$video_width,new_video_height=$video_height")

        val l: ViewGroup.LayoutParams = video_surface.layoutParams
        l.width = video_width
        l.height = video_height
        video_surface.layoutParams = l
        player?.start()

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        // 当SurfaceView中的Surface被创建的时候被调用
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player?.setDisplay(holder)
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        //player.prepareAsync();
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        // 当Surface尺寸等参数改变时触发
        Log.v("Surface Change:::", "surfaceChanged called")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.v("Surface Destory:::", "surfaceDestroyed called")
    }


    @PermissionSuccess(requestCode = 100)
    fun agree() {
        goToPlay()
    }

    @PermissionFail(requestCode = 100)
    private fun disagree() {
        AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("读取SD卡权限没有通过")
                .setPositiveButton("关闭") { dialog, which -> }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())
    }


}
