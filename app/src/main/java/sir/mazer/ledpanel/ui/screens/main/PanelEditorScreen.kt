package sir.mazer.ledpanel.ui.screens.main

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sir.mazer.core.room.models.PanelData
import sir.mazer.ledpanel.R
import sir.mazer.ledpanel.ui.theme.LEDColors
import sir.mazer.ledpanel.ui.theme.LEDFonts
import sir.mazer.ledpanel.ui.theme.LEDPanelTheme
import sir.mazer.ledpanel.ui.theme.spacing
import kotlin.math.roundToInt
import kotlin.math.roundToLong

@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun PanelEditorScreen(
    modifier: Modifier = Modifier,
    data: PanelData? = null,
    textStyles: List<LEDFonts>,
    backgrounds: List<LEDColors>,
    onSave: (PanelData) -> Unit,
    onNavBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    //Panel settings
    val direction = remember {
        mutableStateOf(
            data?.scrollType ?: LEDPanelScrollDirections.END_TO_START.key
        )
    }
    val text = remember { mutableStateOf(data?.text ?: "text") }
    val speedInMs = remember { mutableIntStateOf(data?.speed ?: 1000) }
    val showBlink = remember { mutableStateOf(data?.isBlink == true) }
    val blinkFrequencyMs = remember { mutableLongStateOf(data?.blinkFrequency ?: 500) }
    val selectedFontStyle = remember { mutableIntStateOf(data?.textStyleIndex ?: 0) }
    val textSize = remember { mutableIntStateOf(data?.textSize ?: 24) }
    val textColor = remember { mutableIntStateOf(data?.textColorIndex ?: 1) }
    val backgroundColor = remember { mutableIntStateOf(data?.backgroundIndex ?: 0) }
    val showCells = remember { mutableStateOf(data?.showCells == true) }
    val isGlowing = remember { mutableStateOf(data?.isGlowingText == true) }
    //Screen configuration data
    val screenConfig = LocalConfiguration.current
    val screenRatio = 9f / 16f
    val panelHeight = (screenConfig.screenWidthDp * screenRatio).dp
    //Animation
    val textWidth = text.value.length * spToDp(textSize.intValue.sp.value, context)
    val screenWidth = screenConfig.screenWidthDp
    val scrollStartPosition = when (direction.value) {
        LEDPanelScrollDirections.START_TO_END.key -> -textWidth
        LEDPanelScrollDirections.END_TO_START.key -> screenWidth.toFloat()
        else -> 0f
    }
    val scrollEndPosition = when (direction.value) {
        LEDPanelScrollDirections.START_TO_END.key -> screenWidth.toFloat()
        LEDPanelScrollDirections.END_TO_START.key -> -textWidth
        else -> 0f
    }
    val isAnimated = direction.value != LEDPanelScrollDirections.CENTER.key
    val mode = remember { mutableStateOf(RepeatMode.Restart) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offsetAnimation = infiniteTransition.animateValue(
        initialValue = scrollStartPosition.dp,
        targetValue = scrollEndPosition.dp,
        typeConverter = Dp.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(speedInMs.intValue, easing = LinearEasing, delayMillis = 0),
            repeatMode = mode.value
        ),
        label = ""
    )
    //Menu
    val menuState = rememberPagerState(pageCount = { 3 })

    Column(
        modifier = modifier
    ) {
        //Buttons nav back and save
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Icon(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "Navigate back",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable { onNavBack() }
                )
            }
            Text(
                text = stringResource(R.string.panel_editor),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(2f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.tertiary,
                        blurRadius = 20f
                    )
                )
            )
            Text(
                text = stringResource(R.string.save),
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onSave(
                            PanelData(
                                id = data?.id,
                                text = text.value,
                                textColorIndex = textColor.intValue,
                                backgroundIndex = backgroundColor.intValue,
                                textSize = textSize.intValue,
                                textStyleIndex = selectedFontStyle.intValue,
                                showCells = showCells.value,
                                scrollType = direction.value,
                                isGlowingText = isGlowing.value,
                                speed = speedInMs.intValue,
                                isBlink = showBlink.value,
                                blinkFrequency = blinkFrequencyMs.longValue
                            )
                        )
                    },
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium.copy(
                    shadow = Shadow(
                        color = MaterialTheme.colorScheme.tertiary,
                        blurRadius = 20f
                    )
                )
            )
        }

        //Preview
        Box(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.medium)
                .fillMaxWidth()
                .height(panelHeight)
                .background(color = Color(backgrounds[backgroundColor.intValue].color)),
            contentAlignment = Alignment.Center
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                userScrollEnabled = false
            ) {
                item {
                    Text(
                        text = text.value,
                        color = Color(backgrounds[textColor.intValue].color),
                        maxLines = 1,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier.offset(x = if (isAnimated) offsetAnimation.value else 0.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            shadow =
                            if (isGlowing.value) Shadow(
                                color = Color(backgrounds[textColor.intValue].color),
                                blurRadius = 20f
                            ) else null,
                            fontFamily = textStyles[selectedFontStyle.intValue].font,
                            fontSize = textSize.intValue.sp
                        )
                    )
                }
            }
            if (showBlink.value) {
                //draw bg on the screen with frequency
                val drawBlink = remember { mutableStateOf(true) }
                if (drawBlink.value)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(backgrounds[backgroundColor.intValue].color))
                    )
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(blinkFrequencyMs.longValue)
                        drawBlink.value = !drawBlink.value
                    }
                }
            }
            if (showCells.value) {
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
        }

        //Menu (Text|bg|extra)
        Column(
            modifier = Modifier
                .padding(top = MaterialTheme.spacing.medium)
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(MaterialTheme.spacing.medium)
        ) {
            //Tabrow
            TabRow(
                selectedTabIndex = menuState.currentPage,
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        height = 48.dp,
                        modifier = Modifier
                            .tabIndicatorOffset(
                                currentTabPosition = tabPositions[menuState.currentPage]
                            )
                            .clip(MaterialTheme.shapes.large)
                    )
                }
            ) {
                for (i in 0 until menuState.pageCount) {
                    val isSelected = i == menuState.currentPage
                    val contentColor = animateColorAsState(
                        targetValue = if (isSelected)
                            MaterialTheme.colorScheme.onPrimaryContainer
                        else
                            MaterialTheme.colorScheme.onSecondaryContainer,
                        label = ""
                    )
                    Tab(
                        selected = isSelected,
                        onClick = {
                            scope.launch {
                                menuState.animateScrollToPage(i)
                            }
                        },
                        enabled = !isSelected,
                        modifier = Modifier
                            .zIndex(2f)
                            .padding(
                                horizontal = MaterialTheme.spacing.small,
                                vertical = MaterialTheme.spacing.extraSmall
                            )
                    ) {
                        Text(
                            text = when (i) {
                                0 -> stringResource(R.string.text)
                                1 -> stringResource(R.string.background)
                                else -> stringResource(R.string.extra)
                            },
                            color = contentColor.value,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            //Pages
            HorizontalPager(
                state = menuState,
                userScrollEnabled = true,
                modifier = Modifier
                    .padding(top = MaterialTheme.spacing.medium)
                    .fillMaxSize()
            ) { state ->
                when (state) {
                    //Text settings: text, fontStyle, textSize, textColor, isGlowing
                    0 -> {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            //text
                            item {
                                Text(
                                    text = stringResource(R.string.panel_text),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                BasicTextField(
                                    value = text.value,
                                    onValueChange = { text.value = it },
                                    modifier = Modifier
                                        .padding(top = MaterialTheme.spacing.small)
                                        .fillMaxWidth(),
                                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                                    singleLine = true,
                                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                                    decorationBox = { content ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .border(
                                                    width = 1.dp,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                                .padding(
                                                    horizontal = MaterialTheme.spacing.medium,
                                                    vertical = MaterialTheme.spacing.medium
                                                )
                                        ) {
                                            Box(modifier = Modifier.weight(9f)) {
                                                content()
                                            }
                                            if (text.value.isNotEmpty())
                                                Box(
                                                    modifier = Modifier
                                                        .padding(start = MaterialTheme.spacing.small)
                                                        .weight(1f),
                                                    contentAlignment = Alignment.CenterEnd
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.trashcan_icon),
                                                        contentDescription = "",
                                                        tint = MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.clickable {
                                                            text.value = ""
                                                        }
                                                    )
                                                }
                                        }
                                    }
                                )
                            }

                            //fontstyle
                            item {
                                Text(
                                    text = stringResource(R.string.font_style),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                LazyRow(
                                    modifier = Modifier
                                        .padding(top = MaterialTheme.spacing.small)
                                        .fillMaxWidth()
                                ) {
                                    itemsIndexed(textStyles) { index, style ->
                                        val isSelected = index == selectedFontStyle.intValue
                                        val styleBackgroundColor = animateColorAsState(
                                            targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                            label = "${style.name} background color"
                                        )
                                        val styleTextColor = animateColorAsState(
                                            targetValue = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
                                            label = "${style.name} text color"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                                                .size(48.dp)
                                                .background(
                                                    color = styleBackgroundColor.value,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { selectedFontStyle.intValue = index },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Aa",
                                                color = styleTextColor.value,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = style.font
                                                ),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }

                            //textsize
                            item {
                                Text(
                                    text = stringResource(R.string.text_size),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                Slider(
                                    value = textSize.intValue.toFloat(),
                                    onValueChange = { newValue ->
                                        textSize.intValue = newValue.roundToInt()
                                    },
                                    valueRange = 24f..125f,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                )
                            }

                            //text color
                            item {
                                Text(
                                    text = stringResource(R.string.text_color),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                LazyRow(
                                    modifier = Modifier
                                        .padding(top = MaterialTheme.spacing.small)
                                        .fillMaxWidth()
                                ) {
                                    itemsIndexed(backgrounds) { index, style ->
                                        val isSelected = index == textColor.intValue
                                        val borderColor = animateColorAsState(
                                            targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                            label = "${style.name} border color"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    color = Color(style.color),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = borderColor.value,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable { textColor.intValue = index }
                                        )
                                    }
                                }
                            }

                            //is glowing
                            item {
                                Row(
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Switch(
                                        checked = isGlowing.value,
                                        onCheckedChange = { newValue -> isGlowing.value = newValue }
                                    )
                                    Text(
                                        text = stringResource(R.string.glowing_text),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
                                    )
                                }
                            }
                        }
                    }

                    //Bg settings: backgroundColor
                    1 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Top
                        ) {
                            //background color
                            item {
                                Text(
                                    text = stringResource(R.string.background_color),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                FlowRow(
                                    modifier = Modifier
                                        .padding(top = MaterialTheme.spacing.extraSmall)
                                        .fillMaxWidth()
                                ) {
                                    backgrounds.forEachIndexed { index, style ->
                                        val isSelected = index == backgroundColor.intValue
                                        val borderColor = animateColorAsState(
                                            targetValue = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                            label = "${style.name} border color"
                                        )

                                        Box(
                                            modifier = Modifier
                                                .padding(all = MaterialTheme.spacing.extraSmall)
                                                .size(48.dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    color = Color(style.color),
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .border(
                                                    width = 1.dp,
                                                    color = borderColor.value,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .clickable { backgroundColor.intValue = index }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    //Extra settings: direction, speed, show blink, blink frequency, show cells
                    2 -> {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {

                            //direction
                            item {
                                Text(
                                    text = stringResource(R.string.animation_direction),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .padding(top = MaterialTheme.spacing.small)
                                        .fillMaxWidth()
                                ) {
                                    val directionBackgroundColor1 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.END_TO_START.key)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.secondaryContainer,
                                        label = ""
                                    )
                                    val directionTextColor1 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.END_TO_START.key)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                        label = ""
                                    )
                                    val directionBackgroundColor2 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.CENTER.key)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.secondaryContainer,
                                        label = ""
                                    )
                                    val directionTextColor2 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.CENTER.key)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                        label = ""
                                    )
                                    val directionBackgroundColor3 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.START_TO_END.key)
                                            MaterialTheme.colorScheme.primaryContainer
                                        else
                                            MaterialTheme.colorScheme.secondaryContainer,
                                        label = ""
                                    )
                                    val directionTextColor3 = animateColorAsState(
                                        targetValue = if (direction.value == LEDPanelScrollDirections.START_TO_END.key)
                                            MaterialTheme.colorScheme.onPrimaryContainer
                                        else
                                            MaterialTheme.colorScheme.onSecondaryContainer,
                                        label = ""
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.back_icon),
                                        contentDescription = "",
                                        tint = directionTextColor1.value,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = directionBackgroundColor1.value,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                direction.value =
                                                    LEDPanelScrollDirections.END_TO_START.key
                                            }
                                            .padding(MaterialTheme.spacing.small)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_bottom_arrow),
                                        contentDescription = "",
                                        tint = directionTextColor2.value,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = directionBackgroundColor2.value,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                direction.value =
                                                    LEDPanelScrollDirections.CENTER.key
                                            }
                                            .padding(MaterialTheme.spacing.small)
                                    )
                                    Icon(
                                        painter = painterResource(id = R.drawable.forward_icon),
                                        contentDescription = "",
                                        tint = directionTextColor3.value,
                                        modifier = Modifier
                                            .size(48.dp)
                                            .background(
                                                color = directionBackgroundColor3.value,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                direction.value =
                                                    LEDPanelScrollDirections.START_TO_END.key
                                            }
                                            .padding(MaterialTheme.spacing.small)
                                    )
                                }
                            }

                            //speed
                            item {
                                Text(
                                    text = stringResource(R.string.animation_speed_slow_down),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                Slider(
                                    value = speedInMs.intValue.toFloat(),
                                    onValueChange = { newValue ->
                                        speedInMs.intValue = newValue.roundToInt()
                                    },
                                    onValueChangeFinished = {
                                        //Restart animation 'cause speed state doesn't update animation spec
                                        scope.launch {
                                            val old = direction.value
                                            direction.value = LEDPanelScrollDirections.CENTER.key
                                            delay(10)
                                            direction.value = old
                                        }
                                    },
                                    valueRange = 100f..10000f,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                )
                            }

                            //show blink
                            item {
                                Row(
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Switch(
                                        checked = showBlink.value,
                                        onCheckedChange = { newValue -> showBlink.value = newValue }
                                    )
                                    Text(
                                        text = stringResource(R.string.start_blink_effect),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(start = MaterialTheme.spacing.small)
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.warning_text),
                                    color = MaterialTheme.colorScheme.tertiary,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                )
                            }

                            //blink frequency
                            item {
                                Text(
                                    text = stringResource(R.string.blink_frequency_slow_down),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium)
                                )
                                Slider(
                                    value = blinkFrequencyMs.longValue.toFloat(),
                                    onValueChange = { newValue ->
                                        blinkFrequencyMs.longValue = newValue.roundToLong()
                                    },
                                    valueRange = 100f..1000f,
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.small)
                                )
                            }

                            //show grid
                            item {
                                Row(
                                    modifier = Modifier.padding(top = MaterialTheme.spacing.medium),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Switch(
                                        checked = showCells.value,
                                        onCheckedChange = { newValue -> showCells.value = newValue }
                                    )
                                    Text(
                                        text = stringResource(R.string.show_grid),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(start = MaterialTheme.spacing.small)
                                    )
                                }
                            }

                        }
                    }
                }
            }
        }

    }

}

enum class LEDPanelScrollDirections(val key: String) {
    START_TO_END("ste"), END_TO_START("ets"), CENTER("c")
}

fun spToDp(sp: Float, context: Context): Float {
    val scale = context.resources.displayMetrics.density / context.resources.displayMetrics.density
    return sp * scale
}

@Preview
@Composable
private fun PanelEditorScreenPreview() {
    LEDPanelTheme {
        PanelEditorScreen(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            data = null,
            textStyles = listOf(LEDFonts.DEFAULT),
            backgrounds = listOf(LEDColors.BLUE, LEDColors.BROWN),
            onSave = {},
            onNavBack = {}
        )
    }
}