package com.example.totalitycorptask.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.totalitycorptask.R
import com.example.totalitycorptask.camera.CustomCameraActivity
import com.example.totalitycorptask.checkAppPermissions
import com.example.totalitycorptask.databinding.ActivityHomeBinding
import com.example.totalitycorptask.requestAppPermissions
import com.example.totalitycorptask.startTotalCorpActivity
import com.example.totalitycorptask.utils.AppConstants
import com.example.totalitycorptask.utils.AppConstants.CAMERA_PERMISSION_CODE
import com.example.totalitycorptask.utils.AppConstants.CAMERA_PHOTO_REQUEST_CODE
import com.example.totalitycorptask.utils.AppConstants.CAMERA_STORAGE_PERMISSION_CODE
import com.example.totalitycorptask.utils.AppConstants.GALLERY_PHOTO_REQUEST_CODE
import com.example.totalitycorptask.utils.GlideApp
import com.example.totalitycorptask.viewmodel.HomeViewModel

/**
 * This activity is used to preview edited image with take selfie and select image from gallery
 */
class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityHomeBinding
    lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initialiseViews()
    }

    /**
     * This method used to initialise views & click listeners
     */
    fun initialiseViews() {
        binding.btnTakeSelfie.setOnClickListener(this)
        binding.btnUploadFromGallery.setOnClickListener(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.getString(AppConstants.IMAGE_URL)?.let {
            viewModel.imageUrl = it
        }
        if (viewModel.imageUrl.isNotEmpty()) {
            GlideApp.with(this)
                .load(viewModel.imageUrl.toUri())
                .into(binding.ivPreviewImage)
        }
    }

    /**
     * This method used to check permission and open camera to take selfie
     */
    fun takeSelfie() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkAppPermissions()) {
                openCameraToTakeSelfie()
            } else {
                requestAppPermissions(CAMERA_PERMISSION_CODE)
            }
        } else {
            openCameraToTakeSelfie()
        }
    }

    /**
     * This method opens camera activity
     */
    fun openCameraToTakeSelfie() {
        startActivity(Intent(this, CustomCameraActivity::class.java))
    }

    /**
     * This method check permissions and opens intent to select image from gallery
     */
    fun selectImageFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkAppPermissions()) {
                openIntentToSelectImgFrmGallery()
            } else {
                requestAppPermissions(CAMERA_STORAGE_PERMISSION_CODE)
            }
        } else {
            openIntentToSelectImgFrmGallery()
        }
    }

    /**
     * This method opens intent to select image from gallery
     */
    fun openIntentToSelectImgFrmGallery() {
        startActivityForResult(
            Intent.createChooser(
                Intent().setAction(Intent.ACTION_OPEN_DOCUMENT)
                    .addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    .setType(AppConstants.SET_IMAGE_TYPE),
                AppConstants.SELECT_SINGLE_IMAGE
            ), GALLERY_PHOTO_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PHOTO_REQUEST_CODE -> {
                binding.ivPreviewImage.setImageURI(data?.data)
            }

            GALLERY_PHOTO_REQUEST_CODE -> {
                val uri = data?.data
                uri?.let {
                    contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    binding.ivPreviewImage.setImageURI(data?.data)
                    viewModel.imageUrl = data?.data.toString()

                    val bundleExtra = Bundle()
                    bundleExtra.putString(AppConstants.IMAGE_URL, viewModel.imageUrl)
                    startTotalCorpActivity(
                        ImageEditActivity::class.java, bundleExtra, null
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        openCameraToTakeSelfie()
                    }
                } else {
                    if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                        && grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        openCameraToTakeSelfie()
                    } else {
                        takeSelfie()
                    }
                }
            }

            CAMERA_STORAGE_PERMISSION_CODE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        openIntentToSelectImgFrmGallery()
                    }
                } else {
                    if (grantResults.size > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                        && grantResults[1] ==
                        PackageManager.PERMISSION_GRANTED
                    ) {
                        openIntentToSelectImgFrmGallery()
                    } else {
                        takeSelfie()
                    }
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnTakeSelfie -> {
                takeSelfie()
            }

            R.id.btnUploadFromGallery -> {
                selectImageFromGallery()
            }
        }
    }

}