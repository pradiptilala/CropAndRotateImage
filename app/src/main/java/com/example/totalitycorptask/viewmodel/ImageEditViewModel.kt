package com.example.totalitycorptask.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.totalitycorptask.utils.AppFileUtils

class ImageEditViewModel : ViewModel() {
    /**
     * This variable stores bitmap
     */
    var updatedBitmapLiveData: MutableLiveData<Bitmap> = MutableLiveData()

    /**
     * This variable stores image URL
     */
    var imageUrl: String = ""

    /**
     * This variable stores AppFileUtils instance
     */
    val appFileUtils: AppFileUtils = AppFileUtils()
}