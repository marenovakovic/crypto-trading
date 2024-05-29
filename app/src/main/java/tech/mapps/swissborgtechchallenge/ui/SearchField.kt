package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.mapps.swissborgtechchallenge.R
import tech.mapps.swissborgtechchallenge.ui.theme.SwissborgTechChallengeTheme

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
) {
    OutlinedTextField(
        label = { Text(text = stringResource(R.string.ticker)) },
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(color = MaterialTheme.colorScheme.background),
    )
}

@Preview
@Composable
private fun SearchFieldEmptyPreview() {
    SwissborgTechChallengeTheme {
        SearchField(
            query = "",
            onQueryChange = {},
        )
    }
}

@Preview
@Composable
private fun SearchFieldFilledInPreview() {
    SwissborgTechChallengeTheme {
        SearchField(
            query = "BTC",
            onQueryChange = {},
        )
    }
}