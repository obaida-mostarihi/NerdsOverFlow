/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 2:04 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:04 PM
 *
 */

package com.yoron.nerdsoverflow.viewModels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.litho.ComponentTree
import com.facebook.litho.EventHandler
import com.facebook.litho.StateHandler
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase

import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.java.HomePostsEvent
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.repositories.HomePostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePostsViewModel @Inject constructor(
    val repo: HomePostsRepository
):ViewModel(){

    private var homePostsLoadingEventHandler: EventHandler<HomePostsEvent>? = null

    fun registerLoadingEvent(homePostsLoadingEventHandler: EventHandler<HomePostsEvent>?) {
        this.homePostsLoadingEventHandler = homePostsLoadingEventHandler
    }


    fun unregisterLoadingEvent() {
        homePostsLoadingEventHandler = null
    }





    private val _posts = MutableLiveData<DataOrException<HomePostsList, Exception>> ()
    val posts: LiveData<DataOrException<HomePostsList , Exception>> = _posts


    /**
     * Save the scroll state...
     */
    private val stateHandlerData: MutableLiveData<StateHandler> = MutableLiveData<StateHandler>()

    fun getStateHandler(): StateHandler? {
        return stateHandlerData.value
    }

    fun updateStateHandler(componentTree: ComponentTree?) {
        if (componentTree != null) {
            /**
             * The current state values are wrapped in a StateHandler that lives on the ComponentTree.
             * call acquireStateHandler to obtain a copy.
             */
            stateHandlerData.value = componentTree.acquireStateHandler()
        }
    }


    /**
     * Load the data by initializing the class
     */
    init {
        viewModelScope.launch {
            loadData()
        }
    }



    private suspend fun loadData(){

        repo.getPostsData {dataOrException ->
            _posts.postValue(dataOrException)
            val homePostsEvent  = HomePostsEvent()
            homePostsEvent.posts = dataOrException
            homePostsEvent.isEmpty = dataOrException.data?.isEmpty() ?: true
            homePostsEvent.isFirstLoad = true

            homePostsLoadingEventHandler?.dispatchEvent(homePostsEvent)
        }


    }


    /**
     * Load more posts by passing the last post's document snapshot
     * @param documentSnapshot pass the last post's document snapshot
     */
    fun loadMorePosts(documentSnapshot: DocumentSnapshot?){
        if(documentSnapshot != null)
            viewModelScope.launch {
                repo.getPostsData(documentSnapshot) { dataOrException ->
                    _posts.value?.data = _posts.value?.data.orEmpty() + dataOrException.data.orEmpty()
                    val homePostsEvent  = HomePostsEvent()
                    homePostsEvent.posts = _posts.value!!
                    homePostsEvent.isEmpty = dataOrException.data?.isEmpty() ?: true
                    homePostsEvent.isFirstLoad = false

                    homePostsLoadingEventHandler?.dispatchEvent(homePostsEvent)
                }

            }
    }


    /**
     * Refresh replace the post array with a new one
     */
    fun refreshData(){

        viewModelScope.launch{
            loadData()
        }
    }










}
typealias HomePostsList = List<HomePostModel>
