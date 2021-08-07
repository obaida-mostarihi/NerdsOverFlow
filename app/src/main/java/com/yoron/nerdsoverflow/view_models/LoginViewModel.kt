/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _loginError = MutableLiveData<Error>()
    val loginError: LiveData<Error> = _loginError


    private val _dataOrException = MutableLiveData<AuthDataOrException>()
    val dataOrException: LiveData<AuthDataOrException> = _dataOrException


    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    /**
     * Login user to an existing account
     * @param email the user's email
     * @param password the user's password
     */
    fun loginToAccount(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = (true)
            _dataOrException.postValue(authRepo.loginUserToAnAccount(email, password))
            _loading.value = (false)
        }
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
    EMAIL, PASSWORD , RETYPE_PASSWORD
}

data class ErrorModel<T : ErrorType, E : Exception>(
    val type: T,
    val exception: E
)

typealias Error = ErrorModel<ErrorType, Exception>
typealias AuthDataOrException = DataOrException<AuthResult, Exception>