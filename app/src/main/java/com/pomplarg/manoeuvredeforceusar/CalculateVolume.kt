package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pomplarg.manoeuvredeforceusar.Constants.FROTTEMENT_KEY
import com.pomplarg.manoeuvredeforceusar.Constants.ROULEMENT_KEY
import com.pomplarg.manoeuvredeforceusar.ui.composables.DialogWithImage
import com.pomplarg.manoeuvredeforceusar.ui.composables.FilterChipGroup
import com.pomplarg.manoeuvredeforceusar.ui.composables.InclineApp
import com.pomplarg.manoeuvredeforceusar.ui.composables.RadioButtonWithLabel
import com.pomplarg.manoeuvredeforceusar.ui.composables.SingleChoiceSegmentedButton

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CalculateVolume(
    volumeViewModel: VolumeViewModel,
    navigator: ThreePaneScaffoldNavigator<Nothing>
) {

    val poidsEnabled = remember { mutableStateOf(true) }
    val poidsValue = remember { mutableStateOf("") }
    val rayonValue = remember { mutableStateOf("") }
    val density = remember { mutableDoubleStateOf(0.0) }
    val hauteurValue = remember { mutableStateOf("") }
    val openConeDialog = remember { mutableStateOf(false) }
    val openCylinderDialog = remember { mutableStateOf(false) }
    val materialItems = mapOf("Béton" to 2.0,
        "Béton armé" to 5.0)
    val categoryFrottementRoulement = listOf(FROTTEMENT_KEY,
        ROULEMENT_KEY)
    var categoryFrottementRoulementSelected = remember { mutableStateOf("Frottement") }
    val supportFrottementRoulementSelected = remember { mutableStateOf("") }
    val manualChoice = remember { mutableStateOf(true) }
    val volumeTypeSelected = remember { mutableStateOf("") }
    val angleValue = remember { mutableFloatStateOf(0f) }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Configuration de l'objet",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Saisie du poids",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        RadioButtonWithLabel(
            label = "Saisie manuelle",
            selected = manualChoice.value,
            onClick = {
                poidsEnabled.value = true
                manualChoice.value = true
            }
        )
        RadioButtonWithLabel(
            label = "Saisie par calcul",
            selected = !manualChoice.value,
            onClick = {
                poidsEnabled.value = false
                manualChoice.value = false
            }
        )

        if(manualChoice.value) {
            OutlinedTextField(
                modifier = Modifier.padding(16.dp),
                value = poidsValue.value,
                singleLine = true,
                enabled = poidsEnabled.value,
                onValueChange = {
                    poidsValue.value = it
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
                    trailingIcon = {
                        if (volumeTypeSelected.value == Volume.VolumeType.CONE.name) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "item selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    },
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
                    trailingIcon = {
                        if (volumeTypeSelected.value == Volume.VolumeType.CYLINDER.name) {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "item selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    },
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
                            volumeTypeSelected.value = Volume.VolumeType.CONE.name
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
                            volumeTypeSelected.value = Volume.VolumeType.CYLINDER.name
                        },
                        painter = painterResource(id = R.drawable.volume_cylinder),
                        imageDescription = "",
                        rayonValue,
                        hauteurValue
                    )
                }
            }

            FilterChipGroup(
                modifier = Modifier.padding(16.dp),
                items = materialItems.keys.toList(),
                onSelectedChanged = { selectedIndex ->
                    materialItems[materialItems.keys.toList()[selectedIndex]]?.let {
                        density.value = it
                    }
                }
            )
        }

        SingleChoiceSegmentedButton(modifier = Modifier.padding(16.dp), categoryFrottementRoulement, onClickButton = {
            categoryFrottementRoulementSelected.value =  categoryFrottementRoulement[it]
        })

        FilterChipGroup(
            modifier = Modifier.padding(16.dp),
            defaultSelectedItemIndex = 0,
            items = volumeViewModel.supportItems.keys.toList(),
            onSelectedChanged = { selectedIndex ->
                supportFrottementRoulementSelected.value = volumeViewModel.supportItems.keys.toList()[selectedIndex]
            }
        )

        InclineApp(onClickButton = {
                angle -> angleValue.floatValue = angle
        })

        if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Expanded) {
            Button(
                enabled = ((manualChoice.value && poidsValue.value.isNotBlank()) ||
                        (!manualChoice.value && volumeTypeSelected.value.isNotEmpty() && density.value != 0.0)) &&
                        supportFrottementRoulementSelected.value.isNotEmpty() &&
                        categoryFrottementRoulementSelected.value.isNotEmpty(),
                onClick = {
                    if(manualChoice.value) {
                        volumeViewModel.updateRFManuel(
                            poidsValue.value.toDouble(),
                            categoryFrottementRoulementSelected.value,
                            supportFrottementRoulementSelected.value,
                            angleValue.floatValue)
                    } else {
                        volumeViewModel.updateRFCalcul(
                            volumeTypeSelected.value,
                            rayonValue.value.toDouble(),
                            hauteurValue.value.toDouble(),
                            density.doubleValue,
                            categoryFrottementRoulementSelected.value,
                            supportFrottementRoulementSelected.value,
                            angleValue.floatValue)
                    }
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

object Constants {
    const val FROTTEMENT_KEY = "Frottement"
    const val ROULEMENT_KEY = "Roulement"
}
