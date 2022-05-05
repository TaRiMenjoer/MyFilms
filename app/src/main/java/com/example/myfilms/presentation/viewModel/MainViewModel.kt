package com.example.myfilms.presentation.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.model.Session
import com.example.myfilms.presentation.view.LoginFragment
import com.example.myfilms.presentation.view.LoginFragment.Companion.APP_SETTINGS
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = ApiFactory.getInstance()
    private var prefSettings: SharedPreferences = application.getSharedPreferences(
        APP_SETTINGS,
        Context.MODE_PRIVATE
    ) as SharedPreferences
    private var editor: SharedPreferences.Editor = prefSettings.edit()


    fun deleteSession() {
//        val session_id = prefSettings.getString(LoginFragment.SESSION_ID_KEY , "") as String
//
//        viewModelScope.launch {
//            apiService.deleteSession(sessionId = com.example.myfilms.data.model.Session( session_id = session_id) )
//            editor.clear().commit()
//        }
        viewModelScope.launch {
            SESSION_ID = getSessionId()
            try {
                apiService.deleteSession(sessionId = Session(session_id = SESSION_ID))
            } catch (e: Exception) {
                editor.clear().commit()
            }
        }
    }
    private fun getSessionId(): String {
        var session = ""
        try {
            session =
                prefSettings.getString(SESSION_ID_KEY, "") as String
        } catch (e: Exception) {
        }
        return session
    }
    companion object {

        private var SESSION_ID = ""
        const val APP_SETTINGS = "Settings"
        const val SESSION_ID_KEY = "SESSION_ID"
    }


}