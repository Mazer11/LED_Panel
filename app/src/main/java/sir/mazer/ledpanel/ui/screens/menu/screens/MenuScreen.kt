package sir.mazer.ledpanel.ui.screens.menu.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import sir.mazer.ledpanel.R
import sir.mazer.ledpanel.ui.theme.spacing
import sir.mazer.ledpanel.utils.Constants
import java.util.Locale

@Composable
fun MenuScreen(
    modifier: Modifier = Modifier,
    currentLanguage: String,
    onNewLanguage: (String) -> Unit
) {
    val showLanguageDialog = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
    ) {
        MenuItem(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = { showLanguageDialog.value = true }
        ) {
            Text(
                text = stringResource(R.string.change_language),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }

    if (showLanguageDialog.value)
        SelectLanguageDialog(
            currentLanguage = currentLanguage,
            onNewLanguage = onNewLanguage,
            onDismissRequest = { showLanguageDialog.value = false }
        )
}

@Composable
private fun MenuItem(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(
                vertical = MaterialTheme.spacing.medium,
                horizontal = MaterialTheme.spacing.large
            )
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectLanguageDialog(
    modifier: Modifier = Modifier,
    currentLanguage: String,
    onNewLanguage: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val appLanguages = Constants.appLocales
    val selectedButton = remember { mutableStateOf(currentLanguage) }

    ModalBottomSheet(
        onDismissRequest = { onDismissRequest() },
        shape = MaterialTheme.shapes.small,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
        dragHandle = {},
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Text(
            text = stringResource(R.string.select_language),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(MaterialTheme.spacing.medium)
        )
        appLanguages.forEach { locale ->
            val isSelected = locale == selectedButton.value
            val fullLocaleName = Locale(locale).displayName

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(
                        vertical = MaterialTheme.spacing.small,
                        horizontal = MaterialTheme.spacing.medium
                    )
                    .fillMaxWidth()
                    .clickable {
                        selectedButton.value = locale
                        onNewLanguage(locale)
                    }
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = {
                        selectedButton.value = locale
                        onNewLanguage(locale)
                    }
                )
                Text(
                    text = fullLocaleName,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                )
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge * 2))
    }
}