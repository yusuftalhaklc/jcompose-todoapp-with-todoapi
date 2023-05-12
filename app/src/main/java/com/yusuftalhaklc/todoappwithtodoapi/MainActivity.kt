package com.yusuftalhaklc.todoappwithtodoapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yusuftalhaklc.todoappwithtodoapi.ui.theme.TodoAppWithTodoAPITheme
import com.yusuftalhaklc.todoappwithtodoapi.viewmodels.LoginViewModel
import com.yusuftalhaklc.todoappwithtodoapi.views.LoginScreen
import com.yusuftalhaklc.todoappwithtodoapi.views.TodoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            TodoAppWithTodoAPITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val viewModel: LoginViewModel = LoginViewModel()
                    val context = application.applicationContext
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "todo_login_screen"
                    ) {

                        composable(route = "todo_login_screen") {
                            LoginScreen(navController = navController,viewModel)
                        }

                        composable(route = "todo_lists_screen") {
                            TodoScreen(navController = navController,viewModel)
                        }

                    }
                }
            }
        }
    }
}
