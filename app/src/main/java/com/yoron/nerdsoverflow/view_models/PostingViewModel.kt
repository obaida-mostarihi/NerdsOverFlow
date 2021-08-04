/*
 *
 * Created by Obaida Al Mostarihi on 8/3/21, 1:14 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/3/21, 1:14 PM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.repositories.PostingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostingViewModel @Inject constructor(
    private val repo : PostingRepository
) : ViewModel(){

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading


    fun publishPost(post: HomePostModel){
        viewModelScope.launch {
            _loading.postValue(true)
            repo.publishPost(post)
            _loading.postValue(false)

        }
    }

}