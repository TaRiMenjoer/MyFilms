package com.example.myfilms.presentation.login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myfilms.R
import com.example.myfilms.data.model.LoginApprove
import com.example.myfilms.domain.UseCases.GetResponseSessionUseCase
import com.example.myfilms.presentation.common.Utils.LoadingState
import kotlinx.coroutines.launch

class ViewModelLogin(application: Application ,
                     private val getResponseSessionUseCase: GetResponseSessionUseCase) : AndroidViewModel(application) {

    private val context = application

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState

    private val _sessionId = MutableLiveData<String>()
    val sessionId: LiveData<String>
        get() = _sessionId

    fun login(data: LoginApprove) {
        viewModelScope.launch {
            try {
                _loadingState.value = LoadingState.IS_LOADING

                val session = getResponseSessionUseCase.invoke(data)
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






