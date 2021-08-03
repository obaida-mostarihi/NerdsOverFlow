/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 2:04 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 2:04 PM
 *
 */

package com.yoron.nerdsoverflow.viewModels


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.facebook.litho.EventDispatcher
import com.facebook.litho.EventHandler
import com.facebook.litho.HasEventDispatcher
import com.google.firebase.firestore.DocumentSnapshot

import com.yoron.nerdsoverflow.classes.DataOrException
import com.yoron.nerdsoverflow.java.OnPostClickedEvent
import com.yoron.nerdsoverflow.java.home.HomePostsEvent
import com.yoron.nerdsoverflow.models.HomePostModel
import com.yoron.nerdsoverflow.repositories.HomePostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomePostsViewModel @Inject constructor(
    val repo: HomePostsRepository
) : LithoViewModel<HomePostsEvent>() {
    private lateinit var homePostListeners: HomePostListeners


    private val _posts = MutableLiveData<DataOrException<HomePostsList, Exception>>()
    val posts: LiveData<DataOrException<HomePostsList, Exception>> = _posts


    /**
     * Load the data by initializing the class
     */
    init {
        viewModelScope.launch {
            loadData()
        }
    }


    private suspend fun loadData() {

        repo.getPostsData { dataOrException ->
            _posts.postValue(dataOrException)
            val homePostsEvent =
                HomePostsEvent()
            homePostsEvent.posts = dataOrException
            homePostsEvent.isEmpty = dataOrException.data?.isEmpty() ?: true
            homePostsEvent.isFirstLoad = true

            getLoadingEvent()?.dispatchEvent(homePostsEvent)
        }


    }


    /**
     * Load more posts by passing the last post's document snapshot
     * @param documentSnapshot pass the last post's document snapshot
     */
    fun loadMorePosts(documentSnapshot: DocumentSnapshot?) {
        if (documentSnapshot != null)
            viewModelScope.launch {
                repo.getPostsData(documentSnapshot) { dataOrException ->
                    _posts.value?.data =
                        _posts.value?.data.orEmpty() + dataOrException.data.orEmpty()
                    val homePostsEvent =
                        HomePostsEvent()
                    homePostsEvent.posts = _posts.value!!
                    homePostsEvent.isEmpty = dataOrException.data?.isEmpty() ?: true
                    homePostsEvent.isFirstLoad = false

                    getLoadingEvent()?.dispatchEvent(homePostsEvent)
                }

            }
    }


    /**
     * Refresh replace the post array with a new one
     */
    fun refreshData() {

        viewModelScope.launch {
            loadData()
        }
    }

    fun setHomePostListeners(homePostListeners: HomePostListeners) {
        this.homePostListeners = homePostListeners
    }

    private val hasEventDispatcher = HasEventDispatcher { getPostEventDispatcher() }

    /**
     * This triggers when the post is clicked.
     */
    private fun getPostEventDispatcher() =
        EventDispatcher { _, eventState ->
            val onClickEvent = eventState as OnPostClickedEvent
            homePostListeners.onPostClicked(onClickEvent.post)
            null
        }

    val onPostClickEventEventHandler: EventHandler<OnPostClickedEvent> =
        EventHandler(hasEventDispatcher, 1, null)

}

interface HomePostListeners {
    fun onPostClicked(post: HomePostModel) {}
}
typealias HomePostsList = List<HomePostModel>
