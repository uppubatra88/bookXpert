package com.bookxpert.upasnaprojectss.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bookxpert.upasnaprojectss.domain.Employee
import com.bookxpert.upasnaprojectss.platform.ProfileAvatar
import org.jetbrains.compose.resources.stringResource
import upasnaemployeekmp.composeapp.generated.resources.Res
import upasnaemployeekmp.composeapp.generated.resources.salary_format

@Composable
internal fun TopEarnerCard(rank: Int, employee: Employee) {
    val isTopThree = rank <= 3
    val medalColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    ElevatedCard(
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.elevatedCardElevation(if (isTopThree) 3.dp else 1.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Rank badge
            Box(
                Modifier.size(40.dp).clip(CircleShape).background(
                    if (isTopThree) medalColor.copy(alpha = 0.15f)
                    else MaterialTheme.colorScheme.surfaceVariant
                ),
                contentAlignment = Alignment.Center
            ) {
                if (isTopThree) {
                    Icon(Icons.Default.Star, null, tint = medalColor, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        "#$rank",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            ProfileAvatar(employee.profileImagePath, employee.initials(), Modifier.size(44.dp))

            Column(Modifier.weight(1f)) {
                Text(employee.fullName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(employee.department.label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    stringResource(Res.string.salary_format, employee.salary.toInt()),
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (isTopThree) {
                    Text(
                        when (rank) { 1 -> "🥇 Gold"; 2 -> "🥈 Silver"; else -> "🥉 Bronze" },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
