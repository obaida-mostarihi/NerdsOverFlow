/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 3:04 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 3:04 PM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoron.nerdsoverflow.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repo: AuthRepository
): ViewModel() {
    private val _registerError = MutableLiveData<Error>()
    val registerError: LiveData<Error> = _registerError


    private val _dataOrException = MutableLiveData<AuthDataOrException>()
    val dataOrException: LiveData<AuthDataOrException> = _dataOrException

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading


    fun registerAnAccount(email:String , password: String){
        viewModelScope.launch {
            _loading.postValue(true)
            _dataOrException.value = repo.registerANewAccount(email, password)
            _loading.postValue(false)
        }
    }




    /**
     * Check if the EditTexts are empty or something is wrong.
     */
    fun checkEditTexts(email: String, password: String, retypePassword: String): Boolean {
        var isValid = true

        if (email.isBlank()) {
            _registerError.value = Error(ErrorType.EMAIL, Exception("This field is required."))
            isValid = false
        }

        if (password.isEmpty()) {
            _registerError.value = Error(ErrorType.PASSWORD, Exception("This field is required."))
            isValid = false
        }

        if (retypePassword.isEmpty()) {
            _registerError.value = Error(ErrorType.RETYPE_PASSWORD, Exception("This field is required."))
            isValid = false
        }

        if (retypePassword != password){
            _registerError.value = Error(ErrorType.RETYPE_PASSWORD, Exception("Passwords do not match."))

            isValid = false

        }

        return isValid
    }

}