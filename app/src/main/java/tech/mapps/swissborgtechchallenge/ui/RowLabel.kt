package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.mapps.swissborgtechchallenge.ui.theme.SwissborgTechChallengeTheme

@Composable
fun RowLabel(
    text: String,
    imageVector: ImageVector?,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick,
        ),
    ) {
        Text(text = text)
        Spacer(modifier = Modifier.width(2.dp))
        Box(modifier = Modifier.size(24.dp)) {
            if (imageVector != null)
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                )
        }
    }
}

@Preview
@Composable
private fun RowLabelPreview() {
    SwissborgTechChallengeTheme {
        RowLabel(
            text = "Price",
            imageVector = Icons.Default.ArrowDownward,
            onClick = {},
        )
    }
}