/*
 *
 * Created by Obaida Al Mostarihi on 7/12/21, 8:24 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/12/21, 8:24 PM
 *
 */

package com.yoron.nerdsoverflow.customViews

import android.R
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView


class CoderWriterView constructor(context: Context , attrs: AttributeSet): AppCompatTextView(context , attrs) {
    private var mPlayer: MediaPlayer? = null

    private var mText: CharSequence? = null
    private var mPrintingText: String? = null
    private var mIndex = 0
            var mDelay: Long = 100 //Default 500ms delay


    private var mContext: Context? = null
    private var mTypeWriterListener: CoderWriterListener? = null

    private var mWithMusic = true
    private var animating = false

    private var mBlinker: Runnable? = null
    private var i = 0
    private val mHandler: Handler = Handler()
             var loopCursor = true
    private val mCharacterAdder: Runnable = object : Runnable {
        override fun run() {
            if (animating) {
                text = mText!!.subSequence(0, mIndex++).toString() + "_"
                //typing typed
                mTypeWriterListener?.onCharacterTyped(
                    mPrintingText,
                    mIndex
                )
                if (mIndex <= mText!!.length) {
                    mHandler.postDelayed(this, mDelay)
                } else {
                    if (mWithMusic) mPlayer!!.stop()
                    //typing end
                    mTypeWriterListener?.onTypingEnd(mPrintingText)
                    animating = false
                    callBlink()
                }
            }
        }
    }





    private fun callBlink() {
        mBlinker = Runnable {
            if (loopCursor)
            if (i++ % 2 != 0) {
                text = String.format("%s _", mText)
                mBlinker?.let { mHandler.postDelayed(it, 150) }
            } else {
                text = String.format("%s   ", mText)
                mBlinker?.let { mHandler.postDelayed(it, 150) }
            }
            else{
                if(i<=10)
                if (i++ % 2 != 0) {
                    text = String.format("%s _", mText)
                    mBlinker?.let { mHandler.postDelayed(it, 150) }
                } else {
                    text = String.format("%s   ", mText)
                    mBlinker?.let { mHandler.postDelayed(it, 150) }
                }
            }
        }
        mHandler.postDelayed(mBlinker!!, 150)
    }


    /**
     * plays music of type writer as characters are displayed, using MediaPlayer API
     */
    private fun playMusic() {
        if (mWithMusic) {
            mPlayer = MediaPlayer.create(context, com.yoron.nerdsoverflow.R.raw.typing)
            mPlayer!!.isLooping = true
            mPlayer!!.start()
        }
    }


    /**
     * Call this function to display
     *
     * @param text attribute
     */
    fun animateText(text: String?) {
        if (!animating) {
            animating = true
            mText = text
            mPrintingText = text
            mIndex = 0
            playMusic()
            setText("")
            mHandler.removeCallbacks(mCharacterAdder)
            //typing start
            mTypeWriterListener?.onTypingStart(mPrintingText)
            mHandler.postDelayed(mCharacterAdder, mDelay)
        } else {
            //CAUTION: Already typing something..
            Toast.makeText(mContext, "Typewriter busy typing: $mText", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Call this function to set delay in MILLISECOND [20..150]
     *
     * @param delay
     */
    fun setDelay(delay: Int) {
        if (delay >= 20 && delay <= 150) mDelay = delay.toLong()
    }

    /**
     * Whether to play music or not while animating
     *
     * @param music
     */
    fun setWithMusic(music: Boolean) {
        mWithMusic = music
    }

    /**
     * Call this to remove animation at any time
     */
    fun removeAnimation() {
        mHandler.removeCallbacks(mCharacterAdder)
        if (mWithMusic && mPlayer != null && mPlayer!!.isPlaying) mPlayer!!.stop()
        animating = false
        text = mPrintingText

        //typing removed
        mTypeWriterListener?.onTypingRemoved(mPrintingText)
    }

    /**
     * Set listener to receive typing effects
     *
     * @param typeWriterListener
     */
    fun setTypeWriterListener(typeWriterListener: CoderWriterListener?) {
        mTypeWriterListener = typeWriterListener
    }
}