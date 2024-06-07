package sir.mazer.ledpanel.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import sir.mazer.core.room.models.PanelData
import sir.mazer.ledpanel.R
import sir.mazer.ledpanel.ui.theme.LEDColors
import sir.mazer.ledpanel.ui.theme.LEDFonts
import sir.mazer.ledpanel.ui.theme.spacing

@Composable
fun PanelDemonstrationScreen(
    modifier: Modifier = Modifier,
    data: PanelData,
    textStyles: List<LEDFonts>,
    backgrounds: List<LEDColors>,
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val textWidth = data.text.length * spToDp(data.textSize.sp.value, context)
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val scrollStartPosition = when (data.scrollType) {
        LEDPanelScrollDirections.START_TO_END.key -> -textWidth
        LEDPanelScrollDirections.END_TO_START.key -> screenWidth.toFloat()
        else -> 0f
    }
    val scrollEndPosition = when (data.scrollType) {
        LEDPanelScrollDirections.START_TO_END.key -> screenWidth.toFloat()
        LEDPanelScrollDirections.END_TO_START.key -> -textWidth
        else -> 0f
    }
    val isAnimated = data.scrollType != LEDPanelScrollDirections.CENTER.key
    val mode = remember { mutableStateOf(RepeatMode.Restart) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetAnimation = infiniteTransition.animateValue(
        initialValue = scrollStartPosition.dp,
        targetValue = scrollEndPosition.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(data.speed, easing = LinearEasing, delayMillis = 0),
            repeatMode = mode.value
        ),
        label = ""
    )
    val showMenu = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = showMenu.value) {
        if (showMenu.value) {
            delay(2000)
            showMenu.value = false
        }
    }

    Box(
        modifier = modifier
            .background(color = Color(backgrounds[data.backgroundIndex].color))
            .clickable { showMenu.value = true }
    ) {
        LazyRow(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            userScrollEnabled = false
        ) {
            item {
                Text(
                    text = data.text,
                    color = Color(backgrounds[data.textColorIndex].color),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier.offset(x = if (isAnimated) offsetAnimation.value else 0.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        shadow =
                        if (data.isGlowingText) Shadow(
                            color = Color(backgrounds[data.textColorIndex].color),
                            blurRadius = 20f
                        ) else null,
                        fontFamily = textStyles[data.textStyleIndex].font,
                        fontSize = data.textSize.sp
                    )
                )
            }
        }
        if (data.isBlink) {
            //draw bg on the screen with frequency
            val drawBlink = remember { mutableStateOf(true) }
            if (drawBlink.value)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(backgrounds[data.backgroundIndex].color))
                )
            LaunchedEffect(Unit) {
                while (true) {
                    delay(data.blinkFrequency)
                    drawBlink.value = !drawBlink.value
                }
            }
        }
        if (data.showCells) {
            //Grid pattern
            val gridColor = Color.Black
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = {
                    val cellSize = 8.dp.toPx()
                    val horizontalLines = size.height / cellSize
                    val verticalLines = size.width / cellSize

                    for (i in 0..horizontalLines.toInt()) {
                        drawLine(
                            start = Offset(x = 0f, y = cellSize * i),
                            end = Offset(x = size.width, y = cellSize * i),
                            color = gridColor,
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    for (i in 0..verticalLines.toInt()) {
                        drawLine(
                            start = Offset(x = cellSize * i, y = 0f),
                            end = Offset(x = cellSize * i, y = size.height),
                            color = gridColor,
                            strokeWidth = 1.dp.toPx()
                        )
                    }
                }
            )
        }
        AnimatedVisibility(
            visible = showMenu.value,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(MaterialTheme.spacing.medium),
            label = "Show menu"
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "",
                tint = Color(backgrounds[data.textColorIndex].color),
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onNavBack() }
                    .align(Alignment.TopStart)
            )
        }
    }

}
