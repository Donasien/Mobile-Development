package com.codenesia.donasein.ui.submission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.codenesia.donasein.databinding.ActivitySubmissonBinding
import com.codenesia.donasein.ui.camera.CameraActivity
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.splash.StartActivity
import com.codenesia.donasein.utils.rotateBitmap
import com.codenesia.donasein.utils.uriToFile
import com.google.android.material.datepicker.MaterialDatePicker
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat

class SubmissonActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmissonBinding
    private var getFile: File? = null


    companion object {
        const val CAMERA_X_RESULT = 200

        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubmissonBinding.inflate(layoutInflater)

        setContentView(binding.root)


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.subdonationEtDate.setOnClickListener { showDatePicker() }
        binding.subdonationBtnCamera.setOnClickListener { startCamera() }
        binding.subdonationBtnGallery.setOnClickListener { startGallery() }
        binding.subdonationBtnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }


    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@SubmissonActivity)

            getFile = myFile
            binding.subdonationImgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.subdonationImgPreview.setImageURI(selectedImg)
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.subdonationImgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.subdonationImgPreview.setImageBitmap(result)
        }
    }

    private fun showDatePicker() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
        datePicker.setTitleText("Pilih Tanggal")
        val datePickerComponent = datePicker.build()


        datePickerComponent.show(supportFragmentManager, "MATERIAL_DATE_PICKER")
        datePickerComponent.addOnPositiveButtonClickListener {
            val dateFormat = SimpleDateFormat("dd-MM-yyyy")
            val date = dateFormat.format(it)
            binding.subdonationEtDate.setText(date)
        }
    }

    private fun reduceImage(file: File) : File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }


}
