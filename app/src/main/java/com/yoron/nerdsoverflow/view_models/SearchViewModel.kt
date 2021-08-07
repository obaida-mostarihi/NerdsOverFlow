/*
 *
 * Created by Obaida Al Mostarihi on 8/7/21, 7:40 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/7/21, 7:40 PM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoron.nerdsoverflow.models.UserModel
import com.yoron.nerdsoverflow.repositories.SearchUsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchUsersRepository
) : ViewModel() {


    private val _users = MutableLiveData<List<UserModel>>()
    val users: LiveData<List<UserModel>> = _users


    fun getUsersByName(username: String) {
        viewModelScope.launch {
            _users.postValue(repository.getUsersList(username))
        }
    }

}