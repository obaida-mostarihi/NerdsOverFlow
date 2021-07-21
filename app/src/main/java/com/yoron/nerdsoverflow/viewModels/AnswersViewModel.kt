/*
 *
 * Created by Obaida Al Mostarihi on 7/21/21, 4:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/21/21, 4:37 AM
 *
 */

package com.yoron.nerdsoverflow.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.litho.EventHandler
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.java.fullPost.AnswersEvent
import com.yoron.nerdsoverflow.java.home.HomePostsEvent
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.repositories.AnswersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswersViewModel @Inject constructor(
    private val repo: AnswersRepository
) : ViewModel() {
    private var answersPostsLoadingEventHandler: EventHandler<AnswersEvent>? = null

    fun registerLoadingEvent(answersPostsLoadingEventHandler: EventHandler<AnswersEvent>?) {
        this.answersPostsLoadingEventHandler = answersPostsLoadingEventHandler
    }


    fun unregisterLoadingEvent() {
        answersPostsLoadingEventHandler = null
    }




    private val _answers = MutableLiveData<answersDataOrException>()
    val answers: LiveData<answersDataOrException> = _answers


    fun getAnswers(postId: String) {
        viewModelScope.launch {
            repo.getAnswers(null, postId) {answersDataOrException ->
                _answers.postValue(answersDataOrException)
                val answersEvent  =
                    AnswersEvent()
                answersEvent.answers = answersDataOrException
                answersEvent.isEmpty = answersDataOrException.data?.isEmpty() ?: true
                answersEvent.isFirstLoad = true

                answersPostsLoadingEventHandler?.dispatchEvent(answersEvent)
            }
        }
    }


}
typealias answersDataOrException = DataOrException<List<AnswerModel>, Exception>