package com.vkolisnichenko.captureyourlife.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vkolisnichenko.captureyourlife.presentation.navigation.AppNavHost
import com.vkolisnichenko.captureyourlife.presentation.navigation.Screens
import com.vkolisnichenko.captureyourlife.presentation.theme.CaptureYourLifeTheme
import com.vkolisnichenko.core.domain.model.MediaModel
import com.vkolisnichenko.core.domain.repository.CameraController
import com.vkolisnichenko.core.domain.repository.MediaRepository
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity(), CameraController {

    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var recordVideoLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var navController: NavHostController
    private var photoUri: Uri? = null
    private var videoUri: Uri? = null
    private var isPhotoSelected = true

    private val showPermissionDeniedDialog = mutableStateOf(false)

    @Inject
    lateinit var mediaRepository: MediaRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleActivityResults()
        setContent {
            navController = rememberNavController()
            CaptureYourLifeTheme {
                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            modifier = Modifier.padding(innerPadding),
                            cameraController = this@MainActivity
                        )
                    }

                    if (showPermissionDeniedDialog.value) {
                        PermissionDeniedDialog(onDismiss = {
                            showPermissionDeniedDialog.value = false
                        }, onOpenSettings = {
                            openSettings()
                        })
                    }
                }
            }
        }
    }

    private fun openSettings() {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        startActivity(intent)
    }

    private fun handleActivityResults() {
        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                if (isPhotoSelected) openCameraForPhoto()
                else openCameraForVideo()
            } else {
                showPermissionDeniedDialog.value = true
            }
        }

        takePhotoLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                photoUri?.let {
                    navigateToMediaConfirmation(it, true)
                }
            }
        }

        recordVideoLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                videoUri?.let {
                    navigateToMediaConfirmation(it, false)
                }
            }
        }
    }

    private fun navigateToMediaConfirmation(it: Uri, isPhoto: Boolean) {
        mediaRepository.saveMedia(MediaModel(mediaUri = it, isPhoto = isPhoto))
        navController.navigate("${Screens.MediaConfirmation.route}/${false}")
    }

    private fun isCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun openCameraForPhoto() {
        if (isCameraPermission()) {
            val photoFile = createImageFile()
            photoUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                photoFile
            )

            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            takePhotoLauncher.launch(takePictureIntent)

        } else {
            isPhotoSelected = true
            requestPermissionLauncher.launch(
                android.Manifest.permission.CAMERA
            )
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    override fun openCameraForVideo() {
        if (isCameraPermission()) {
            val videoFile = createVideoFile()
            videoUri = FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.fileprovider",
                videoFile
            )

            val recordVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, videoUri)
                addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
            recordVideoLauncher.launch(recordVideoIntent)
        } else {
            isPhotoSelected = false
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun createVideoFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        return File.createTempFile(
            "VID_${timeStamp}_",
            ".mp4",
            storageDir
        )
    }

}
