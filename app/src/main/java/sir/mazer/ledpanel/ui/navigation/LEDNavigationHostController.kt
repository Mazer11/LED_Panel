package sir.mazer.ledpanel.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import sir.mazer.ledpanel.ui.screens.splash.SplashScreen

@Composable
fun LEDNavigationHostController(
    navHostController: NavHostController,
    modifier: Modifier
) {
    val startDestination = NavigationRoutes.SPLASH.route

    NavHost(navController = navHostController, startDestination = startDestination) {
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