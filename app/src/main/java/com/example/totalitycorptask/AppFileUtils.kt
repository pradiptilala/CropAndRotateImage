package com.example.totalitycorptask

import android.content.Context
import android.os.Environment
import android.provider.ContactsContract
import android.provider.MediaStore
import java.io.File
import java.nio.file.DirectoryIteratorException
import java.text.SimpleDateFormat
import java.util.*

class AppFileUtils {
    /** Use external media if it is available, our app's file directory otherwise */
    fun getOutputDirectory(context: Context): File {
        val appContext = context.applicationContext
        val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else appContext.filesDir
    }

    /* Helper function used to create a timestamped file */
   fun createNewFile(context: Context,format: String, extension: String) =
        File(
            getOutputDirectory(context), SimpleDateFormat(format, Locale.US)
                .format(System.currentTimeMillis()) + extension
        )

}