package com.example.myapplication.activity

import android.content.Intent
import com.example.myapplication.R
import com.google.zxing.Result
import com.king.zxing.CaptureActivity
import com.king.zxing.DecodeConfig
import com.king.zxing.DecodeFormatManager
import com.king.zxing.analyze.MultiFormatAnalyzer
import org.json.JSONObject


class ScanActivity : CaptureActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_scan
    }

    override fun initCameraScan() {
        super.initCameraScan()
        //初始化解码配置
        //初始化解码配置
        val decodeConfig = DecodeConfig()
        decodeConfig.setHints(DecodeFormatManager.ALL_HINTS) //如果只有识别二维码的需求，这样设置效率会更高，不设置默认为DecodeFormatManager.DEFAULT_HINTS
            .setFullAreaScan(false) //设置是否全区域识别，默认false
            .setAreaRectRatio(0.8f) //设置识别区域比例，默认0.8，设置的比例最终会在预览区域裁剪基于此比例的一个矩形进行扫码识别
            .setAreaRectVerticalOffset(0).areaRectHorizontalOffset =
            0 //设置识别区域水平方向偏移量，默认为0，为0表示居中，可以为负数


        //在启动预览之前，设置分析器，只识别二维码

        //在启动预览之前，设置分析器，只识别二维码
        cameraScan
            .setVibrate(true) //设置是否震动，默认为false
            .setNeedAutoZoom(true) //二维码太小时可自动缩放，默认为false
            .setAnalyzer(MultiFormatAnalyzer(decodeConfig)) //设置分析器,如果内置实现的一些分析器不满足您的需求，你也可以自定义去实现


    }

    override fun onResume() {
        super.onResume()
        cameraScan.setAnalyzeImage(true)
    }

    override fun onPause() {
        super.onPause()
        cameraScan.setAnalyzeImage(false)
    }

    override fun onScanResultCallback(result: Result?): Boolean {
        try {
            val jsonObject = JSONObject(result?.text ?: "")
            val zhanQuName = jsonObject.optString("zhanqu")
            val zhanPinName = jsonObject.optString("zhanpin")

            if (zhanQuName.isNullOrEmpty() || zhanPinName.isNullOrEmpty()) {
                val intent = Intent(this, ErrorActivity::class.java)
                intent.putExtra("error", "扫码失败：非展区二维码1")
                startActivity(intent)
            } else {
                val intent = Intent(this, AfterServiceActivity::class.java)
                intent.putExtra("zhanQu", zhanQuName)
                intent.putExtra("zhanPin", zhanPinName)
                startActivity(intent)
            }
        } catch (e: java.lang.Exception) {
            val intent = Intent(this, ErrorActivity::class.java)
            intent.putExtra("error", "扫码失败：非展区二维码2")
            startActivity(intent)
        }

        return true
    }


}