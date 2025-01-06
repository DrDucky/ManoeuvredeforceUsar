package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.PI
import kotlin.math.pow

@Composable
fun CalculateVolume() {
    val selectedOption = remember { mutableStateOf("Saisie manuelle") }
    val textFieldValue = remember { mutableStateOf("") }
    val textFieldEnabled = remember { mutableStateOf(true) }
    val rayonValue = remember { mutableStateOf("") }
    val hauteurValue = remember { mutableStateOf("") }
    val openAlertDialog = remember { mutableStateOf(false) }
    val volumeValue = remember { mutableDoubleStateOf(0.0) }
    val densiteItems = listOf("Béton", "Béton armé")
    var densiteValue by remember { mutableIntStateOf(0) }
    var densiteExpanded by remember { mutableStateOf(false) }
    var betonSelected by remember { mutableStateOf(false) }
    var betonArmeSelected by remember { mutableStateOf(false) }

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
            modifier = Modifier.padding(16.dp))
        RadioButtonWithLabel(
            label = "Saisie manuelle",
            selected = selectedOption.value == "Saisie manuelle",
            onClick = {
                textFieldEnabled.value = true
                selectedOption.value = "Saisie manuelle"
            }
        )
        RadioButtonWithLabel(
            label = "Saisie par calcul",
            selected = selectedOption.value == "Saisie par calcul",
            onClick = {
                textFieldEnabled.value = false
                selectedOption.value = "Saisie par calcul"
            }
        )
        if (selectedOption.value == "Saisie par calcul") {
            Row (modifier = Modifier.padding(16.dp)) {
                AssistChip(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { openAlertDialog.value = true },
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
                    onClick = { openAlertDialog.value = true },
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
                openAlertDialog.value -> {
                    DialogWithImage(
                        onDismissRequest = { openAlertDialog.value = false },
                        onConfirmation = {
                            openAlertDialog.value = false
                            volumeValue.doubleValue = PI * rayonValue.value.toDouble()
                                .pow(2) * hauteurValue.value.toDouble() * 1 / 3
                            textFieldValue.value = volumeValue.value.toString()
                        },
                        painter = painterResource(id = R.drawable.volume_cone),
                        imageDescription = "",
                        rayonValue,
                        hauteurValue
                    )
                }
            }
            Row(modifier = Modifier.padding(16.dp)) {
                FilterChip(
                    onClick = { betonSelected = !betonSelected },
                    label = {
                        Text("Béton")
                    },
                    selected = betonSelected,
                    leadingIcon = if (betonSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
                FilterChip(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    onClick = { betonArmeSelected = !betonArmeSelected },
                    label = {
                        Text("Béton armé")
                    },
                    selected = betonArmeSelected,
                    leadingIcon = if (betonArmeSelected) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "Done icon",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
        OutlinedTextField(
            modifier = Modifier.padding(16.dp),
            value = textFieldValue.value,
            enabled = textFieldEnabled.value,
            onValueChange = { textFieldValue.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            label = { Text("Poids") }
        )
        /*DropdownMenu(
            expanded = true,
            onDismissRequest = { densiteExpanded = false }
        ) {
            densiteItems.forEachIndexed { index, s ->
                DropdownMenuItem(
                    text = { Text(s) },
                    onClick = {
                    densiteValue = index
                    densiteExpanded = false
                })
            }
        }*/
    }
}

@Composable
fun RadioButtonWithLabel(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
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
