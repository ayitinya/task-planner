package com.ayitinya.taskplanner.ui.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ayitinya.taskplanner.R
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomModalWithTextField(
    title: String,
    value: String,
    placeholderText: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(onDismissRequest = onDismissRequest, sheetState = bottomSheetState, dragHandle = {}) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title, style = MaterialTheme.typography.headlineSmall
            )
        }

        TextField(value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholderText) })

        Row(
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.ime)
                .padding(8.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.End
        ) {
            TextButton(onClick = {
                scope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        onDismissRequest()
                    }
                }
            }, modifier = Modifier.padding(horizontal = 8.dp)) {
                Text(stringResource(id = R.string.cancel))
            }
            Button(onClick = {
                onSave()
                scope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        onDismissRequest()
                    }
                }
            }, enabled = value.isNotEmpty()) {
                Text(stringResource(id = R.string.save))
            }
        }
    }
}