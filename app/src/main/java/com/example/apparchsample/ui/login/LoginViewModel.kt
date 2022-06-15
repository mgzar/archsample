package com.example.apparchsample.ui.login

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.apparchsample.database.getDatabase
import com.example.apparchsample.repository.FunMarsRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FunMarsRepository(getDatabase(application))
    private var _isNetworkError = MutableLiveData(false)
    var loginResponse = repository.loginResponse
    var email = ObservableField<String>()
    var password = ObservableField<String>()

    val isNetworkError: LiveData<Boolean>
        get() = _isNetworkError

    fun setLogin(username: String, password: String) {
        viewModelScope.launch {
            try {
                repository.setLogin(username, password)
            } catch (e: Exception) {
                _isNetworkError.value = true
                Log.d("#LoginViewModelVM", e.toString())
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}