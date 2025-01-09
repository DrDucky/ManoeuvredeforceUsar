package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DisplaySchema(volumeViewModel:  VolumeViewModel) {

    val volumeUiState by volumeViewModel.uiState.collectAsState()

    var densiteValue by remember { mutableIntStateOf(0) }
    var densiteExpanded by remember { mutableStateOf(false) }
    val painter: Painter = painterResource(id = R.drawable.machine)
    val painterObject: Painter = painterResource(id = R.drawable.objet)
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val textMeasurer = rememberTextMeasurer()

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                with(painter) {
                    draw(size = Size(200.dp.toPx(), 200.dp.toPx()))
                }
                val canvasWidth = size.width
                val canvasHeight = size.height
                val measuredText =
                    textMeasurer.measure(
                        "Density = ${volumeViewModel.uiState.value.density}",
                        style = TextStyle(fontSize = 12.sp)
                    )

                drawText(measuredText , topLeft =  Offset(x = 220.dp.toPx(), y = 90.dp.toPx()))
                drawLine(
                    start = Offset(x = 160.dp.toPx(), y = 95.dp.toPx()),
                    end = Offset(x = 309.dp.toPx(), y = 127.dp.toPx()),
                    color = Color.Green,
                    strokeWidth = 5.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                drawLine(
                    start = Offset(x = 131.dp.toPx(), y = 136.dp.toPx()),
                    end = Offset(x = 331.dp.toPx(), y = 161.dp.toPx()),
                    color = Color.Black,
                    strokeWidth = 5.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
                )
                drawLine(
                    start = Offset(x = 121.dp.toPx(), y = 165.dp.toPx()),
                    end = Offset(x = 359.dp.toPx(), y = 184.dp.toPx()),
                    color = Color.Black,
                    strokeWidth = 5.dp.toPx() // instead of 5.dp.toPx() , you can also pass 5f
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