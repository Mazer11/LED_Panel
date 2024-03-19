package sir.mazer.ledpanel.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltipBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import sir.mazer.core.room.models.PanelData
import sir.mazer.ledpanel.R
import sir.mazer.ledpanel.ui.screens.main.common.SmallPanel
import sir.mazer.ledpanel.ui.theme.LEDColors
import sir.mazer.ledpanel.ui.theme.LEDFonts
import sir.mazer.ledpanel.ui.theme.LEDPanelTheme
import sir.mazer.ledpanel.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedPanelsScreen(
    modifier: Modifier = Modifier,
    panels: List<PanelData>,
    styles: List<LEDFonts>,
    backgrounds: List<LEDColors>,
    onPanelClick: (PanelData) -> Unit,
    onDeletePanel: (PanelData) -> Unit,
    onOpenEditPanel: (PanelData) -> Unit,
    onOpenCreateNewPanel: () -> Unit
) {

    Column(modifier = modifier) {
        //Header (with about to help with swipe)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(modifier = Modifier.weight(1f)) {}
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.weight(10f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.tertiary,
                        blurRadius = 20f
                    )
                ),
                color = MaterialTheme.colorScheme.tertiary
            )
            PlainTooltipBox(
                modifier = Modifier.weight(1f),
                contentColor = Color.Transparent,
                containerColor = Color.Transparent,
                shape = RoundedCornerShape(8.dp),
                tooltip = {
                    val tooltipTextColor = MaterialTheme.colorScheme.onTertiaryContainer
                    Text(
                        text = stringResource(id = R.string.main_screen_tooltip),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        modifier = Modifier
                            .padding(bottom = 4.dp)
                            .drawBehind {
                                val trianglePath = Path().apply {
                                    moveTo(size.width / 2f, 0f)
                                    lineTo(
                                        size.width / 2f - 6.dp.toPx(),
                                        4.dp.toPx()
                                    )
                                    lineTo(
                                        size.width / 2f + 6.dp.toPx(),
                                        4.dp.toPx()
                                    )
                                }
                                drawPath(
                                    path = trianglePath,
                                    color = tooltipTextColor
                                )
                            }
                            .padding(bottom = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(8.dp)
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.tooltip_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .size(24.dp)
                        .tooltipAnchor()
                )
            }
        }

        //Panels list
        LazyColumn(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.medium)
                .fillMaxWidth()
        ) {
            if (panels.isNotEmpty())
                items(panels) { data ->
                    SmallPanel(
                        text = data.text,
                        textColor = Color(backgrounds[data.textColorIndex].color),
                        textStyle = MaterialTheme.typography.titleMedium.copy(fontFamily = styles[data.textStyleIndex].font),
                        backgroundColor = Color(backgrounds[data.backgroundIndex].color),
                        onDelete = { onDeletePanel(data) },
                        onEdit = { onOpenEditPanel(data) },
                        onClick = { onPanelClick(data) },
                        modifier = Modifier
                            .padding(vertical = MaterialTheme.spacing.small)
                            .fillMaxWidth()
                    )
                }
            //Add new
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = MaterialTheme.spacing.small)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onOpenCreateNewPanel() }
                        .background(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(
                            vertical = MaterialTheme.spacing.small,
                            horizontal = MaterialTheme.spacing.medium
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plus_icon),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

    }

}

@Preview
@Composable
private fun SavedPanelsScreenPreview() {
    LEDPanelTheme {
        SavedPanelsScreen(
            panels = listOf(),
            backgrounds = listOf(),
            styles = listOf(),
            onPanelClick = {},
            onDeletePanel = {},
            onOpenEditPanel = {},
            onOpenCreateNewPanel = {},
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            .2f to MaterialTheme.colorScheme.background,
                            1f to MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                )
                .padding(MaterialTheme.spacing.medium)
        )
    }
}