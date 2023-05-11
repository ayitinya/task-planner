package com.ayitinya.taskplanner.ui.list_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ayitinya.taskplanner.R
import com.ayitinya.taskplanner.data.todos.Todo
import com.ayitinya.taskplanner.navigation.Screen
import com.ayitinya.taskplanner.ui.composables.CircularCheckBox
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ListDetailScreen(
    navController: NavController, modifier: Modifier = Modifier, viewModel: ListDetailViewModel = hiltViewModel()
) {


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = uiState.isTodoListDeleted, block = {
        if (uiState.isTodoListDeleted) navController.popBackStack()
    })

    var openBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (openBottomSheet) {
        var text by remember {
            mutableStateOf("")
        }
        ModalBottomSheet(onDismissRequest = { openBottomSheet = !openBottomSheet },
            sheetState = bottomSheetState,
            dragHandle = {}) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.new_todo), style = MaterialTheme.typography.headlineSmall
                )
            }

            TextField(value = text,
                onValueChange = { text = it },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(id = R.string.new_todo)) })

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .windowInsetsPadding(WindowInsets.ime)
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.EditNote,
                        contentDescription = "Details",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = "Time",
                        modifier = Modifier.size(32.dp)
                    )
                }
                IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(0.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.StarOutline,
                        contentDescription = "Favorite",
                        modifier = Modifier.size(32.dp)
                    )
                }
                ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                    val (row) = createRefs()
                    Row(modifier = Modifier
                        .constrainAs(row) { end.linkTo(parent.end, margin = 0.dp) }
                        .wrapContentWidth(Alignment.End)) {
                        TextButton(onClick = {
                            scope.launch {
                                bottomSheetState.hide()
                                text = ""
                            }.invokeOnCompletion { openBottomSheet = false }
                        }) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    viewModel.addTodo(title = text)
                                    bottomSheetState.hide()
                                    text = ""
                                }.invokeOnCompletion { openBottomSheet = false }
                            }, enabled = text.isNotEmpty()
                        ) {
                            Text(stringResource(id = R.string.save))
                        }
                    }
                }
            }
        }
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        MediumTopAppBar(title = { uiState.todoList?.let { Text(text = it.title) } }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            IconButton(onClick = { scope.launch { viewModel.deleteList() } }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete list")
            }
        }, scrollBehavior = scrollBehavior
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = { openBottomSheet = !openBottomSheet }) {
            Icon(imageVector = Icons.Default.AddTask, contentDescription = "Add Task")
        }
    }) {
        if (uiState.todos.isEmpty() && uiState.completedTodos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.add_a_todo))
            }
        } else {
            LazyColumn(contentPadding = it) {
                items(uiState.todos, key = { item: Todo -> item.id }) { todo ->
                    TodoItem(modifier = Modifier.animateItemPlacement(), todo = todo, onCheckedChange = {
                        scope.launch {
                            viewModel.toggleTodoCompletionState(
                                todo = todo, isCompleted = !todo.isCompleted
                            )
                        }
                    }, onClick = {
                        Screen.TaskDetailScreen.navigateToTaskDetailScreen(
                            todo.id, navController
                        )
                    })
                }

                if (uiState.completedTodos.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(id = R.string.completed),
                            Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                items(uiState.completedTodos, key = { item: Todo -> item.id }) { todo ->
                    TodoItem(modifier = Modifier.animateItemPlacement(), todo = todo, onCheckedChange = {
                        scope.launch {
                            viewModel.toggleTodoCompletionState(
                                todo = todo, isCompleted = !todo.isCompleted
                            )
                        }
                    }, onClick = {
                        Screen.TaskDetailScreen.navigateToTaskDetailScreen(
                            todo.id, navController
                        )
                    })
                }

            }
        }
    }
}

@Composable
private fun TodoItem(
    modifier: Modifier = Modifier, todo: Todo, onCheckedChange: (Boolean) -> Unit, onClick: () -> Unit
) {
    Row(modifier = modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        CircularCheckBox(checked = todo.isCompleted, onCheckedChange = onCheckedChange, modifier = Modifier.size(24.dp))
        Text(text = todo.title,
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .padding(16.dp),
            textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else TextDecoration.None)
    }
}
