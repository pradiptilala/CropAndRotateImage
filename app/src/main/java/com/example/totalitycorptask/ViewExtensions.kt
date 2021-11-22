package com.example.totalitycorptask

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import com.example.totalitycorptask.AppConstants.ANIMATION_FAST_MILLIS
import com.example.totalitycorptask.BuildConfig

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

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
