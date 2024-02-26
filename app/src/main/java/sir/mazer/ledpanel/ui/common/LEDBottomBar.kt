package sir.mazer.ledpanel.ui.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sir.mazer.ledpanel.ui.navigation.BottomBarDestinations
import sir.mazer.ledpanel.ui.theme.spacing

@Composable
fun LEDBottomBar(
    modifier: Modifier = Modifier,
    selectedButton: BottomBarDestinations,
    buttons: List<BottomBarDestinations>,
    onNavigate: (String) -> Unit
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface)
            .padding(MaterialTheme.spacing.medium),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEach { data ->
            BottomBarButton(
                iconResource = data.iconId,
                text = stringResource(id = data.titleResource),
                isSelected = data == selectedButton,
                onClick = { onNavigate(data.route) }
            )
        }
    }
}

@Composable
fun BottomBarButton(
    modifier: Modifier = Modifier,
    iconResource: Int,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        label = ""
    )
    val contentColor = animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        label = ""
    )

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = backgroundColor.value,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(!isSelected) { onClick() }
            .padding(
                vertical = MaterialTheme.spacing.small,
                horizontal = MaterialTheme.spacing.medium
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconResource),
            contentDescription = "$text button",
            tint = contentColor.value,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = contentColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = MaterialTheme.spacing.small)
        )
    }
}