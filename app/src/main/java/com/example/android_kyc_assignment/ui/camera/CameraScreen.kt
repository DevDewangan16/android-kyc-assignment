package com.example.android_kyc_assignment.ui.camera

import android.Manifest
import android.graphics.Bitmap
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.android_kyc_assignment.ui.theme.Black
import com.example.android_kyc_assignment.ui.theme.White
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.isGranted
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    customerId: Int,
    onSelfieCaptured: (Bitmap) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }
    var isFrontCamera by remember { mutableStateOf(true) }
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // State for captured photo
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var showPreview by remember { mutableStateOf(false) }

    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    fun startCameraWithLens(useFrontCamera: Boolean) {
        val provider = cameraProvider ?: return
        try {
            provider.unbindAll()

            val previewBuilder = Preview.Builder().build().also {
                preview = it
            }

            val imageCaptureBuilder = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(android.view.Surface.ROTATION_0)
                .build()
                .also {
                    imageCapture = it
                }

            val cameraSelector = if (useFrontCamera) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            provider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                previewBuilder,
                imageCaptureBuilder
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(cameraPermissionState.status.isGranted) {
        if (cameraPermissionState.status.isGranted) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    cameraProvider = cameraProviderFuture.get()
                    startCameraWithLens(isFrontCamera)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(context))
        }
    }

    LaunchedEffect(isFrontCamera) {
        if (cameraPermissionState.status.isGranted && cameraProvider != null) {
            startCameraWithLens(isFrontCamera)
        }
    }

    // Handle captured photo
    fun handleCapture(bitmap: Bitmap) {
        capturedBitmap = bitmap
        showPreview = true
    }

    // Confirm photo (save and return)
    fun confirmPhoto() {
        capturedBitmap?.let {
            onSelfieCaptured(it)
            capturedBitmap = null
            showPreview = false
        }
    }

    // Retake photo (go back to camera)
    fun retakePhoto() {
        capturedBitmap = null
        showPreview = false
        // Restart camera
        if (cameraPermissionState.status.isGranted && cameraProvider != null) {
            startCameraWithLens(isFrontCamera)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (showPreview && capturedBitmap != null) {
            // Show captured photo preview
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                // Display captured photo
                androidx.compose.foundation.Image(
                    bitmap = capturedBitmap!!.asImageBitmap(),
                    contentDescription = "Captured photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    contentScale = androidx.compose.ui.layout.ContentScale.FillWidth
                )

                // Preview controls overlay
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Instruction
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Black.copy(alpha = 0.7f),
                        modifier = Modifier.padding(bottom = 24.dp)
                    ) {
                        Text(
                            text = "Is this photo good?",
                            color = White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Retake Button
                        Button(
                            onClick = { retakePhoto() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .width(130.dp)
                                .height(48.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Retake",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "Retake",
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Confirm Button (Save)
                        Button(
                            onClick = { confirmPhoto() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00C853),
                                contentColor = White
                            ),
                            shape = CircleShape,
                            modifier = Modifier
                                .size(72.dp)
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Confirm",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        } else if (cameraPermissionState.status.isGranted) {
            // Camera Preview
            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).apply {
                        scaleType = PreviewView.ScaleType.FILL_CENTER
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { previewView ->
                    preview?.setSurfaceProvider(previewView.surfaceProvider)
                }
            )

            // Top Bar with camera switch
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = {
                        isFrontCamera = !isFrontCamera
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = White.copy(alpha = 0.9f),
                    contentColor = Black,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Icon(
                        Icons.Default.FlipCameraAndroid,
                        contentDescription = "Switch Camera",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Bottom Controls
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                // Instruction
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Black.copy(alpha = 0.7f),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Text(
                        text = if (isFrontCamera) "Position your face in the center" else "Point camera at subject",
                        color = White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cancel Button
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = White
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .width(100.dp)
                            .height(48.dp)
                    ) {
                        Text(
                            "Cancel",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Capture Button
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(
                                color = White,
                                shape = CircleShape
                            )
                            .clickable {
                                takePhoto(
                                    context = context,
                                    imageCapture = imageCapture,
                                    onCaptured = { bitmap ->
                                        handleCapture(bitmap)
                                    }
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .background(
                                    color = Black,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
        }

        // Permission Denied UI
        if (!cameraPermissionState.status.isGranted) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Black),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "📷 Camera Required",
                        style = MaterialTheme.typography.headlineMedium,
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Camera permission is needed to capture your selfie for KYC verification.",
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { cameraPermissionState.launchPermissionRequest() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = White,
                            contentColor = Black
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth(0.6f)
                    ) {
                        Text(
                            "Grant Permission",
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
            cameraProvider?.unbindAll()
        }
    }
}

private fun takePhoto(
    context: android.content.Context,
    imageCapture: ImageCapture?,
    onCaptured: (Bitmap) -> Unit
) {
    if (imageCapture == null) return

    imageCapture.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(imageProxy: ImageProxy) {
                val bitmap = imageProxy.toBitmap()
                imageProxy.close()
                bitmap?.let { onCaptured(it) }
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
            }
        }
    )
}