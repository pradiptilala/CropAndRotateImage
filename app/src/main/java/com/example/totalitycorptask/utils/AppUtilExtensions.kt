package com.example.totalitycorptask

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.example.totalitycorptask.utils.AppConstants.ANIMATION_FAST_MILLIS


/**
 * This function is used to print log in activities and fragments
 */
fun Context.printLog(printData: String?, additionalTag: String? = "") {
    val TAG = this.javaClass.simpleName
    if (BuildConfig.DEBUG) if (additionalTag != null || printData != null) {
        Log.e(TAG + additionalTag + "::", printData!!)
    }
}


/**
 * Simulate a button click, including a small delay while it is being pressed to trigger the
 * appropriate animations.
 */
@JvmOverloads
fun ImageButton.simulateClick(delay: Long = ANIMATION_FAST_MILLIS) {
    performClick()
    isPressed = true
    invalidate()
    postDelayed({
        invalidate()
        isPressed = false
    }, delay)
}

/**
 * This function is used for starting activity
 */
fun <T> Context.startTotalCorpActivity(
    clazz: Class<T>,
    bundle: Bundle? = null,
    flag1: Int? = null
) {
    val intent = Intent(this, clazz)

    if (bundle != null) {
        intent.putExtras(bundle)
    }

    if (flag1 != null) {
        intent.addFlags(flag1)
    }

    startActivity(intent)
}

/**
 * This function is used to print log in activities and fragments
 */
fun Activity.checkAppPermissions(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
        this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
        this.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
        this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    ) {
        return true
    } else {
        return false
    }
}

/**
 * This function is used to print log in activities and fragments
 */
fun Activity.requestAppPermissions(REQ_CODE: Int) {
    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        arrayOf(
            Manifest.permission.CAMERA
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf()
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.requestPermissions(permission, REQ_CODE)
    }
}
