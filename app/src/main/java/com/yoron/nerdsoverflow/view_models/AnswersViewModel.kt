/*
 *
 * Created by Obaida Al Mostarihi on 7/21/21, 4:37 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/21/21, 4:37 AM
 *
 */

package com.yoron.nerdsoverflow.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.facebook.litho.*
import com.google.firebase.firestore.DocumentSnapshot
import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.java.OnCodeClickEvent
import com.yoron.nerdsoverflow.java.fullPost.AnswersEvent
import com.yoron.nerdsoverflow.models.AnswerModel
import com.yoron.nerdsoverflow.repositories.AnswersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnswersViewModel @Inject constructor(
    private val repo: AnswersRepository
) : LithoViewModel<AnswersEvent>() {


    private lateinit var fullAnswerListeners: FullAnswerListeners


    private val _answers = MutableLiveData<answersDataOrException>()


    fun loadAnswers(postId: String) {
        viewModelScope.launch {
            repo.getAnswers(null, postId) { answersDataOrException ->
                _answers.postValue(answersDataOrException)
                val answersEvent =
                    AnswersEvent()
                answersEvent.answers = answersDataOrException
                answersEvent.isEmpty = answersDataOrException.data?.isEmpty() ?: true
                answersEvent.isFirstLoad = true

                getLoadingEvent()?.dispatchEvent(answersEvent)
            }
        }
    }


    /**
     * Load more posts by passing the last post's document snapshot
     * @param documentSnapshot pass the last post's document snapshot
     */
    fun loadMoreAnswers(documentSnapshot: DocumentSnapshot?, postId: String) {
        if (documentSnapshot != null)
            viewModelScope.launch {
                repo.getAnswers(documentSnapshot, postId) { dataOrException ->
                    _answers.value?.data =
                        _answers.value?.data.orEmpty() + dataOrException.data.orEmpty()
                    val answersEvent =
                        AnswersEvent()
                    answersEvent.answers = _answers.value!!
                    answersEvent.isEmpty = dataOrException.data?.isEmpty() ?: true
                    answersEvent.isFirstLoad = false

                    getLoadingEvent()?.dispatchEvent(answersEvent)
                }

            }
    }


    fun setFullAnswerListeners(fullAnswerListeners: FullAnswerListeners) {
        this.fullAnswerListeners = fullAnswerListeners
    }


    private val hasCodeEventDispatcher = HasEventDispatcher {
        EventDispatcher { _, eventState ->
            val code = (eventState as OnCodeClickEvent).code
            fullAnswerListeners.onCodeClicked(code)
            null
        }
    }

    val onCodeClickEventEventHandler: EventHandler<OnCodeClickEvent> =
        EventHandler(hasCodeEventDispatcher, 1, null)

}//Class


interface FullAnswerListeners {
    fun onCodeClicked(code: String) {}
}
typealias answersDataOrException = DataOrException<List<AnswerModel>, Exception>