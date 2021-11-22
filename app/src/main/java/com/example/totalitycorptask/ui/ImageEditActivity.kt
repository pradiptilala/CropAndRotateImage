package com.example.totalitycorptask.ui

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.totalitycorptask.R
import com.example.totalitycorptask.databinding.ActivityImageEditBinding
import com.example.totalitycorptask.startTotalCorpActivity
import com.example.totalitycorptask.utils.AppConstants.FILENAME
import com.example.totalitycorptask.utils.AppConstants.IMAGE_URL
import com.example.totalitycorptask.utils.AppConstants.PHOTO_EXTENSION
import com.example.totalitycorptask.utils.GlideApp
import com.example.totalitycorptask.viewmodel.ImageEditViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException


/**
 * This activity is used to edit image using undo, crop, rotate and save functionality
 */
class ImageEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityImageEditBinding
    lateinit var viewModel: ImageEditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageEditBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel = ViewModelProvider(this)[ImageEditViewModel::class.java]
        initialiseViews()
    }

    private fun initialiseViews() {
        binding.btnUndo.setOnClickListener(this)
        binding.btnRotate.setOnClickListener(this)
        binding.btnCrop.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

        viewModel.updatedBitmapLiveData.observe(this, Observer { bitmap ->
            if (bitmap != null) {
                binding.ivPreviewImageEdit.setImageBitmap(bitmap)
            }
        })

        getIntentData()

        if (viewModel.imageUrl.isNotBlank()) {
            setOrginalImageToView()
        }
    }

    fun getIntentData() {
        intent.extras?.getString(IMAGE_URL)?.let {
            viewModel.imageUrl = it
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUndo -> {
                setOrginalImageToView()
            }

            R.id.btnRotate -> {
                rotateImageToView()
            }

            R.id.btnCrop -> {
                viewModel.updatedBitmapLiveData.value = binding.ivPreviewImageEdit.croppedImage
            }

            R.id.btnSave -> {
                saveFile()
            }
        }
    }

    /**
     * This method used to set original image into ImageView
     */
    fun setOrginalImageToView() = runBlocking {
        withContext(Dispatchers.IO) {
            try {
                val bitmap: Bitmap = GlideApp.with(this@ImageEditActivity)
                    .asBitmap()
                    .load(viewModel.imageUrl.toUri())
                    .submit()
                    .get()
                bitmap
            } catch (e: IOException) {
                null
            } catch (e: Exception) {
                null
            }
        }?.let { bitmap ->
            viewModel.updatedBitmapLiveData.value = bitmap
        }
    }

    /**
     * This method used to rotate image
     */
    fun rotateImageToView() = runBlocking {
        withContext(Dispatchers.IO) {
            try {
                viewModel.updatedBitmapLiveData.value?.let { bitmap ->
                    val matrix = Matrix()

                    matrix.postRotate(90f)

                    val scaledBitmap =
                        Bitmap.createScaledBitmap(bitmap, bitmap.width, bitmap.height, true)

                    val rotatedBitmap = Bitmap.createBitmap(
                        scaledBitmap,
                        0,
                        0,
                        scaledBitmap.width,
                        scaledBitmap.height,
                        matrix,
                        true
                    )
                    rotatedBitmap
                }
            } catch (e: IOException) {
                null
            } catch (e: Exception) {
                null
            }
        }?.let { bitmap ->
            viewModel.updatedBitmapLiveData.value = bitmap
        }
    }

    /**
     * This method used to save file and redirect to home screen
     */
    fun saveFile() {
        val file = viewModel.appFileUtils.createNewFile(this, FILENAME, PHOTO_EXTENSION)
        val fOut = FileOutputStream(file)
        viewModel.updatedBitmapLiveData.value?.compress(Bitmap.CompressFormat.PNG, 85, fOut)
        fOut.flush()
        fOut.close()

        val bundleExtra = Bundle()
        bundleExtra.putString(IMAGE_URL, file.toUri().toString())

        startTotalCorpActivity(
            HomeActivity::class.java, bundleExtra, null
        )

        finish()
    }
}