package com.pomplarg.manoeuvredeforceusar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pomplarg.manoeuvredeforceusar.ui.theme.ManoeuvreDeForceUSARTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ManoeuvreDeForceUSARTheme {
                SupportingPaneSample()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UsarApp() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("USAR - Manoeuvre de force") },
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary) // Modifier applied properly
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Your action */ }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            },
            content = { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Column {
                        SupportingPaneSample()
                    }
                }
            }
        )
    }

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Composable
    fun SupportingPaneSample() {
        val navigator = rememberSupportingPaneScaffoldNavigator()

        BackHandler(navigator.canNavigateBack()) {
            navigator.navigateBack()
        }

        SupportingPaneScaffold(
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            supportingPane = {
                AnimatedPane(
                    modifier = Modifier.safeContentPadding()
                ) {
                    Column {
                        CalculateVolume()
                    }
                }
            }, mainPane = {
                AnimatedPane(
                    modifier = Modifier.safeContentPadding()
                ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                        Button(
                            onClick = {navigator.navigateTo(
                                ThreePaneScaffoldRole.Secondary
                            )},
                            modifier = Modifier.padding(8.dp).align(Alignment.End),
                        ) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text("Configuration de l'objet")
                        }
                    }
                    DisplaySchema(
                        onNavigateToSupportingPane = {
                            navigator.navigateTo(
                                ThreePaneScaffoldRole.Secondary
                            )
                        }
                    )
                }
            }
        })
    }
}