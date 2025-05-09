package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pomplarg.manoeuvredeforceusar.ui.composables.SingleChoiceSegmentedButton
import com.pomplarg.manoeuvredeforceusar.ui.theme.AColor
import com.pomplarg.manoeuvredeforceusar.ui.theme.BColor
import com.pomplarg.manoeuvredeforceusar.ui.theme.Secondary40
import kotlin.math.roundToInt
import androidx.compose.material3.Icon as Icon

@Composable
fun DisplaySchema(volumeViewModel: VolumeViewModel) {

    val volumeUiState by volumeViewModel.volumeState.collectAsState()

    val emdValue = remember { mutableStateOf("") }
    var emdDisplayed by remember { mutableStateOf(false) }
    val painter: Painter = painterResource(id = R.drawable.machine)
    val painterObject: Painter = painterResource(id = R.drawable.objet)
    val textMeasurer = rememberTextMeasurer()
    val treuilsItems = listOf("TU16", "TU32", "Treuil")
    val a1Color = if (volumeUiState.a1Activated) Color.Green else Color.Black
    val a2Color = if (volumeUiState.a2Activated) Color.Green else Color.Black
    val a3Color = if (volumeUiState.a3Activated) Color.Green else Color.Black
    val a4Color = if (volumeUiState.a4Activated) Color.Green else Color.Black
    val a5Color = if (volumeUiState.a5Activated) Color.Green else Color.Black

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            SingleChoiceSegmentedButton(Modifier.padding(16.dp),
                treuilsItems,
                onClickButton = { index ->
                    emdDisplayed = false
                    when (index) {
                        0 -> volumeViewModel.updateEmd(1600)
                        1 -> volumeViewModel.updateEmd(3000)
                        else -> {
                            emdDisplayed = true
                        }
                    }

                })
            if (emdDisplayed) {
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    value = emdValue.value,
                    singleLine = true,
                    onValueChange = {
                        emdValue.value = it
                        volumeViewModel.updateEmd(it.toIntOrNull() ?: 0)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    label = { Text("Emd") }
                )
            }
            Text(
                text = "Poids : ${volumeUiState.weight.roundToInt()}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "Résistance Fardeau : ${volumeUiState.rf.roundToInt()}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "EMD : ${volumeUiState.emd}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 16.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "CR Brins : ${volumeUiState.crBrins}",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 10.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                text = "Nombre de brins : ${volumeUiState.nbBrins}",
                modifier = Modifier.padding(start = 16.dp),
                color = Secondary40,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Row {
                Text(
                    text = "Sécurité du mouflage : ${volumeUiState.safety.roundToInt()} %",
                    modifier = Modifier.padding(start = 16.dp),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (volumeUiState.safety < 20) Color.Red else Secondary40
                )
                Text(
                    text = "Si <20%, ajouter 1 brin",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (volumeUiState.safety < 20) Color.Red else Secondary40
                )
            }
            if (volumeUiState.nbBrins > 5 || volumeUiState.safety < 20) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp)
                    ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "warning",
                        modifier = Modifier.size(FilterChipDefaults.IconSize),
                        tint = Color.Red
                    )
                    Text(
                        text = "Manoeuvre impossible",
                        modifier = Modifier.padding(start = 16.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
            }
            Box(modifier = Modifier.height(250.dp)) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    with(painter) {
                        draw(size = Size(200.dp.toPx(), 200.dp.toPx()))
                    }
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val a1Text = designCanvasText(textMeasurer, "A1", AColor)
                    val a2Text = designCanvasText(textMeasurer, "A2", AColor)
                    val a3Text = designCanvasText(textMeasurer, "A3", AColor)
                    val a4Text = designCanvasText(textMeasurer, "A4", AColor)
                    val a5Text = designCanvasText(textMeasurer, "A5", AColor)

                    val b1Text = designCanvasText(textMeasurer, "B1", BColor)
                    val b2Text = designCanvasText(textMeasurer, "B2", BColor)
                    val b3Text = designCanvasText(textMeasurer, "B3", BColor)
                    val b4Text = designCanvasText(textMeasurer, "B4", BColor)
                    val b5Text = designCanvasText(textMeasurer, "B5", BColor)
                    val b6Text = designCanvasText(textMeasurer, "B6", BColor)

                    drawLine(
                        start = Offset(x = 160.dp.toPx(), y = 95.dp.toPx()),
                        end = Offset(x = 255.dp.toPx(), y = 95.dp.toPx()),
                        color = a1Color,
                        strokeWidth = 4.dp.toPx()
                    )
                    drawLine(
                        start = Offset(x = 131.dp.toPx(), y = 125.dp.toPx()),
                        end = Offset(x = 257.dp.toPx(), y = 120.dp.toPx()),
                        color = a2Color,
                        strokeWidth = 4.dp.toPx()
                    )
                    drawLine(
                        start = Offset(x = 131.dp.toPx(), y = 145.dp.toPx()),
                        end = Offset(x = 275.dp.toPx(), y = 131.dp.toPx()),
                        color = a3Color,
                        strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                    )
                    drawLine(
                        start = Offset(x = 120.dp.toPx(), y = 158.dp.toPx()),
                        end = Offset(x = 282.dp.toPx(), y = 154.dp.toPx()),
                        color = a4Color,
                        strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                    )
                    drawLine(
                        start = Offset(x = 120.dp.toPx(), y = 178.dp.toPx()),
                        end = Offset(x = 310.dp.toPx(), y = 170.dp.toPx()),
                        color = a5Color,
                        strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                    )
                    translate(
                        left = canvasWidth - 100.dp.toPx(),
                        top = 80.dp.toPx()
                    ) {
                        with(painterObject) {
                            draw(size = Size(100.dp.toPx(), 100.dp.toPx()))
                        }
                    }

                    drawText(a1Text, topLeft = Offset(x = 210.dp.toPx(), y = 80.dp.toPx()))
                    drawText(a2Text, topLeft = Offset(x = 190.dp.toPx(), y = 105.dp.toPx()))
                    drawText(a3Text, topLeft = Offset(x = 193.dp.toPx(), y = 124.dp.toPx()))
                    drawText(a4Text, topLeft = Offset(x = 220.dp.toPx(), y = 140.dp.toPx()))
                    drawText(a5Text, topLeft = Offset(x = 215.dp.toPx(), y = 160.dp.toPx()))

                    drawText(b1Text, topLeft = Offset(x = 130.dp.toPx(), y = 85.dp.toPx()))
                    drawText(b2Text, topLeft = Offset(x = 280.dp.toPx(), y = 95.dp.toPx()))
                    drawText(b3Text, topLeft = Offset(x = 100.dp.toPx(), y = 124.dp.toPx()))
                    drawText(b4Text, topLeft = Offset(x = 295.dp.toPx(), y = 135.dp.toPx()))
                    drawText(b5Text, topLeft = Offset(x = 95.dp.toPx(), y = 170.dp.toPx()))
                    drawText(b6Text, topLeft = Offset(x = 315.dp.toPx(), y = 160.dp.toPx()))
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column {
                    Legend(
                        "A1 = ${volumeUiState.a1.roundToInt()}",
                        AColor,
                        volumeUiState.a1Activated
                    )
                    Legend(
                        "A2 = ${volumeUiState.a2.roundToInt()}",
                        AColor, volumeUiState.a2Activated
                    )
                    Legend(
                        "A3 = ${volumeUiState.a3.roundToInt()}",
                        AColor,
                        volumeUiState.a3Activated
                    )
                    Legend(
                        "A4 = ${volumeUiState.a4.roundToInt()}",
                        AColor,
                        volumeUiState.a4Activated
                    )
                    Legend(
                        "A5 = ${volumeUiState.a5.roundToInt()}",
                        AColor,
                        volumeUiState.a5Activated
                    )
                }
                Column {
                    Legend(
                        "B1 = ${volumeUiState.a1.roundToInt()}",
                        BColor,
                        volumeUiState.a1Activated
                    )
                    Legend(
                        "B2 = ${volumeUiState.a1.roundToInt() + volumeUiState.a2.roundToInt()}",
                        BColor,
                        volumeUiState.a1Activated
                    )
                    Legend(
                        "B3 = ${volumeUiState.a2.roundToInt() + volumeUiState.a3.roundToInt()}",
                        BColor,
                        volumeUiState.a2Activated
                    )
                    Legend(
                        "B4 = ${volumeUiState.a3.roundToInt() + volumeUiState.a4.roundToInt()}",
                        BColor,
                        volumeUiState.a3Activated
                    )
                    Legend(
                        "B5 = ${volumeUiState.a4.roundToInt() + volumeUiState.a5.roundToInt()}",
                        BColor,
                        volumeUiState.a4Activated
                    )
                    Legend(
                        "B6 = ${volumeUiState.a5.roundToInt()}",
                        BColor,
                        volumeUiState.a5Activated
                    )
                }
            }
        }
    }
}

fun designCanvasText(textMeasurer: TextMeasurer, text: String, color: Color): TextLayoutResult {
    return textMeasurer.measure(
        text,
        style = TextStyle(
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    )
}

@Composable
fun Legend(text: String, color: Color, isActivated: Boolean) {
    if (isActivated) {
        Text(
            text = text,
            modifier = Modifier.padding(start = 16.dp),
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
