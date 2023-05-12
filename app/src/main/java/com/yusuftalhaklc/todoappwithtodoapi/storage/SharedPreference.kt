package com.yusuftalhaklc.todoappwithtodoapi.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AppDatastore(private var context: Context) {
    private val Context.ds : DataStore<Preferences> by preferencesDataStore("JWT")

    companion object{
        val JWT = stringPreferencesKey("JWT")
    }

    suspend fun editJWT(jwt:String){
        context.ds.edit {
            it[JWT] = jwt
        }
    }
    suspend fun readJWT():String{
        val p = context.ds.data.first()
        return p[JWT]?:"There is no JWT"
    }
}