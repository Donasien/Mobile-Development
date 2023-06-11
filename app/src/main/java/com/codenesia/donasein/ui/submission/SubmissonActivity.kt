package com.codenesia.donasein.ui.submission

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import com.codenesia.donasein.R
import com.codenesia.donasein.data.Results
import com.codenesia.donasein.data.local.preference.UserPreferences
import com.codenesia.donasein.databinding.ActivitySubmissonBinding
import com.codenesia.donasein.ui.ViewModelFactory
import com.codenesia.donasein.ui.camera.CameraActivity
import com.codenesia.donasein.ui.main.MainActivity
import com.codenesia.donasein.ui.main.ui.donate.DonateViewModel
import com.codenesia.donasein.ui.splash.StartActivity
import com.codenesia.donasein.utils.rotateBitmap
import com.codenesia.donasein.utils.uriToFile
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class SubmissonActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySubmissonBinding
    private var getFile1: File? = null // sampul
    private var getFile2: File? = null // diagnosa
    private var getFile3: File? = null // hasil lab
    private var getFile4: File? = null // ktp
    private var getFile5: File? = null // sktm
    private var type_disaster: String? = null // type disaster

    private var checkGallery: Int? = null
    private var dateDonate: String? = null

    private val submissionMedicalApiViewModel: SubmissionMedicalApiViewModel by viewModels {
        ViewModelFactory(this)
    }

    private val donateViewModel: DonateViewModel by viewModels {
        ViewModelFactory(this)
    }


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
        binding.subdonationSpinnerDisaster.lifecycleOwner = this


        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.subdonationSpinnerDisaster.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            type_disaster = newText
        }

        binding.subdonationEtDate.setOnClickListener { showDatePicker() }
        binding.subdonationBtnCamera.setOnClickListener { startCamera() }
        binding.subdonationBtnGallery.setOnClickListener { startGallery(1) }
        binding.subdonationBtnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java), ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle())
            finish()
        }

        binding.subdonationBtnGalleryDiagnosa.setOnClickListener { startGallery(2) }
        binding.subdonationBtnGalleryLab.setOnClickListener { startGallery(3) }
        binding.subdonationBtnKtp.setOnClickListener { startGallery(4) }
        binding.subdonationBtnSktm.setOnClickListener { startGallery(5) }
        binding.subdonationBtnSave.setOnClickListener { submitDonation() }


    }

    private fun submitDonation() {
        if (getFile1 != null && getFile2 != null && getFile3 != null && getFile4 != null && getFile5 != null && type_disaster!!.isNotEmpty()) {
            val cover_photo = reduceImage(getFile1 as File)
            val medical_photo = reduceImage(getFile2 as File)
            val disease_photo = reduceImage(getFile3 as File)
            val ktp_photo = reduceImage(getFile4 as File)
            val sktm_photo = reduceImage(getFile5 as File)
            val description = binding.subdonationEtDesc.text.toString()
            val title = binding.subdonationEtTitle.text.toString()
            val target_amount = binding.subdonationEtCost.text.toString()
            val end_date = dateDonate

            if (description.isNotEmpty() && title.isNotEmpty() && target_amount.isNotEmpty() && end_date!!.isNotEmpty()) {
                addDonation(cover_photo, medical_photo, disease_photo, ktp_photo,
                    sktm_photo, description, title, target_amount, end_date
                )
            } else {
                showMessage(false, "Fill in the form correctly")
            }

        } else {
            showMessage(false, "Fill in the form correctly")
        }
    }

    private fun addDonation(
        coverPhoto: File,
        medicalPhoto: File,
        diseasePhoto: File,
        ktpPhoto: File,
        sktmPhoto: File,
        description: String,
        title: String,
        targetAmount: String,
        endDate: String
    ) = runBlocking<Unit> {
        // string to request body
        val userToken = UserPreferences(applicationContext).getUser().tokenId.toString()
        val token = userToken.toRequestBody("text/plain".toMediaTypeOrNull())
        val title_donation = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val desc_donation = description.toRequestBody("text/plain".toMediaTypeOrNull())
        val target_donation = targetAmount.toRequestBody("text/plain".toMediaTypeOrNull())
        val date_donation = endDate.toRequestBody("text/plain".toMediaTypeOrNull())

        // request body to image
        val cover_photo = coverPhoto.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val medical_photo = medicalPhoto.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val disease_photo = diseasePhoto.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val ktp_photo = ktpPhoto.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val sktm_photo = sktmPhoto.asRequestBody("image/jpeg".toMediaTypeOrNull())

        // create a multipartbody
        val file1: MultipartBody.Part = MultipartBody.Part.createFormData("cover_photo", coverPhoto.name, cover_photo)
        val file2: MultipartBody.Part = MultipartBody.Part.createFormData("medical_photo", medicalPhoto.name, medical_photo)
        val file3: MultipartBody.Part = MultipartBody.Part.createFormData("disease_photo", diseasePhoto.name, disease_photo)
        val file4: MultipartBody.Part = MultipartBody.Part.createFormData("ktp_photo", ktpPhoto.name, ktp_photo)
        val file5: MultipartBody.Part = MultipartBody.Part.createFormData("sktm_photo", sktmPhoto.name, sktm_photo)
        val file6: MultipartBody.Part = MultipartBody.Part.createFormData("file", medicalPhoto.name, medical_photo)


        sendDataToServer(token, title_donation, desc_donation, target_donation, date_donation, file1, file2, file3, file4, file5, file6)
    }

    private fun sendDataToServer(
        token: RequestBody,
        titleDonation: RequestBody,
        descDonation: RequestBody,
        targetDonation: RequestBody,
        dateDonation: RequestBody,
        file1: MultipartBody.Part,
        file2: MultipartBody.Part,
        file3: MultipartBody.Part,
        file4: MultipartBody.Part,
        file5: MultipartBody.Part,
        file6: MultipartBody.Part
    ){
        submissionMedicalApiViewModel.getPredictResult(token, file6).observe(this) {
            if (it != null) {
                when(it) {
                    is Results.Loading -> {
                        binding.loadingLottie.visibility = View.VISIBLE
                    }
                    is Results.Error -> {
                        binding.loadingLottie.visibility = View.GONE
                        sendData(token, titleDonation, descDonation, targetDonation, dateDonation, file1, file2, file3, file4, file5, "Normal", "Normal")
                    }
                    is Results.Success -> {
                        binding.loadingLottie.visibility = View.GONE
                        val dataAlzheimer = it.data.predictionAlzheimer
                        val dataLung = it.data.predictionLung
                        showMessage(true, "Successfully detecting the form")
                        sendData(token, titleDonation, descDonation, targetDonation, dateDonation, file1, file2, file3, file4, file5, dataAlzheimer, dataLung)
                    }
                }
            }
        }
    }

    private fun sendData(
        token: RequestBody,
        titleDonation: RequestBody,
        descDonation: RequestBody,
        targetDonation: RequestBody,
        dateDonation: RequestBody,
        file1: MultipartBody.Part,
        file2: MultipartBody.Part,
        file3: MultipartBody.Part,
        file4: MultipartBody.Part,
        file5: MultipartBody.Part,
        alzheimer: String?,
        lung: String?
    ) {

        donateViewModel.submitDonation(
            token,
            titleDonation,
            targetDonation,
            dateDonation,
            descDonation,
            file1,
            file4,
            file2,
            file3,
            file5,
            alzheimer!!.toRequestBody("text/plain".toMediaTypeOrNull()),
            lung!!.toRequestBody("text/plain".toMediaTypeOrNull()),
            type_disaster!!.toRequestBody("text/plain".toMediaTypeOrNull())
        ).observe(this){ result ->
            if (result != null) {
                when(result) {
                    is Results.Loading -> {
                        binding.loadingLottie.visibility = View.VISIBLE
                    }
                    is Results.Error -> {
                        binding.loadingLottie.visibility = View.GONE
                        showMessage(true, "Server Submit Donation Error")
                    }
                    is Results.Success -> {
                        binding.loadingLottie.visibility = View.GONE
                        val data = result.data.data
                        showMessage(true, "Submission successfully")
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    private fun startGallery(check : Int) {
        checkGallery = check
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

            if (checkGallery == 1) {
                getFile1 = myFile
                binding.subdonationImgPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.subdonationImgPreview.setImageURI(selectedImg)
            } else if (checkGallery == 2) {
                getFile2 = myFile
                binding.subdonationImgDiagnosaPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.subdonationImgDiagnosaPreview.setImageURI(selectedImg)
            } else if (checkGallery == 3) {
                getFile3 = myFile
                binding.subdonationImgLabPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.subdonationImgLabPreview.setImageURI(selectedImg)
            } else if (checkGallery == 4) {
                getFile4 = myFile
                binding.subdonationImgKtpPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.subdonationImgKtpPreview.setImageURI(selectedImg)
            } else if (checkGallery == 5) {
                getFile5 = myFile
                binding.subdonationImgSktmPreview.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.subdonationImgSktmPreview.setImageURI(selectedImg)
            }
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

            getFile1 = myFile
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(it)
            binding.subdonationEtDate.setText(date)
            dateDonate = date
        }
    }

    private fun reduceImage(file: File) : File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 70
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

    private fun showMessage(check: Boolean, message: String) {
        if (check) {
            MotionToast.createColorToast(
                this,
                "Success",
                message,
                MotionToastStyle.SUCCESS,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        } else {
            MotionToast.createColorToast(
                this,
                "Failed",
                message,
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.SHORT_DURATION,
                ResourcesCompat.getFont(this, R.font.poppins_medium)
            )
        }
    }


}
