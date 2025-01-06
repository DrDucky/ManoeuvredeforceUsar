package com.pomplarg.manoeuvredeforceusar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun DisplaySchema(onNavigateToSupportingPane: () -> Unit) {
    var densiteValue by remember { mutableIntStateOf(0) }
    var densiteExpanded by remember { mutableStateOf(false) }
    val painter: Painter = painterResource(id = R.drawable.machine)
    val painterObject: Painter = painterResource(id = R.drawable.objet)
    val navigator = rememberSupportingPaneScaffoldNavigator()


    Column (modifier = Modifier
        .fillMaxSize()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            with(painter) {
                draw(size = Size(200.dp.toPx(), 200.dp.toPx()))
            }
            val canvasWidth = size.width
            val canvasHeight = size.height
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
        Button(
            onClick = onNavigateToSupportingPane,
            modifier = Modifier.padding(8.dp),
        ) {
            Text("Configuration de l'objet")
        }
    }
}