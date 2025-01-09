package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Locale

@Composable
fun CalculateVolume(volumeViewModel:  VolumeViewModel) {

    val volumeUiState by volumeViewModel.uiState.collectAsState()

    val poidsEnabled = remember { mutableStateOf(true) }
    val rayonValue = remember { mutableStateOf("") }
    val hauteurValue = remember { mutableStateOf("") }
    val openConeDialog = remember { mutableStateOf(false) }
    val openCylinderDialog = remember { mutableStateOf(false) }
    var manualChoice by remember { mutableStateOf(true) }
    val materialItems = mapOf("Béton" to 2,
        "Béton armé" to 5)
    val supportItems = mapOf("Béton sec" to 0.8,
        "Béton mouillé" to 0.6)


    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(16.dp)
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
        if (!manualChoice) {
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
                    volumeViewModel.updateWeight(volumeUiState.volume, materialItems[materialItems.keys.toList()[selectedIndex]])
                }
            )
            Text(
                text = "Densité : ${volumeUiState.density}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
        }
        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = volumeUiState.weight.toString(),
            singleLine = true,
            enabled = poidsEnabled.value,
            onValueChange = { volumeViewModel.updateWeightWithEntry(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Poids") }
        )

        SingleChoiceSegmentedButton(modifier = Modifier.padding(16.dp))

        FilterChipGroup(
            modifier = Modifier.padding(16.dp),
            items = supportItems.keys.toList(),
            onSelectedChanged = { selectedIndex ->
                volumeViewModel.updateRF(volumeUiState.volume, supportItems[supportItems.keys.toList()[selectedIndex]])
            }
        )

        Text(
            text = "Résistance Fardeau : ${volumeUiState.rf}",
            modifier = Modifier.padding(start = 16.dp),
            fontSize = 10.sp,
            fontStyle = FontStyle.Italic
        )
        Button(
            onClick = {
                //Valider
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.End),
        ) {
            Text("Valider")
        }
    }
}

@Composable
fun RadioButtonWithLabel(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .selectable(
                selected = selected,
                onClick = { onClick() },
                role = Role.RadioButton
            ),
        verticalAlignment = Alignment.CenterVertically

    ) {
        RadioButton(
            selected = selected,
            onClick = null, // null recommended for accessibility with screenreaders
            colors = RadioButtonDefaults.colors(
                selectedColor = Color.Blue,
                unselectedColor = Color.Gray
            )
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
    rayonValue: MutableState<String>,
    hauteurValue: MutableState<String>
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    painter = painter,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(160.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        value = rayonValue.value,
                        singleLine = true,
                        onValueChange = { rayonValue.value = it },
                        label = { Text("Rayon (r)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Text(
                        modifier = Modifier.weight(0.3f),
                        text = "mètres"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1f),
                        value = hauteurValue.value,
                        singleLine = true,
                        onValueChange = { hauteurValue.value = it },
                        label = { Text("Hauteur (H)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Text(
                        modifier = Modifier.weight(0.3f),
                        text = "mètres"
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Annuler")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Valider")
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipGroup(
    modifier: Modifier,
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {}
) {
    var selectedItemIndex by remember { mutableStateOf(defaultSelectedItemIndex) }
    var firstSelection by remember { mutableStateOf(false) }

    LazyRow(userScrollEnabled = true, modifier = modifier) {
        items(items.size) { index: Int ->
            FilterChip(
                modifier = Modifier.padding(end = 6.dp),
                selected = firstSelection && items[selectedItemIndex] == items[index],
                onClick = {
                    firstSelection = true
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                label = { Text(items[index]) },
                leadingIcon = if (firstSelection && items[selectedItemIndex] == items[index]) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "item selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf("Frottement", "Roulement")

    SingleChoiceSegmentedButtonRow (modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}
