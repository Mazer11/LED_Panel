package sir.mazer.ledpanel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import sir.mazer.ledpanel.ui.navigation.LEDNavigationHostController
import sir.mazer.ledpanel.ui.theme.LEDPanelTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LEDPanelTheme {
                val navController = rememberNavController()

                Scaffold(
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
                    LEDNavigationHostController(
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