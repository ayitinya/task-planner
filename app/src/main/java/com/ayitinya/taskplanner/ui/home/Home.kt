package com.ayitinya.taskplanner.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ayitinya.taskplanner.R
import com.ayitinya.taskplanner.navigation.Screen
import com.ayitinya.taskplanner.ui.composables.BottomModalWithTextField
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var openBottomSheet by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    if (openBottomSheet) {
        var text by remember {
            mutableStateOf("")
        }
        BottomModalWithTextField(
            title = stringResource(id = R.string.create_a_list),
            value = text,
            placeholderText = stringResource(id = R.string.create_a_list),
            onDismissRequest = { openBottomSheet = false },
            onValueChange = { text = it },
            onSave = { scope.launch { viewModel.createTodoList(text) } }
        )
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }, scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { openBottomSheet = !openBottomSheet }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add New List")
            }
        },
    ) { paddingValues ->
        if (uiState.todoLists.isNotEmpty()) {
            LazyColumn(contentPadding = paddingValues) {
                items(uiState.todoLists) {
                    Text(text = it.title, modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Screen.ListDetailScreen.navigateToListDetailScreen(
                                id = it.id, title = it.title , navController
                            )
                        }
                        .padding(horizontal = 16.dp, vertical = 16.dp))
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(id = R.string.create_a_list))
            }
        }
    }
}
