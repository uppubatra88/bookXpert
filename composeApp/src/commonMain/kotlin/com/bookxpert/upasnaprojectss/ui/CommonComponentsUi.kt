package com.bookxpert.upasnaprojectss.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Employee
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.active_status
import upasnaemployeekmp.composeapp.generated.resources.empty_search_hint
import upasnaemployeekmp.composeapp.generated.resources.inactive_status
import upasnaemployeekmp.composeapp.generated.resources.no_employees_found

internal fun Employee.initials(): String = fullName.initialsFromName()

internal fun String.initialsFromName(): String =
    trim().split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "EP" }

// ─── Color Schemes ────────────────────────────────────────────────────────────

internal fun lightAppColors() = androidx.compose.material3.lightColorScheme(
    primary = Color(0xFF1B6B53),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD2F0E4),
    onPrimaryContainer = Color(0xFF00391F),
    secondary = Color(0xFF4D6357),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD0E8D8),
    tertiary = Color(0xFF3D6373),
    background = Color(0xFFF4F8F6),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFEEF3F0),
    onSurfaceVariant = Color(0xFF5E7568),
    outline = Color(0xFFB0C4BA),
    outlineVariant = Color(0xFFD8E8E0)
)

internal fun darkAppColors() = androidx.compose.material3.darkColorScheme(
    primary = Color(0xFF7DD8BA),
    onPrimary = Color(0xFF003826),
    primaryContainer = Color(0xFF00513A),
    onPrimaryContainer = Color(0xFF9AF4D6),
    secondary = Color(0xFFB5CCBE),
    background = Color(0xFF0C1410),
    surface = Color(0xFF151D18),
    surfaceVariant = Color(0xFF1E2922),
    onSurfaceVariant = Color(0xFFAAC4B3),
    outline = Color(0xFF3D5145),
    outlineVariant = Color(0xFF2A3D33)
)
internal fun appTypography() = androidx.compose.material3.Typography()

internal fun highlight(text: String, query: String) = buildAnnotatedString {
    val q = query.trim()
    val index = text.lowercase().indexOf(q.lowercase())
    if (q.isBlank() || index < 0) {
        append(text)
    } else {
        append(text.substring(0, index))
        withStyle(SpanStyle(background = Color(0xFFFFE082), fontWeight = FontWeight.Bold)) {
            append(text.substring(index, index + q.length))
        }
        append(text.substring(index + q.length))
    }
}

@Composable
internal fun EmptyState() {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier.size(72.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(stringResource(Res.string.no_employees_found), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                stringResource(Res.string.empty_search_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
internal fun StatusPill(active: Boolean) {
    val bg by animateColorAsState(if (active) Color(0xFF2E7D32).copy(alpha = 0.12f) else Color(0xFFC62828).copy(alpha = 0.12f))
    val fg = if (active) Color(0xFF2E7D32) else Color(0xFFC62828)
    Surface(shape = RoundedCornerShape(50), color = bg) {
        Row(
            Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Box(Modifier.size(7.dp).clip(CircleShape).background(fg))
            Text(
                if (active) stringResource(Res.string.active_status) else stringResource(Res.string.inactive_status),
                style = MaterialTheme.typography.labelSmall,
                color = fg,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
