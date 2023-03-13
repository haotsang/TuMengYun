package com.example.myapplication.modules.space.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.example.myapplication.activity.BaseActivity
import com.example.myapplication.databinding.ActivityVideoPlayBinding
import com.example.myapplication.modules.space.utils.PortraitWhenFullScreenController
import com.example.myapplication.utils.RetrofitUtils
import xyz.doikki.videocontroller.StandardVideoController
import xyz.doikki.videocontroller.component.*
import xyz.doikki.videoplayer.player.VideoView


class VideoPlayerActivity2 : BaseActivity() {

    companion object {

        fun start(context: Context, url: String, title: String) {
            val intent = Intent(context, VideoPlayerActivity2::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }


    private val viewBinding: ActivityVideoPlayBinding by lazy {
        val inflater = LayoutInflater.from(this)
        ActivityVideoPlayBinding.inflate(inflater)
    }

    private var mController: PortraitWhenFullScreenController? = null
//    private var mController: StandardVideoController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        var mUrl = intent.dataString ?: intent.getStringExtra("url")
        if (mUrl.isNullOrEmpty()) {
            return
        }

        mUrl = RetrofitUtils.TOMCAT_ROOT_URL + mUrl

        val mTitle = intent.getStringExtra("title")


//        mController = StandardVideoController(this)
//        mController?.addDefaultControlComponent(mTitle, false)
        mController = PortraitWhenFullScreenController(this)
        mController?.addControlComponent(CompleteView(this))
        mController?.addControlComponent(ErrorView(this))
        mController?.addControlComponent(PrepareView(this))

        val titleView = TitleView(this)
        titleView.findViewById<View>(xyz.doikki.videocontroller.R.id.back).setOnClickListener { onBackPressed() }
        titleView.setTitle(mTitle)
        mController?.addControlComponent(titleView)

        mController?.addControlComponent(GestureView(this))
        mController?.setEnableOrientation(true)

        viewBinding.player2.startFullScreen()
        viewBinding.player2.setUrl(mUrl)
        viewBinding.player2.setVideoController(mController) //设置控制器
        viewBinding.player2.setScreenScaleType(VideoView.SCREEN_SCALE_16_9)
        viewBinding.player2.start() //开始播放，不调用则不自动播放

    }

    override fun onPause() {
        super.onPause()
        viewBinding.player2.pause()
    }

    override fun onResume() {
        super.onResume()
        viewBinding.player2.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding.player2.release()
    }

    override fun onBackPressed() {
        if (!mController?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
}
