package com.yusuftalhaklc.todoappwithtodoapi.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuftalhaklc.todoappwithtodoapi.Network.ApiClient
import com.yusuftalhaklc.todoappwithtodoapi.models.Content
import com.yusuftalhaklc.todoappwithtodoapi.models.CreateListBody
import com.yusuftalhaklc.todoappwithtodoapi.models.ListResponse
import com.yusuftalhaklc.todoappwithtodoapi.models.UpdateBody
import kotlinx.coroutines.launch

class LoginViewModel(): ViewModel() {

    private val api = ApiClient.getClient()
    val loginIsSuccessful = MutableLiveData <Boolean> ()
    val loginMsg = MutableLiveData <String> ()
    val loginIsLoading = MutableLiveData <Boolean> ()

    val todolistLoginIsSuccessful : MutableLiveData <Boolean>  = MutableLiveData()
    private val todolistLoginIsLoading = MutableLiveData <Boolean> ()
    private val authtoken = MutableLiveData <String> ()

    val todoLists = MutableLiveData <ListResponse>()



    fun login(username: String, password: String) {
        loginIsLoading.value = true
        authtoken.value = "initial"
        viewModelScope.launch {
            try {
                val response = api.login(username, password)
                val token = response.body()?.access_token

                if (response.isSuccessful) {
                    loginMsg.value = "Logged in successfully!"
                    loginIsSuccessful.value = true
                    loginIsLoading.value = false
                    if (token != null) {
                        authtoken.value = token.toString()
                    }
                    getTodoList()
                    Log.d("Login JWT", authtoken.value!!)
                    if (loginIsSuccessful.value == true) {
                        loginIsLoading.value = false

                    }
                } else {
                    loginMsg.value = "Invalid email or password"
                    loginIsSuccessful.value = false

                }
            } catch (e: Exception) {
                e.message?.let {
                    Log.d("Login", it)
                }
                loginMsg.value = "Something went wrong, Please try again Later"


                loginIsSuccessful.value = false
                loginIsLoading.value = false
            }
        }
    }

     fun getTodoList() {
        loginIsLoading.value = true
         Log.d("Login TLVM Response", todoLists.value.toString())
         viewModelScope.launch {
            try {
                val response = api.getTodos("Bearer ${authtoken.value}")
                if (response.isSuccessful) {
                    val list = response.body()!!

                    todoLists.value = list


                    todolistLoginIsSuccessful.value = true
                    todolistLoginIsLoading.value = false
                    todolistLoginIsLoading.value = false
                } else {
                    todolistLoginIsLoading.value = false
                    Log.d("Login TLVM Response", response.message())
                }

            } catch (e: Exception) {
                todolistLoginIsLoading.value = false
                e.message?.let {
                    Log.d("Login TLVM", it)
                }

            }
        }
    }

    fun createTodoList(title:String){
        viewModelScope.launch {
            try {
               val response =  api.createTodoList(token = "Bearer ${authtoken.value}", createListBody = CreateListBody(title))
                if (response.isSuccessful){
                    getTodoList()
                }
            }catch (e: Exception) {
                e.printStackTrace()
                getTodoList()
                e.message?.let { Log.d("Login Delete func ", it )}
            }
        }
    }

    fun deleteTodoList(list_id : Int){
        viewModelScope.launch {
            try {
                api.deleteTodoList(token = "Bearer ${authtoken.value}", list_id = list_id)


            }catch (e: Exception) {
                e.printStackTrace()
                getTodoList()
            }
            }
    }
    fun deleteItem(list_id : Int, item_id: Int){
        viewModelScope.launch {
            try {
                api.deleteTodoItem(token = "Bearer ${authtoken.value}", list_id = list_id, item_id = item_id)

            }catch (e: Exception) {
                e.printStackTrace()
                getTodoList()
            }
        }
    }

    fun addItem(list_id : Int, content:String){
        viewModelScope.launch {
            try {
                val response = api.addItem(token = "Bearer ${authtoken.value}", list_id = list_id , content = Content(content) )
                if (response.isSuccessful){
                    getTodoList()
                }


            }catch (e: Exception) {
                e.printStackTrace()

            }
        }
    }
    fun updateItem(list_id : Int, item_id: Int, content:String,is_completed :Boolean){
        viewModelScope.launch {
            try {
                val response = api.updateItem(token = "Bearer ${authtoken.value}",
                    list_id = list_id ,
                    item_id = item_id,
                    updateBody =UpdateBody(content = content, is_completed = is_completed) )
                if (response.isSuccessful){
                    getTodoList()
                }

            }catch (e: Exception) {
                e.printStackTrace()
                Log.d("ADD func 3", e.message.toString() )

            }
        }
    }
}