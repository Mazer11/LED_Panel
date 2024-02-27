package sir.mazer.ledpanel.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import sir.mazer.ledpanel.ui.common.ErrorScreen
import sir.mazer.ledpanel.ui.common.LoadingScreen
import sir.mazer.ledpanel.ui.screens.main.SavedPanelsScreen
import sir.mazer.ledpanel.ui.screens.main.states.SavedPanelsScreenState
import sir.mazer.ledpanel.ui.screens.main.vm.MainViewModel
import sir.mazer.ledpanel.ui.screens.splash.SplashScreen
import sir.mazer.ledpanel.ui.theme.spacing

@Composable
fun LEDNavHost(
    navHostController: NavHostController,
    modifier: Modifier
) {
    val startDestination = NavigationRoutes.SPLASH.route

    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = NavigationRoutes.SPLASH.route) {
            SplashScreen(
                modifier = Modifier.fillMaxSize(),
                onComplete = {
                    navHostController.navigate(NavigationRoutes.GRAPH_MAIN.route)
                }
            )
        }
        mainGraph(navHostController)
        menuGraph(navHostController)
    }

}

fun NavGraphBuilder.mainGraph(
    navController: NavController
) {
    navigation(
        startDestination = NavigationRoutes.GRAPH_MAIN_START.route,
        route = NavigationRoutes.GRAPH_MAIN.route
    ) {
        composable(route = NavigationRoutes.GRAPH_MAIN_START.route) {
            val vm = it.sharedViewModel<MainViewModel>(navController = navController)
            val screenState = vm.savedPanelsScreenState.collectAsState()

            AnimatedContent(
                targetState = screenState.value,
                label = "",
                transitionSpec = {
                    fadeIn(tween(500)) togetherWith fadeOut(tween(500))
                }
            ) { state ->
                when (state) {
                    is SavedPanelsScreenState.Loading -> {
                        LoadingScreen(modifier = Modifier.fillMaxSize())
                    }

                    is SavedPanelsScreenState.Error -> {
                        ErrorScreen(
                            message = state.message,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .fillMaxSize()
                        )
                    }

                    is SavedPanelsScreenState.Success -> {
                        SavedPanelsScreen(
                            panels = state.panels,
                            styles = vm.styles,
                            backgrounds = vm.backgrounds,
                            onPanelClick = { panel ->
                                vm.onPanelClick(panel)
                                navController.navigate(NavigationRoutes.GRAPH_MAIN_PANEL.route)
                            },
                            onDeletePanel = { panel -> vm.onDeletePanel(panel) },
                            onOpenEditPanel = { panel ->
                                vm.onOpenEditPanel(panel)
                                navController.navigate(NavigationRoutes.GRAPH_MAIN_SETUP.route)
                            },
                            onOpenCreateNewPanel = {
                                navController.navigate(NavigationRoutes.GRAPH_MAIN_SETUP.route)
                            },
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .fillMaxSize()
                        )
                    }
                }
            }

        }

        composable(route = NavigationRoutes.GRAPH_MAIN_PANEL.route) {

        }

        composable(route = NavigationRoutes.GRAPH_MAIN_SETUP.route) {

        }
    }
}

fun NavGraphBuilder.menuGraph(
    navController: NavController
) {
    navigation(
        startDestination = NavigationRoutes.GRAPH_MENU_START.route,
        route = NavigationRoutes.GRAPH_MENU.route
    ) {
        composable(route = NavigationRoutes.GRAPH_MENU_START.route) {

        }

        composable(route = NavigationRoutes.GRAPH_MENU_ABOUT.route) {

        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val currentNavGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(currentNavGraphRoute)
    }
    return hiltViewModel(parentEntry)
}