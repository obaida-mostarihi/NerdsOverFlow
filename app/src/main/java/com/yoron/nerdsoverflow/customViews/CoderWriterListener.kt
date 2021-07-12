package com.yoron.nerdsoverflow.customViews

interface CoderWriterListener {
    fun onTypingStart(text: String?)

    fun onTypingEnd(text: String?)

    fun onCharacterTyped(text: String?, position: Int)

    fun onTypingRemoved(text: String?)
}