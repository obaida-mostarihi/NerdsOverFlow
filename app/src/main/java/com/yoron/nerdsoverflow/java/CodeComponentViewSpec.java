/*
 *
 * Created by Obaida Al Mostarihi on 7/20/21, 9:20 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/20/21, 9:20 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.facebook.litho.ComponentContext;

import com.facebook.litho.Output;
import com.facebook.litho.annotations.MountSpec;

import com.facebook.litho.annotations.OnCreateMountContent;
import com.facebook.litho.annotations.OnMount;
import com.facebook.litho.annotations.OnPrepare;
import com.facebook.litho.annotations.OnUnmount;
import com.facebook.litho.annotations.Prop;

import com.yoron.nerdsoverflow.R;

import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;

import br.tiagohm.codeview.CodeView;
import br.tiagohm.codeview.Language;
import br.tiagohm.codeview.Theme;


@MountSpec
class CodeComponentViewSpec {
    // For more information on creating MountSpec read docs at https://fblitho.com/docs/mount-specs


    @UiThread
    @OnCreateMountContent
    static CodeView onCreateMountContent(Context c) {

        return new CodeView(c);
    }

    @UiThread
    @OnMount
    static void onMount(
            ComponentContext context,
            CodeView codeView,
            @Prop String code) {


        codeView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (code != null)
            codeView
                    .setLanguage(Language.AUTO)
                    .setTheme(Theme.AGATE)
                    .setCode(code)
                    .setFontSize(14)
                    .setWrapLine(true)
                    .apply();
        codeView.setBackgroundColor(ContextCompat.getColor(context.getAndroidContext(), R.color.lightColor));


//        webView.setFocusable(true);
//
//    Codeview.with(context.getAndroidContext())
//                .withCode(code)
//                .setStyle(Settings.WithStyle.GITHUB)
//                .into(webView);
    }


}