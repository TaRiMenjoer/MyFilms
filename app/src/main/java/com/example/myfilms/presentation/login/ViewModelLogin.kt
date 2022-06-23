package com.example.myfilms.presentation.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount


    fun login(data: LoginApprove , userName: String , password: String) {
        viewModelScope.launch {
            try {

                val result = validateInput(userName , password)
                if(result) {
                    _loadingState.value = LoadingState.IS_LOADING
                    val session = getResponseSessionUseCase.invoke(data)
                    if (session.isSuccessful) {
                        _sessionId.value = session.body()?.session_id
                        _loadingState.value = LoadingState.FINISHED
                        _loadingState.value = LoadingState.SUCCESS
                    } else {
                        _loadingState.value = LoadingState.FINISHED

                        _errorInputName.value = true
                        _errorInputCount.value = true
                    }
                } else{
                    _errorInputName.value = true
                    _errorInputCount.value = true
                }

            } catch (e: Exception) {
                _loadingState.value = LoadingState.FINISHED
                _errorInputName.value = true
                _errorInputCount.value = true
            }
        }

    }


    private fun validateInput(userName: String, password: String): Boolean {
        var result = true
        if (userName.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (password.isBlank()) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputUserName() {
        _errorInputName.value = false
    }

    fun resetErrorInputPassword() {
        _errorInputCount.value = false
    }



}






