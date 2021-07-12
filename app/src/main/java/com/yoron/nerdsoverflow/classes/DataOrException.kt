package com.yoron.nerdsoverflow.classes

/**
 * This will return either data of the type you choose or an Exception.
 */
data class DataOrException<T , E: Exception?> (
    var data: T? = null,
    var e: E? = null
)
