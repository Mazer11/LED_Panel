package sir.mazer.ledpanel.ui.navigation

sealed class NavigationRoutes(val route: String) {
    //Splash
    data object SPLASH : NavigationRoutes("led_splash")

    //Main panel graph-----------------------------------------------------------------
    data object GRAPH_MAIN : NavigationRoutes("led_graph_main")

    //Main screen
    data object GRAPH_MAIN_START : NavigationRoutes("led_graph_main_start")

    //Show panel
    data object GRAPH_MAIN_PANEL : NavigationRoutes("led_graph_main_panel")

    //Setup panel
    data object GRAPH_MAIN_SETUP : NavigationRoutes("led_graph_main_setup")

    //Menu graph-----------------------------------------------------------------------
    data object GRAPH_MENU : NavigationRoutes("led_graph_menu")

    //Menu screen
    data object GRAPH_MENU_START : NavigationRoutes("led_graph_menu_start")

    //About
    data object GRAPH_MENU_ABOUT : NavigationRoutes("led_graph_menu_about")

}