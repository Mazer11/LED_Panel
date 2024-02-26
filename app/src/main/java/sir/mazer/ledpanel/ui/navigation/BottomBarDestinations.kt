package sir.mazer.ledpanel.ui.navigation

import sir.mazer.ledpanel.R

enum class BottomBarDestinations (
    val iconId: Int,
    val titleResource: Int,
    val route: String
) {
    HOME(
        iconId = R.drawable.home_nav,
        route = NavigationRoutes.GRAPH_MAIN.route,
        titleResource = R.string.panels
    ),
    MENU(
        iconId = R.drawable.settings_nav,
        route = NavigationRoutes.GRAPH_MENU.route,
        titleResource = R.string.menu
    )
}