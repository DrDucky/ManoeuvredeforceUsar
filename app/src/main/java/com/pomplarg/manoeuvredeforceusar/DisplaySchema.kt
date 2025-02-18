package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pomplarg.manoeuvredeforceusar.ui.composables.SingleChoiceSegmentedButton
import kotlin.math.roundToInt

@Composable
fun DisplaySchema(volumeViewModel:  VolumeViewModel) {

    val volumeUiState by volumeViewModel.volumeState.collectAsState()

    val emdValue = remember { mutableStateOf("") }
    var emdDisplayed by remember { mutableStateOf(false) }
    val painter: Painter = painterResource(id = R.drawable.machine)
    val painterObject: Painter = painterResource(id = R.drawable.objet)
    val textMeasurer = rememberTextMeasurer()
    val treuilsItems = listOf("TU16", "TU32", "Treuil")
    val a1Color = if(volumeUiState.a1Activated) Color.Green else Color.Black
    val a2Color = if(volumeUiState.a2Activated) Color.Green else Color.Black
    val a3Color = if(volumeUiState.a3Activated) Color.Green else Color.Black

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            SingleChoiceSegmentedButton(Modifier.padding(16.dp),
                treuilsItems,
                onClickButton = {
                index ->
                    emdDisplayed = false
                    when (index) {
                        0 -> volumeViewModel.updateEmd(1600)
                        1 -> volumeViewModel.updateEmd(3000)
                        else -> {
                            emdDisplayed = true
                        }
                    }

            })
            if(emdDisplayed) {
                OutlinedTextField(
                    modifier = Modifier.padding(16.dp),
                    value = emdValue.value,
                    singleLine = true,
                    onValueChange = {
                        emdValue.value = it
                        volumeViewModel.updateEmd(it.toIntOrNull()?:0)
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
                fontSize = 16.sp)
            Text(
                text = "Sécurité du mouflage : ${volumeUiState.safety} %",
                modifier = Modifier.padding(start = 16.dp),
                fontSize = 16.sp,
                color = Color.Red)
            Canvas(
                modifier = Modifier
                    .fillMaxWidth().padding(vertical = 16.dp)
            ) {
                with(painter) {
                    draw(size = Size(200.dp.toPx(), 200.dp.toPx()))
                }
                val canvasWidth = size.width
                val canvasHeight = size.height
                val measuredText =
                    textMeasurer.measure(
                        "A1 = ${volumeViewModel.volumeState.value.a1}",
                        style = TextStyle(fontSize = 12.sp)
                    )

                drawText(measuredText , topLeft =  Offset(x = 220.dp.toPx(), y = 90.dp.toPx()))
                drawLine(
                    start = Offset(x = 160.dp.toPx(), y = 95.dp.toPx()),
                    end = Offset(x = 309.dp.toPx(), y = 127.dp.toPx()),
                    color = a1Color,
                    strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                drawLine(
                    start = Offset(x = 131.dp.toPx(), y = 136.dp.toPx()),
                    end = Offset(x = 331.dp.toPx(), y = 161.dp.toPx()),
                    color = a2Color,
                    strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                drawLine(
                    start = Offset(x = 121.dp.toPx(), y = 165.dp.toPx()),
                    end = Offset(x = 359.dp.toPx(), y = 184.dp.toPx()),
                    color = a3Color,
                    strokeWidth = 4.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                translate(
                    left = 300.dp.toPx(),
                    top = 100.dp.toPx()
                ) {
                    with(painterObject) {
                        draw(size = Size(100.dp.toPx(), 100.dp.toPx()))
                    }
                }
            }
        }
    }
}