/*
 *
 * Created by Obaida Al Mostarihi on 7/24/21, 7:13 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/24/21, 7:13 PM
 *
 */

package com.yoron.nerdsoverflow.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.litho.ComponentTree
import com.facebook.litho.EventHandler
import com.facebook.litho.StateHandler
import com.yoron.nerdsoverflow.java.home.HomePostsEvent

/**
 * A ViewModel that contains everything we need to store (State , event handler).
 * @property E The event handler type that needs to be registered.
 */
open class LithoViewModel<E> : ViewModel() {


    private var loadingEvent: EventHandler<E>? = null

    fun getLoadingEvent() = loadingEvent

    fun registerLoadingEvent(loadingEvent: EventHandler<E>?) {
        this.loadingEvent = loadingEvent
    }

    fun unregisterLoadingEvent() {
        loadingEvent = null
    }






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

}