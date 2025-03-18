package com.pomplarg.manoeuvredeforceusar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
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

    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    @Composable
    fun SupportingPaneSample() {
        val navigator = rememberSupportingPaneScaffoldNavigator()
        val volumeViewModel: VolumeViewModel = viewModel()

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
                        CalculateVolume(volumeViewModel, navigator)
                    }
                }
            }, mainPane = {
                AnimatedPane(
                    modifier = Modifier.safeContentPadding()
                ) {
                Column {
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
                    DisplaySchema(volumeViewModel)
                }
            }
        })
    }
}