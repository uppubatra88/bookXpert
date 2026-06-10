package com.bookxpert.upasnaprojectss.platform

import androidx.compose.runtime.Composable
import com.bookxpert.upasnaprojectss.domain.ResumeDocument

data class PickedImage(val localPath: String)

expect class PlatformFilePicker {
    fun takePhoto()
    fun chooseFromGallery()
    fun chooseDocument()
}

@Composable
expect fun rememberPlatformFilePicker(
    onImagePicked: (PickedImage) -> Unit,
    onDocumentPicked: (ResumeDocument) -> Unit,
    onError: (String) -> Unit
): PlatformFilePicker
