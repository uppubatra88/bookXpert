package com.bookxpert.upasnaprojectss.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.bookxpert.upasnaprojectss.domain.ResumeDocument

actual class PlatformFilePicker internal constructor(private val onError: (String) -> Unit) {
    actual fun takePhoto() = onError("iOS camera picker placeholder: connect UIImagePickerController")
    actual fun chooseFromGallery() = onError("iOS gallery picker placeholder: connect PhotosUI")
    actual fun chooseDocument() = onError("iOS document picker placeholder: connect UIDocumentPickerViewController")
}

@Composable
actual fun rememberPlatformFilePicker(
    onImagePicked: (PickedImage) -> Unit,
    onDocumentPicked: (ResumeDocument) -> Unit,
    onError: (String) -> Unit
): PlatformFilePicker = remember { PlatformFilePicker(onError) }
