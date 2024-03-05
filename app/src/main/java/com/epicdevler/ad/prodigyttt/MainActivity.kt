package com.epicdevler.ad.prodigyttt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.epicdevler.ad.prodigyttt.ui.screen.GameScreen
import com.epicdevler.ad.prodigyttt.ui.screen.StartScreen
import com.epicdevler.ad.prodigyttt.ui.theme.ProdigyTTTTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val context = LocalContext.current
    val controller = rememberNavController()
    val startRoute = "start"

    var wasUp by rememberSaveable {
        mutableStateOf(false)
    }

    ProdigyTTTTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavHost(navController = controller, startDestination = startRoute) {
                composable(route = "start"){
                    StartScreen(
                        wasInGame = wasUp,
                        onNavigate = {
                            controller.navigate(it)
                        },
                        exitApp = {
                            (context as MainActivity).finish()
                        }
                    )
                }
                composable(route = "game"){
                    GameScreen(
                        onNavigateUp = {
                            controller.navigateUp()
                        },
                        onLoad = {
                            wasUp = true
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 33)
@Composable
fun AppPreview() {
    App()
}