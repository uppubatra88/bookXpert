# Upasna Employee KMP

Kotlin Multiplatform employee profile management app for package `com.bookxpert.upasnaprojectss`.

## Included

- Compose Multiplatform shared UI for Android and iOS.
- MVVM with `EmployeeViewModel`, `StateFlow`, debounced search, filters, sort, and top earners.
- Koin DI in `commonMain`.
- Room KMP entity, DAO, converters, repository, and bundled SQLite driver.
- Android camera, gallery, and Documents picker implementation.
- Resume validation for PDF/DOC/DOCX metadata and 5 MB size limit.
- Employee form validation, duplicate email/phone inline errors, and normalized phone storage.
- DSA requirements in `commonMain`: HashSet duplicate index, fixed-size min-heap top salaries, undo stack.
- Common unit tests for algorithms, phone normalization, undo, and validation.

## Open in Android Studio

1. Open this folder as a Gradle project.
2. Let Android Studio sync dependencies.
3. Run the `composeApp` Android configuration.

The Gradle wrapper properties are included. If `gradle-wrapper.jar` is missing, Android Studio can still import the project with its bundled Gradle, or you can regenerate the wrapper from a machine with Gradle installed.

## Important Files

- Shared UI: `composeApp/src/commonMain/kotlin/com/bookxpert/upasnaprojectss/ui/App.kt`
- ViewModel: `composeApp/src/commonMain/kotlin/com/bookxpert/upasnaprojectss/presentation/EmployeeViewModel.kt`
- Algorithms: `composeApp/src/commonMain/kotlin/com/bookxpert/upasnaprojectss/algorithms/EmployeeAlgorithms.kt`
- Room: `composeApp/src/commonMain/kotlin/com/bookxpert/upasnaprojectss/data`
- Android pickers: `composeApp/src/androidMain/kotlin/com/bookxpert/upasnaprojectss/platform/FilePicker.android.kt`

## Notes

Android camera/gallery/document picking is implemented. iOS has compile-time `actual` placeholders for picker hooks; connect `PhotosUI`, `UIImagePickerController`, and `UIDocumentPickerViewController` for production iOS uploads.
