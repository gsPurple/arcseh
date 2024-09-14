/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */

package com.purplexed.arrowcounter.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import com.purplexed.arrowcounter.R
import com.purplexed.arrowcounter.presentation.theme.ArcseshTheme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.hypot
import kotlin.math.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp("Gonçalo")
        }
    }
}

@Composable
fun WearApp(greetingName: String) {
    ArcseshTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            ArcheryTarget()
        }
    }
}

@Composable
fun ArcheryTarget() {
    // The size of the target
    val targetSize = 300.dp

    // State to track the clicked ring
    var clickedRing by remember { mutableStateOf(-1) }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .size(targetSize)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Calculate the distance from the center
                        val center = Offset((size.width / 2).toFloat(), (size.height / 2).toFloat())
                        val clickedDistance = hypot(offset.x - center.x, offset.y - center.y)

                        // Calculate the radius of the entire target and the width of each ring
                        val targetRadius = min(size.width, size.height) / 2
                        val ringWidth = targetRadius / 10

                        // Determine which ring was clicked based on the distance from the center
                        clickedRing = ((targetRadius - clickedDistance) / ringWidth)
                            .toInt()
                            .coerceIn(0, 9)

                        handleRingClick(clickedRing)
                    }
                },
            onDraw = {
                val targetRadius = min(size.width, size.height) / 2
                val numRings = 10
                val ringWidth = targetRadius / numRings

                // Draw concentric rings with different colors
                for (i in 0 until numRings) {
                    drawCircle(
                        color = getRingColor(i),
                        radius = targetRadius - (i * ringWidth),
                        style = Stroke(width = ringWidth * 0.98f)
                    )
                }

                // Draw a solid center circle to cover any remaining area
                val centerCircleRadius = maxOf(0f, targetRadius - (numRings * ringWidth) + ringWidth / 2)
                drawCircle(
                    color = getRingColor(numRings - 1), // Use the color of the innermost ring
                    radius = centerCircleRadius,
                )

                // Draw a cross in the middle
                val crossSize = ringWidth / 4 // Size of the cross
                val center = Offset(size.width / 2, size.height / 2)
                drawLine(
                    color = Color.Black,
                    start = Offset(center.x - crossSize, center.y),
                    end = Offset(center.x + crossSize, center.y),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(center.x, center.y - crossSize),
                    end = Offset(center.x, center.y + crossSize),
                    strokeWidth = 2f
                )
            }
        )
    }
}

// Get a color for each ring (just for visual purposes)
fun getRingColor(index: Int): Color {
    val blueRing = Color(android.graphics.Color.parseColor("#3480eb"))
    val redRing = Color(android.graphics.Color.parseColor("#f24430"))
    val goldRing = Color(android.graphics.Color.parseColor("#f2c830"))

    return when (index) {
        0 -> Color.White
        1 -> Color.White
        2 -> Color.Black
        3 -> Color.Black
        4 -> blueRing
        5 -> blueRing
        6 -> redRing
        7 -> redRing
        8 -> goldRing
        9 -> goldRing
        else -> Color.Gray
    }
}

fun handleRingClick(index: Int) {
    val score = index + 1
    // Print which ring was clicked
    println("Nice shot! You scored: $score")
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp("Preview Android")
}