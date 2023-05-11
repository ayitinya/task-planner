package com.ayitinya.taskplanner.ui.task_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ayitinya.taskplanner.R
import com.ayitinya.taskplanner.ui.composables.BottomModalWithTextField
import com.ayitinya.taskplanner.ui.composables.CircularCheckBox
import kotlinx.coroutines.launch
import java.time.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navController: NavController, modifier: Modifier = Modifier, viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(title = { uiState.todoList?.let { Text(text = it.title) } }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })
    }, bottomBar = {
        Divider()
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .navigationBarsPadding()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Created on ${uiState.todo?.date?.dayOfWeek}", color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = {
                scope.launch {
                    uiState.todo?.let {
                        viewModel.deleteTodo(it)
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "delete",
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }) { paddingValues ->
        val scrollState = rememberScrollState()
        val subTask = remember { mutableStateListOf<String>() }

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 8.dp)) {

                uiState.todo?.let {
                    CircularCheckBox(checked = it.isCompleted, onCheckedChange = { state ->
                        scope.launch {
                            viewModel.updateTodo(
                                uiState.todo!!.copy(isCompleted = state)
                            )
                        }
                    }, modifier = Modifier)
                }
                Spacer(modifier = Modifier.width(8.dp))

                var openTitleModal by rememberSaveable {
                    mutableStateOf(false)
                }
                uiState.todo?.let {
                    Text(text = it.title,
                        style = MaterialTheme.typography.titleLarge,
                        textDecoration = if (uiState.todo?.isCompleted == true) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openTitleModal = true
                            })
                }

                if (openTitleModal) {
                    var title by rememberSaveable { mutableStateOf(uiState.todo?.title ?: "") }
                    BottomModalWithTextField(
                        title = stringResource(id = R.string.rename_task),
                        value = title,
                        placeholderText = stringResource(id = R.string.rename_task),
                        onDismissRequest = {
                            openTitleModal = !openTitleModal
                            title = uiState.todo!!.title
                        },
                        onValueChange = { title = it },
                        onSave = { scope.launch { uiState.todo?.let { viewModel.updateTodo(it.copy(title = title)) } } },
                    )
                }
            }

            subTask.forEach { element ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .padding(start = 16.dp)
                ) {
                    var checked by rememberSaveable {
                        mutableStateOf(false)
                    }
                    CircularCheckBox(
                        checked = checked,
                        onCheckedChange = { state -> checked = state },
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    var title by rememberSaveable {
                        mutableStateOf(element)
                    }

                    var openTitleModal by rememberSaveable {
                        mutableStateOf(false)
                    }

                    Text(text = element,
                        textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                openTitleModal = true
                            })


                    if (openTitleModal) {
                        BottomModalWithTextField(
                            title = stringResource(id = R.string.add_subtask),
                            value = title,
                            placeholderText = stringResource(id = R.string.add_subtask),
                            onDismissRequest = {
                                TODO("NOT IMPLEMENTED")
                            },
                            onValueChange = { title = it },
                            onSave = { TODO("NOT IMPLEMENTED") },
                        )
                    }
                }
            }

            var subTaskModal by rememberSaveable {
                mutableStateOf(false)
            }
            var newSubTask by rememberSaveable {
                mutableStateOf("")
            }
            TextButton(onClick = { subTaskModal = !subTaskModal }, enabled = false) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(id = R.string.add_subtask))
            }

            if (subTaskModal) {
                BottomModalWithTextField(
                    title = stringResource(id = R.string.add_subtask),
                    value = newSubTask,
                    placeholderText = stringResource(id = R.string.add_subtask),
                    onDismissRequest = {
                        subTaskModal = !subTaskModal
                        newSubTask = ""
                    },
                    onValueChange = { TODO("NOT IMPLEMENTED") },
                    onSave = { TODO("NOT IMPLEMENTED") },
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Card {
                val dueDate =
                    rememberDatePickerState(initialSelectedDateMillis = ZonedDateTime.now().toEpochSecond() * 1000)
                var openDatePickerDialog by rememberSaveable {
                    mutableStateOf(false)
                }
                if (openDatePickerDialog) {
                    DatePickerDialog(onDismissRequest = { openDatePickerDialog = false }, dismissButton = {
                        TextButton(onClick = {
                            openDatePickerDialog = false
                        }) {
                            Text("Cancel")
                        }
                    }, confirmButton = {
                        TextButton(onClick = {
                            scope.launch {
                                uiState.todo?.let {
                                    viewModel.updateTodo(
                                        it.copy(
                                            dueDate = ZonedDateTime.ofInstant(
                                                dueDate.selectedDateMillis?.let { it1 -> Instant.ofEpochMilli(it1) },
                                                ZoneId.systemDefault()
                                            )
                                        )
                                    )
                                    openDatePickerDialog = false
                                }
                            }
                        }) {
                            Text("Confirm")
                        }
                    }) {
                        DatePicker(state = dueDate)
                    }
                }

                val reminderTimeState = rememberTimePickerState()
                var openTimePickerDialog by remember { mutableStateOf(false) }

                if (openTimePickerDialog) {

                    TimePickerDialog(onDismissRequest = { openTimePickerDialog = false }, onConfirm = {
                        scope.launch {
                            uiState.todo?.let {
                                viewModel.updateTodo(
                                    it.copy(
                                        reminder = ZonedDateTime.of(
                                            LocalDate.now(),
                                            LocalTime.of(reminderTimeState.hour, reminderTimeState.minute),
                                            ZoneId.systemDefault()
                                        )
                                    )
                                )
                                openTimePickerDialog = false
                            }
                        }

                    }) {
                        TimePicker(state = reminderTimeState)
                    }

                }

                Row(modifier = Modifier
                    .clickable { openTimePickerDialog = true }
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Icon(imageVector = Icons.Default.Alarm, contentDescription = "Remind me")
                    Spacer(modifier = modifier.width(16.dp))
                    when (uiState.todo?.reminder) {
                        null -> "Remind me"
                        else -> uiState.todo!!.reminder?.let {
                            LocalTime.of(
                                uiState.todo!!.reminder!!.hour,
                                uiState.todo!!.reminder!!.minute
                            ).toString()
                        }
                    }?.let {
                        Text(
                            text = it
                        )
                    }
                }
                Divider()
                Row(modifier = Modifier
                    .clickable { openDatePickerDialog = true }
                    .fillMaxWidth()
                    .padding(16.dp)) {
                    Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = "Add due date")
                    Spacer(modifier = modifier.width(16.dp))
                    Text(
                        text = when (uiState.todo?.dueDate) {
                            null -> "Add due date"
                            else -> "${uiState.todo!!.dueDate?.dayOfMonth} ${uiState.todo!!.dueDate?.month} ${uiState.todo!!.dueDate?.year}"
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            var openNoteModal by rememberSaveable {
                mutableStateOf(false)
            }

            Card(
                onClick = { openNoteModal = true }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                uiState.todo?.note?.let { Text(text = it.ifEmpty { "Add note" }, modifier = Modifier.padding(16.dp)) }
            }
            val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            if (openNoteModal) {
                var note by rememberSaveable {
                    mutableStateOf(uiState.todo?.note ?: "")
                }
                ModalBottomSheet(onDismissRequest = {
                    openNoteModal = false
                    note = ""
                }, dragHandle = {}, sheetState = bottomSheetState) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Add note", style = MaterialTheme.typography.headlineSmall)
                    }
                    TextField(
                        value = note,
                        placeholder = { Text("Add note") },
                        onValueChange = { value -> note = value },
                        minLines = 2,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(), horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = {
                            openNoteModal = false
                            note = uiState.todo?.note ?: ""
                            scope.launch { bottomSheetState.hide() }
                        }, modifier = Modifier.padding(horizontal = 8.dp)) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            openNoteModal = false
                            scope.launch {
                                uiState.todo?.let { viewModel.updateTodo(it.copy(note = note)) }
                                bottomSheetState.hide()
                            }
                        }) {
                            Text("Save")
                        }
                    }
                }
            }

        }

    }
    LaunchedEffect(uiState.isTodoDeleted) {
        if (uiState.isTodoDeleted) navController.popBackStack()
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surface
                ),
        ) {
            toggle()
            Column(
                modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(
                        onClick = onDismissRequest
                    ) { Text("Cancel") }
                    TextButton(
                        onClick = onConfirm
                    ) { Text("OK") }
                }
            }
        }
    }
}

