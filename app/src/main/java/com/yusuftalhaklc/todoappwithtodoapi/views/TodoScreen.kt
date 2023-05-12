package com.yusuftalhaklc.todoappwithtodoapi.views

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.yusuftalhaklc.todoappwithtodoapi.R
import com.yusuftalhaklc.todoappwithtodoapi.models.ListResponse
import com.yusuftalhaklc.todoappwithtodoapi.models.ListResponseItem
import com.yusuftalhaklc.todoappwithtodoapi.models.TodoItem
import com.yusuftalhaklc.todoappwithtodoapi.ui.theme.cardColor
import com.yusuftalhaklc.todoappwithtodoapi.viewmodels.LoginViewModel

@Composable
fun TodoScreen(navController : NavController ,viewModel: LoginViewModel) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val temp = rememberSaveable{ mutableStateOf(false)}
    val listState = remember { mutableStateOf(emptyList<ListResponseItem>()) }

    var showDialog by remember { mutableStateOf(false) }
    var loginIsSuccessful by remember { mutableStateOf(false) }

    viewModel.todoLists.observeAsState()



    LaunchedEffect(viewModel, lifecycleOwner) {

        viewModel.loginIsSuccessful.observe(lifecycleOwner){
            it?.let {
                loginIsSuccessful = it
            }
        }

        viewModel.todoLists.observe(lifecycleOwner){
            it?.let {
                listState.value = mutableListOf()
                listState.value = it
                Log.d("Login 2","refreshhed")
                temp.value = true
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showDialog = true
                },
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }
            )
        }
    ) {

        if (showDialog){
            var newListName by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { },
                title = { Text("Create new list") },
                text = {
                    OutlinedTextField(
                        value = newListName,
                        onValueChange = { newListName = it },
                        label = { Text(text = "List Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDialog = false
                            viewModel.createTodoList(newListName)
                        }
                    ) {
                        Text("Create")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false}) {

                        Text("Dismiss")
                    }
                }
            )
        }


        if(temp.value && loginIsSuccessful){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment =  Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                content = {
                    items(items = listState.value){ list ->
                        TodoList(list, viewModel)
                    }
                })
        }
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        }
    }

}

@Composable
fun TodoList(list : ListResponseItem,viewModel: LoginViewModel) {
    val label = remember { mutableStateOf(list.title +"\t %"+ list.completion_percentage.toInt()) }
    var isFocused by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .width(450.dp)
        .padding(20.dp)
        .background(cardColor, RoundedCornerShape(8.dp))
    )
    {
        Column(modifier = Modifier
            .fillMaxWidth()
        ) {

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                BasicTextField(textStyle = TextStyle(fontSize = 20.sp,
                    fontWeight = FontWeight.Bold),
                    value = label.value,
                    onValueChange = {label.value = it},
                    maxLines = 1,
                    modifier = Modifier.padding(8.dp)

                )

                Row() {
                    Image(painter = painterResource(id = R.drawable.trashitem),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            viewModel.deleteTodoList(list.id)
                        })
                }
            }

            Spacer(modifier = Modifier
                .height(1.dp)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(Color.Gray))


        }

        val height = 55 + list.todo_items.size * 50
        LazyColumn(modifier = Modifier
            .width(450.dp)
            .height(height.dp),
            horizontalAlignment =  Alignment.Start,
            verticalArrangement = Arrangement.Center,content = {
                items(items = list.todo_items){ item ->
                    TodoItem(item,viewModel)
                }
                item {
                    val label2 = remember { mutableStateOf("") }
                    Row( modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(painter = painterResource(id = R.drawable.arrow),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 12.dp))
                            Image(painter = painterResource(id = R.drawable.add),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 0.dp, end = 0.dp)
                                    .clickable {

                                    })
                            BasicTextField(textStyle = TextStyle(fontSize = 16.sp),
                                value = label2.value,
                                onValueChange = {label2.value = it},
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = androidx.compose.ui.text.input.ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        viewModel.addItem(list.id, label2.value)
                                        isFocused = false
                                    }
                                ),
                                modifier = Modifier.onFocusChanged {
                                    isFocused = it.isFocused
                                },
                                decorationBox = { innerTextField ->
                                    Box(
                                        modifier = Modifier
                                            .width(200.dp)
                                            .border(
                                                width = 0.dp,
                                                color = cardColor,
                                                shape = RoundedCornerShape(5.dp)
                                            ).padding(end = 12.dp)

                                    ) {
                                        Row(
                                            modifier = Modifier.padding(15.dp)
                                        ) {
                                            if(label2.value.isEmpty())
                                                Text(text = "Add item", color = Color.LightGray)

                                            innerTextField.invoke()
                                        }
                                    }

                                }

                            )

                        }
                        if (isFocused){
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(painter = painterResource(id = R.drawable.check),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .clickable {
                                            viewModel.addItem(list.id, label2.value)
                                            isFocused = false
                                        })
                            }
                        }

                    }

                }
            })

    }

}


@Composable
fun TodoItem(item:TodoItem,viewModel: LoginViewModel) {

    val context = LocalContext.current
    val checkedState  = remember { mutableStateOf(item.is_completed) }
    val label = remember { mutableStateOf(item.content) }
    var isFocused by remember { mutableStateOf(false) }

    Row( modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(
            checked = checkedState.value,
            onCheckedChange = {
                checkedState.value = it
                viewModel.updateItem(item.todolist_id,item.id,label.value,checkedState.value)
            }
        )
        BasicTextField(textStyle = TextStyle(fontSize = 16.sp),
            value = label.value,
            onValueChange = {label.value = it},
            maxLines = 1,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = androidx.compose.ui.text.input.ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.updateItem(
                        list_id = item.todolist_id,
                        item_id = item.id,
                        content = label.value
                        , is_completed = checkedState.value)
                    Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                    isFocused = false
                }
            ),
            modifier = Modifier.onFocusChanged {
                isFocused = it.isFocused
            }

        )



        }
        if (isFocused){
            Row() {
                Image(painter = painterResource(id = R.drawable.check),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            viewModel.updateItem(
                                list_id = item.todolist_id,
                                item_id = item.id,
                                content = label.value, is_completed = checkedState.value
                            )
                            Toast
                                .makeText(context, "Updated", Toast.LENGTH_SHORT)
                                .show()
                            isFocused = false
                        })

                Image(painter = painterResource(id = R.drawable.trash),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            viewModel.deleteItem(list_id = item.todolist_id, item_id = item.id)
                            Toast
                                .makeText(context, "Deleted", Toast.LENGTH_SHORT)
                                .show()
                            isFocused = false
                        })

            }

        }

    }
}




@Composable
fun NewListDialog() {

}
