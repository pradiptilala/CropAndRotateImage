package com.example.totalitycorptask


import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.example.totalitycorptask.AppConstants.IMAGE_URL
import com.example.totalitycorptask.databinding.ActivityImageEditBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.IOException


class ImageEditActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityImageEditBinding
    lateinit var viewModel: ImageEditViewModel
    val PIC_CROP = 1

    var curRotate = 0f

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this@ImageEditActivity) // optional usage
        } else {
            // an error occurred
            val exception = result.error
        }
    }

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
        getIntentData()
        setImageToPreview()
    }

    fun getIntentData() {
        intent.extras?.getString(IMAGE_URL)?.let {
            viewModel.imageUrl = it
        }
    }

    private fun setImageToPreview() {
        if (viewModel.imageUrl.isNotBlank()) {
//            binding.ivPreviewImageEdit.setImageURI(viewModel.imageUrl.toUri())
//            val bitmap = rotateBitmap(viewModel.imageUrl)
//            binding.ivPreviewImageEdit.setImageBitmap(viewModel.imageUrl.toUri())
//            binding.ivPreviewImageEdit.setImageBitmap(bitmap)
            setImageToView()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnUndo -> {

            }

            R.id.btnRotate -> {
                setImageToView()
            }

            R.id.btnCrop -> {
                cropImage()
            }

            R.id.btnSave -> {

            }
        }
    }

    fun cropImage() {
        binding.ivPreviewImageEdit.setImageBitmap(binding.ivPreviewImageEdit.croppedImage)
        binding.ivPreviewImageEdit.customOutputUri

        binding.ivPreviewImageEdit.setOnCropImageCompleteListener(object :
            CropImageView.OnCropImageCompleteListener {
            override fun onCropImageComplete(
                view: CropImageView,
                result: CropImageView.CropResult
            ) {
                binding.ivPreviewImageEdit.setImageBitmap(result.bitmap)
            }
        })
    }

    /**
     * This methos used to add bitmap to image in remoteView
     */
    fun setImageToView() = runBlocking {
        withContext(Dispatchers.IO) {
            try {

                val bitmap: Bitmap = GlideApp
                    .with(this@ImageEditActivity)
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
            binding.ivPreviewImageEdit.setImageBitmap(bitmap)
        }
    }

    /**
     * This methos used to add bitmap to image in remoteView
     */
    fun rotateImageToView() = runBlocking {
        withContext(Dispatchers.IO) {
            try {

                val bitmap: Bitmap = GlideApp
                    .with(this@ImageEditActivity)
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
            binding.ivPreviewImageEdit.setImageBitmap(bitmap)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PIC_CROP) {
            if (data != null) {
                // get the returned data
                val extras = data.extras
                // get the cropped bitmap
                val selectedBitmap = extras!!.getParcelable<Bitmap>("data")
                binding.ivPreviewImageEdit.setImageBitmap(selectedBitmap)
            }
        }
    }
}