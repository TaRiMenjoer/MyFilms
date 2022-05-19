package com.example.myfilms.presentation.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.R
import com.example.myfilms.data.ApiFactory
import com.example.myfilms.data.model.LoginApprove
import com.example.myfilms.data.model.Token
import com.example.myfilms.data.repository.MovieRepository
import com.example.myfilms.presentation.Utils.LoadingState
import kotlinx.coroutines.launch
import java.lang.Exception

class ViewModelLogin(application: Application) : AndroidViewModel(application) {

    private val context = application
  //  private val apiService = ApiFactory.getInstance()

    private val repository = MovieRepository(application)

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String>
        get() = _sessionId

    //    fun login(data: LoginApprove) {
//
//        viewModelScope.launch {
//            try {
//                _loadingState.value = LoadingState.IS_LOADING
//                val responseGet = apiService.getToken()
//                if (responseGet.isSuccessful) {
//                    val loginApprove = LoginApprove(
//                        username = data.username,
//                        password = data.password,
//                        request_token = responseGet.body()?.request_token as String
//                    )
//                    val responseApprove = apiService.approveToken(loginApprove = loginApprove)
//                    if (responseApprove.isSuccessful) {
//                        val session =
//                            apiService.createSession(token = responseApprove.body() as Token)
//                        if (session.isSuccessful) {
//                            _sessionId.value = session.body()?.session_id
//                            _loadingState.value = LoadingState.FINISHED
//                            _loadingState.value = LoadingState.SUCCESS
//                        }
//                    } else {
//                        _loadingState.value = LoadingState.FINISHED
//                        Toast.makeText(
//                            context,
//                            R.string.wrong_data,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            } catch (e: Exception) {
//                _loadingState.value = LoadingState.FINISHED
//                Toast.makeText(
//                    context,
//                    R.string.no_enternet_connection,
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//    }

    fun login(data: LoginApprove) {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.IS_LOADING

                val session = repository.getResponseSession(data)
                if (session.isSuccessful) {
                    _sessionId.value = session.body()?.session_id
                    _loadingState.value = LoadingState.FINISHED
                    _loadingState.value = LoadingState.SUCCESS
                } else {
                    _loadingState.value = LoadingState.FINISHED
                    Toast.makeText(
                        context,
                        R.string.wrong_data,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                _loadingState.value = LoadingState.FINISHED
                Toast.makeText(
                    context,
                    R.string.no_enternet_connection,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}






