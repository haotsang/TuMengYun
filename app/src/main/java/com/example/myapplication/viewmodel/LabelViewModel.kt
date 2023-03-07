package com.example.myapplication.viewmodel

import androidx.lifecycle.LifecycleCoroutineScope
import com.example.myapplication.entity.LabelBean
import com.example.myapplication.entity.LabelImgBean
import com.example.myapplication.http.LabelImgUtils
import com.example.myapplication.http.LabelUtils
import com.example.myapplication.utils.livebus.LiveDataBus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object LabelViewModel {

    var label: LabelBean? = null
        set(value) { // setter
            field = value
            LiveDataBus.send("livebus_label_change", field)
        }
    var labelImg: List<LabelImgBean>? = null
        set(value) { // setter
            field = value
            LiveDataBus.send("livebus_label_img_change", field)
        }

    init {
        label = null
        labelImg = null
    }


    fun getLabel(viewModelScope: LifecycleCoroutineScope, region: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val labelBean = try {
                LabelUtils.getOrInsert(region)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
//                ?: throw ExceptionInInitializerError("labelBean is null")

            label = labelBean
            LiveDataBus.send("livebus_label_change", labelBean)
        }

    }

    fun getLabelImg(viewModelScope: LifecycleCoroutineScope, labelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val img = try {
                LabelImgUtils.getLabelImgList(labelId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            labelImg = img
            LiveDataBus.send("livebus_label_img_change", img)
        }
    }


    /*used once*/
    fun getLabel2(viewModelScope: LifecycleCoroutineScope, region: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val labelBean = try {
                LabelUtils.getOrInsert(region)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }
//                ?: throw ExceptionInInitializerError("labelBean is null")

            label = labelBean
            LiveDataBus.send("livebus_label_change2", labelBean)
        }

    }
    fun getLabelImg2(viewModelScope: LifecycleCoroutineScope, labelId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val img = try {
                LabelImgUtils.getLabelImgList(labelId)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                null
            }

            labelImg = img
            LiveDataBus.send("livebus_label_img_change2", img)
        }
    }

}