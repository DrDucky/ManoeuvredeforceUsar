package com.pomplarg.manoeuvredeforceusar.ui.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InclineApp(onClickButton: (Float) -> Unit) {
    var angle by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Angle: ${angle.toInt()}°")
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    if (angle < 45) angle += 5
                    onClickButton(angle)
                }) {
                    Text(text = "+", fontSize = 20.sp)
                }
                Button(onClick = {
                    if (angle > -45) angle -= 5
                    onClickButton(angle)
                }) {
                    Text(text = "-", fontSize = 20.sp)

                }
            }
            Column (Modifier.weight(1f)) {
                InclineCanvas(angle)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun InclineCanvas(angle: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
    ) {
        val length = size.width * 0.5f
        val endX = (size.width / 2 + length * cos(Math.toRadians(angle.toDouble()))).toFloat()
        val endY = (size.height / 2 - length * sin(Math.toRadians(angle.toDouble()))).toFloat()

        // Draw the incline line
        drawLine(
            color = Color.Black,
            start = center,
            end = androidx.compose.ui.geometry.Offset(endX, endY),
            strokeWidth = 5f
        )

        // Draw the parallelepiped
        val rectWidth = 100f
        val rectHeight = 50f
        val depth = 30f
        val rectCenterX =
            (size.width / 2 + (length / 2) * cos(Math.toRadians(angle.toDouble()))).toFloat()
        val rectCenterY =
            (size.height / 2 - (length / 2) * sin(Math.toRadians(angle.toDouble()))).toFloat()

        rotate(
            degrees = -angle,
            pivot = androidx.compose.ui.geometry.Offset(rectCenterX, rectCenterY)
        ) {
            // Front face
            val frontFaceTopLeft = androidx.compose.ui.geometry.Offset(
                rectCenterX - rectWidth / 2,
                rectCenterY - rectHeight - 10
            )
            val frontFaceSize = androidx.compose.ui.geometry.Size(rectWidth, rectHeight)

            drawRect(
                color = Color.Red,
                topLeft = androidx.compose.ui.geometry.Offset(
                    rectCenterX - rectWidth / 2,
                    rectCenterY - rectHeight - 10
                ),
                size = androidx.compose.ui.geometry.Size(rectWidth, rectHeight)
            )
            drawArrow(this, frontFaceTopLeft, frontFaceSize) // Dessiner la flèche à l'intérieur de la face avant
            // Top face
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX - rectWidth / 2,
                    rectCenterY - rectHeight - 10
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX - rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2,
                    rectCenterY - rectHeight - 10
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX - rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                strokeWidth = 5f
            )
            // Side face
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2,
                    rectCenterY - rectHeight - 10
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2,
                    rectCenterY - 10
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - 10 - depth
                ),
                strokeWidth = 5f
            )
            drawLine(
                color = Color.Red,
                start = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - rectHeight - 10 - depth
                ),
                end = androidx.compose.ui.geometry.Offset(
                    rectCenterX + rectWidth / 2 + depth,
                    rectCenterY - 10 - depth
                ),
                strokeWidth = 5f
            )
        }
    }
}
fun drawArrow(drawScope: DrawScope, topLeft: androidx.compose.ui.geometry.Offset, size: androidx.compose.ui.geometry.Size) {
    val path = Path().apply {
        moveTo(topLeft.x + size.width / 2, topLeft.y + size.height / 4)
        lineTo(topLeft.x + size.width / 2 - 2.82f, topLeft.y + size.height / 4 + 2.82f)
        lineTo(topLeft.x + size.width / 2 + 8.34f, topLeft.y + size.height / 2)
        lineTo(topLeft.x + size.width / 4, topLeft.y + size.height / 2)
        lineTo(topLeft.x + size.width / 4, topLeft.y + size.height / 2 + 4f)
        lineTo(topLeft.x + size.width / 2 + 8.34f, topLeft.y + size.height / 2 + 4f)
        lineTo(topLeft.x + size.width / 2 - 2.82f, topLeft.y + size.height / 2 + 15.18f)
        lineTo(topLeft.x + size.width / 2, topLeft.y + size.height / 2 + 18f)
        lineTo(topLeft.x + size.width / 2 + 16f, topLeft.y + size.height / 2)
        close()
    }
    drawScope.drawPath(path, Color.White)
}