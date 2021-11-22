package com.example.totalitycorptask

import androidx.lifecycle.ViewModel

class ImageEditViewModel : ViewModel() {
    var imageUrl: String = ""

    var croppedImageUrl: String = ""

    val appFileUtils : AppFileUtils = AppFileUtils()

}