package com.example.totalitycorptask

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.totalitycorptask.AppConstants.CAMERA_PERMISSION_CODE
import com.example.totalitycorptask.AppConstants.CAMERA_PHOTO_REQUEST_CODE
import com.example.totalitycorptask.camera.CustomCameraActivity
import com.example.totalitycorptask.databinding.ActivityHomeBinding


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

    fun initialiseViews() {
        binding.btnTakeSelfie.setOnClickListener(this)
        binding.btnUploadFromGallery.setOnClickListener(this)
    }

    fun takeSalfie() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED && checkSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED
            ) {

                val permission = arrayOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
                requestPermissions(permission, CAMERA_PERMISSION_CODE)
            } else {
                openIntentToTakeSelfie()
            }
        } else {
            openIntentToTakeSelfie()
        }
    }

    fun openIntentToTakeSelfie() {
//        val file = viewModel.appFileUtils.createNewFile(this, AppConstants.FILENAME, PHOTO_EXTENSION)

//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val photoUri: Uri = Uri.fromFile(file)
//        val photoUri: Uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//        intent.putExtra(android.intent.extras.CAMERA_FACING, 1)
//        startActivityForResult(intent, CAMERA_PHOTO_REQUEST_CODE)
        startActivity(Intent(this, CustomCameraActivity::class.java))
    }

    fun selectImageFromGallery() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_PHOTO_REQUEST_CODE -> {
                binding.ivPreviewImage.setImageURI(data?.data)
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
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                    && grantResults[1] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    openIntentToTakeSelfie()
                } else {
                    takeSalfie()
                }
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnTakeSelfie -> {
                takeSalfie()
            }
        }
    }

}