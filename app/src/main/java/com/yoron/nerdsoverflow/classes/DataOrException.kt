/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.classes

/**
 * This will return either data of the type you choose or an Exception.
 */
data class DataOrException<T , E> (
    var data: T? = null,
    var e: E? = null
)
