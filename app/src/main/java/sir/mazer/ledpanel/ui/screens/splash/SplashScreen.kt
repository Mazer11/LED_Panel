package sir.mazer.ledpanel.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sir.mazer.ledpanel.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onComplete: () -> Unit
) {
    val animationState = remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val animatedPosition = animateDpAsState(
        targetValue = if (animationState.value) -screenWidth.dp else screenWidth.dp,
        label = "Logo scroll animation value",
        animationSpec = tween(5000)
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.displayMedium.copy(
                shadow = Shadow(
                    color = MaterialTheme.colorScheme.tertiary,
                    blurRadius = 20f
                )
            ),
            color = MaterialTheme.colorScheme.tertiary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = animatedPosition.value),
            maxLines = 1
        )
    }

    LaunchedEffect(Unit) {
        animationState.value = true
    }
    LaunchedEffect(key1 = animatedPosition.value) {
        if (animatedPosition.value == -screenWidth.dp)
            onComplete()
    }
}