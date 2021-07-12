package com.yoron.nerdsoverflow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    private val _loginError = MutableLiveData<Error>()
    val loginError: LiveData<Error> = _loginError


    /**
     * Login user to an existing account
     * @param email the user's email
     * @param password the user's password
     */
    fun loginToAccount(email: String, password: String){

    }







    /**
     * Check if the EditTexts are empty or something is wrong.
     */
    fun checkEditTexts(email: String, password: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            _loginError.value = Error(ErrorType.EMAIL, Exception("This field is required."))
            isValid = false
        }

        if (password.isEmpty()) {
            _loginError.value = Error(ErrorType.PASSWORD, Exception("This field is required."))
            isValid = false
        }

        return isValid
    }







}


enum class ErrorType {
    EMAIL, PASSWORD
}

data class ErrorModel<T : ErrorType, E : Exception>(
    val type: T,
    val exception: E
)

typealias Error = ErrorModel<ErrorType, Exception>