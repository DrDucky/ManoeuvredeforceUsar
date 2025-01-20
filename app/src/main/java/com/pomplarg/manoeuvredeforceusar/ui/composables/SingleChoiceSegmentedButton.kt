package com.pomplarg.manoeuvredeforceusar.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButton(modifier: Modifier = Modifier, options: List<String>, onClickButton: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var firstSelection by remember { mutableStateOf(false) }

    SingleChoiceSegmentedButtonRow (modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    firstSelection = true
                    selectedIndex = index
                    onClickButton(index) },
                selected = firstSelection && (index == selectedIndex),
                label = { Text(label) }
            )
        }
    }
}