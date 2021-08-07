/*
 *
 * Created by Obaida Al Mostarihi on 7/25/21, 11:54 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/25/21, 11:54 PM
 *
 */

package com.yoron.nerdsoverflow.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.tiagohm.codeview.Language
import br.tiagohm.codeview.Theme
import com.yoron.nerdsoverflow.R
import kotlinx.android.synthetic.main.activity_full_screen_code.*

class FullScreenCodeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen_code)

        val code = intent.getStringExtra("code")
        fullScreenCodeExit.setOnClickListener {
            finish()
        }
        code?.let {
            fullScreenCodeView.setTheme(Theme.AGATE)
                .setLanguage(Language.AUTO)
                .setCode("$it\n\n\n")
                .setFontSize(14f)
                .setWrapLine(true)
                .setShowLineNumber(true)
                .setZoomEnabled(true)
                .setShowLineNumber(true)
                .setStartLineNumber(9000).apply()
        }


    }
}