package com.epicdevler.ad.prodigyttt.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    onNavigate: (String) -> Unit,
    exitApp: () -> Unit,
    wasInGame: Boolean
) {


    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Tic Tac Toe.",
            textAlign = TextAlign.Center,
            style = typography.displayMedium
        )
        Text(
            text = "A Classic Game",
            textAlign = TextAlign.Center,
            style = typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(50.dp))

        ElevatedButton(
            onClick = { onNavigate("game") },
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 0.dp,
                focusedElevation = 0.dp,
                hoveredElevation = 0.dp,
                disabledElevation = 0.dp,
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = colorScheme.primary,
                contentColor = colorScheme.onPrimary
            ),
            modifier = Modifier.fillMaxWidth(.7f)
        ) {
            Text(text = "Play")
        }
        if (wasInGame) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(.7f),
                onClick = exitApp,
                colors = ButtonDefaults.outlinedButtonColors()
            ) {
                Text(text = "Exit Game")
            }
        }
    }


}