package com.pomplarg.manoeuvredeforceusar.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterChipGroup(
    modifier: Modifier,
    items: List<String>,
    defaultSelectedItemIndex: Int = 0,
    onSelectedChanged: (Int) -> Unit = {}
) {
    var selectedItemIndex by remember { mutableIntStateOf(defaultSelectedItemIndex) }
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