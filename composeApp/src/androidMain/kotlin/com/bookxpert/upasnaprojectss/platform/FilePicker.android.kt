package com.bookxpert.upasnaprojectss.platform

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bookxpert.upasnaprojectss.domain.ResumeDocument
import java.io.File
import java.io.FileOutputStream

actual class PlatformFilePicker internal constructor(
    private val launchCamera: () -> Unit,
    private val launchGallery: () -> Unit,
    private val launchDocument: () -> Unit
) {
    actual fun takePhoto() = launchCamera()
    actual fun chooseFromGallery() = launchGallery()
    actual fun chooseDocument() = launchDocument()
}

@Composable
actual fun rememberPlatformFilePicker(
    onImagePicked: (PickedImage) -> Unit,
    onDocumentPicked: (ResumeDocument) -> Unit,
    onError: (String) -> Unit
): PlatformFilePicker {
    val context = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap == null) onError("Camera cancelled") else onImagePicked(PickedImage(context.saveBitmap(bitmap)))
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) cameraLauncher.launch(null) else onError("Camera permission denied")
    }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onImagePicked(PickedImage(context.copyUriToCache(it, "profile"))) } ?: onError("No image selected")
    }
    val galleryPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted || Build.VERSION.SDK_INT < 33) galleryLauncher.launch("image/*") else onError("Photo permission denied")
    }
    val docLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val meta = context.readDocumentMeta(it)
            val path = context.copyUriToCache(it, "resume")
            onDocumentPicked(meta.copy(path = path))
        } ?: onError("No document selected")
    }
    return remember {
        PlatformFilePicker(
            launchCamera = { cameraPermission.launch(Manifest.permission.CAMERA) },
            launchGallery = {
                if (Build.VERSION.SDK_INT >= 33) galleryPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                else galleryLauncher.launch("image/*")
            },
            launchDocument = {
                docLauncher.launch(arrayOf(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                ))
            }
        )
    }
}

private fun Context.saveBitmap(bitmap: Bitmap): String {
    val file = File(cacheDir, "profile_${System.currentTimeMillis()}.jpg")
    FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.JPEG, 92, it) }
    return file.absolutePath
}

private fun Context.copyUriToCache(uri: Uri, prefix: String): String {
    val name = queryName(uri).ifBlank { "$prefix-${System.currentTimeMillis()}" }
    val extension = name.substringAfterLast('.', "")
    val file = File(cacheDir, "${prefix}_${System.currentTimeMillis()}${if (extension.isBlank()) "" else ".$extension"}")
    contentResolver.openInputStream(uri).use { input ->
        FileOutputStream(file).use { output -> input?.copyTo(output) }
    }
    return file.absolutePath
}

private fun Context.readDocumentMeta(uri: Uri): ResumeDocument {
    val name = queryName(uri).ifBlank { "resume" }
    val size = querySize(uri)
    val mime = contentResolver.getType(uri).orEmpty()
    return ResumeDocument(path = uri.toString(), name = name, sizeBytes = size, mimeType = mime)
}

private fun Context.queryName(uri: Uri): String {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (index >= 0 && cursor.moveToFirst()) return cursor.getString(index)
    }
    return uri.lastPathSegment.orEmpty()
}

private fun Context.querySize(uri: Uri): Long {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val index = cursor.getColumnIndex(OpenableColumns.SIZE)
        if (index >= 0 && cursor.moveToFirst()) return cursor.getLong(index)
    }
    return 0L
}
