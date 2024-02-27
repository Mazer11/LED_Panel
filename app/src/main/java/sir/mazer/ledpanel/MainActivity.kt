package sir.mazer.ledpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sir.mazer.ledpanel.ui.common.LEDBottomBar
import sir.mazer.ledpanel.ui.navigation.BottomBarDestinations
import sir.mazer.ledpanel.ui.navigation.LEDNavHost
import sir.mazer.ledpanel.ui.theme.LEDPanelTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LEDPanelTheme {
                val navController = rememberNavController()
                val navBackStack = navController.currentBackStackEntryAsState().value
                val showBottomBar = remember { mutableStateOf(false) }
                val selectedBottomBarButton =
                    remember { mutableStateOf(BottomBarDestinations.HOME) }
                val bottomBarButtons = BottomBarDestinations.entries.toList()
                val bottomBarRoutes = bottomBarButtons.map { it.route }

                LaunchedEffect(key1 = navBackStack) {
                    showBottomBar.value =
                        navBackStack?.destination?.parent?.route in bottomBarRoutes
                                && navBackStack?.destination?.parent?.startDestinationRoute == navBackStack?.destination?.route
                    if (showBottomBar.value) {
                        selectedBottomBarButton.value =
                            bottomBarButtons.firstOrNull { destinations ->
                                destinations.route == navBackStack?.destination?.parent?.route
                            } ?: BottomBarDestinations.HOME
                    }
                }

                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = showBottomBar.value,
                            enter = fadeIn(tween(500)),
                            exit = fadeOut(tween(500))
                        ) {
                            LEDBottomBar(
                                selectedButton = selectedBottomBarButton.value,
                                buttons = bottomBarButtons,
                                onNavigate = { route -> navController.navigate(route) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colorStops = arrayOf(
                                    .2f to MaterialTheme.colorScheme.background,
                                    1f to MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        ),
                    containerColor = Color.Transparent
                ) { paddingValues ->
                    LEDNavHost(
                        navHostController = navController,
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}