package com.pomplarg.manoeuvredeforceusar.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    painter: Painter,
    imageDescription: String,
    rayonValue: MutableState<String>?,
    hauteurValue: MutableState<String>?,
    longueurValue: MutableState<String>?,
    largeurValue: MutableState<String>?
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
                    rayonValue?.value?.let {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            value = it,
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
                }
                hauteurValue?.let {
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
                }
                longueurValue?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            value = longueurValue.value,
                            singleLine = true,
                            onValueChange = { longueurValue.value = it },
                            label = { Text("Longueur (L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Text(
                            modifier = Modifier.weight(0.3f),
                            text = "mètres"
                        )
                    }
                }
                largeurValue?.let {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(16.dp)
                                .weight(1f),
                            value = largeurValue.value,
                            singleLine = true,
                            onValueChange = { largeurValue.value = it },
                            label = { Text("Largeur (l)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                        Text(
                            modifier = Modifier.weight(0.3f),
                            text = "mètres"
                        )
                    }
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