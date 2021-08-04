/*
 *
 * Created by Obaida Al Mostarihi on 8/2/21, 8:41 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/2/21, 8:41 AM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.models.UserModel
import com.yoron.nerdsoverflow.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repo: MainRepository
) : ViewModel() {

    private val _hasDetails = MutableLiveData<DataOrException<UserModel, Exception>>()
    val hasDetails: LiveData<DataOrException<UserModel, Exception>> = _hasDetails


    fun checkForDetails(){
        viewModelScope.launch {
            _hasDetails.value = repo.hasDetails()
        }
    }

}