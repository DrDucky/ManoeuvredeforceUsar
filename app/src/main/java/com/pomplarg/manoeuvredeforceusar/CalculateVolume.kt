package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pomplarg.manoeuvredeforceusar.ui.composables.DialogWithImage
import com.pomplarg.manoeuvredeforceusar.ui.composables.FilterChipGroup
import com.pomplarg.manoeuvredeforceusar.ui.composables.RadioButtonWithLabel
import com.pomplarg.manoeuvredeforceusar.ui.composables.SingleChoiceSegmentedButton
import java.util.Locale

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CalculateVolume(
    volumeViewModel: VolumeViewModel,
    navigator: ThreePaneScaffoldNavigator<Nothing>
) {

    val volumeUiState by volumeViewModel.uiState.collectAsState()

    val poidsEnabled = remember { mutableStateOf(true) }
    val poidsValue = remember { mutableStateOf("") }
    val rayonValue = remember { mutableStateOf("") }
    val hauteurValue = remember { mutableStateOf("") }
    val openConeDialog = remember { mutableStateOf(false) }
    val openCylinderDialog = remember { mutableStateOf(false) }
    var manualChoice by remember { mutableStateOf(true) }
    val materialItems = mapOf("Béton" to 2,
        "Béton armé" to 5)
    val supportItems = mapOf("Béton sec" to 0.8,
        "Béton mouillé" to 0.6)
    val cfcrItems = listOf("Frottement",
        "Roulement")


    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Configuration de l'objet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        RadioButtonWithLabel(
            label = "Saisie manuelle",
            selected = manualChoice,
            onClick = {
                poidsEnabled.value = true
                manualChoice = true
            }
        )
        RadioButtonWithLabel(
            label = "Saisie par calcul",
            selected = !manualChoice,
            onClick = {
                poidsEnabled.value = false
                manualChoice = false
            }
        )

        if(manualChoice) {
            OutlinedTextField(
                modifier = Modifier.padding(16.dp),
                value = poidsValue.value,
                singleLine = true,
                enabled = poidsEnabled.value,
                onValueChange = {
                    poidsValue.value = it
                    volumeViewModel.updateWeightWithManualEntry(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                label = { Text("Poids") }
            )
        }
        else {
            Row(modifier = Modifier.padding(16.dp)) {
                AssistChip(
                    onClick = { openConeDialog.value = true },
                    label = { Text("Cône") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.cone),
                            contentDescription = "cone icone",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
                AssistChip(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { openCylinderDialog.value = true },
                    label = { Text("Cylindre") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.cylinder),
                            contentDescription = "cylindre icone",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            }

            when {
                openConeDialog.value -> {
                    DialogWithImage(
                        onDismissRequest = { openConeDialog.value = false },
                        onConfirmation = {
                            openConeDialog.value = false
                            volumeViewModel.updateVolume(Volume.VolumeType.CONE, rayonValue.value.toDouble(), hauteurValue.value.toDouble())
                        },
                        painter = painterResource(id = R.drawable.volume_cone),
                        imageDescription = "",
                        rayonValue,
                        hauteurValue
                    )
                }
                openCylinderDialog.value -> {
                    DialogWithImage(
                        onDismissRequest = { openCylinderDialog.value = false },
                        onConfirmation = {
                            openCylinderDialog.value = false
                            volumeViewModel.updateVolume(Volume.VolumeType.CYLINDER, rayonValue.value.toDouble(), hauteurValue.value.toDouble())
                        },
                        painter = painterResource(id = R.drawable.volume_cylinder),
                        imageDescription = "",
                        rayonValue,
                        hauteurValue
                    )
                }
            }
            Text(
                text = "Volume : ${String.format(Locale.FRANCE,"%.2f", volumeUiState.volume)} m3",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )

            FilterChipGroup(
                modifier = Modifier.padding(16.dp),
                items = materialItems.keys.toList(),
                onSelectedChanged = { selectedIndex ->
                    volumeViewModel.updateDensity(materialItems[materialItems.keys.toList()[selectedIndex]])
                }
            )
            Text(
                text = "Densité : ${volumeUiState.density}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )

            Text(
                text = "Poids : ${volumeUiState.weight}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }

        SingleChoiceSegmentedButton(modifier = Modifier.padding(16.dp), cfcrItems, onClickButton = {
            //TODO
        })

        FilterChipGroup(
            modifier = Modifier.padding(16.dp),
            items = supportItems.keys.toList(),
            onSelectedChanged = { selectedIndex ->
                volumeViewModel.updateRF(supportItems[supportItems.keys.toList()[selectedIndex]])
            }
        )

        Text(
            text = "Résistance Fardeau : ${volumeUiState.rf}",
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic
        )
        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Expanded) {
            Button(
                onClick = {
                    navigator.navigateBack()
                },
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.End),
            ) {
                Text("Valider")
            }
        }
    }
}
