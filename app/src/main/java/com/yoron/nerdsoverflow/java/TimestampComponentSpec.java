/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:50 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:50 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.Output;
import com.facebook.litho.Size;
import com.facebook.litho.SizeSpec;
import com.facebook.litho.annotations.FromPrepare;
import com.facebook.litho.annotations.MountSpec;
import com.facebook.litho.annotations.OnBind;
import com.facebook.litho.annotations.OnBoundsDefined;
import com.facebook.litho.annotations.OnCreateMountContent;
import com.facebook.litho.annotations.OnMeasure;
import com.facebook.litho.annotations.OnMount;
import com.facebook.litho.annotations.OnPrepare;
import com.facebook.litho.annotations.OnUnbind;
import com.facebook.litho.annotations.OnUnmount;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.ShouldUpdate;
import com.facebook.litho.widget.Text;
import com.facebook.litho.widget.TextDrawable;
import com.facebook.yoga.YogaMeasureOutput;
import com.google.firebase.Timestamp;
import com.yoron.nerdsoverflow.R;

import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;

@MountSpec
class TimestampComponentSpec {
    @OnPrepare
    static void onPrepare(
            ComponentContext c,
            @Prop Timestamp timestamp,
            Output<String> date) {
        if(timestamp != null) {
            SimpleDateFormat year = new SimpleDateFormat("MM/dd");
            SimpleDateFormat hour = new SimpleDateFormat("HH:mm");

            date.set(year.format(timestamp.toDate()) + " at " + hour.format(timestamp.toDate()));
        }
    }

    @UiThread
    @OnCreateMountContent
    static TextView onCreateMountContent(Context c) {
        // TODO: create content that may be reused for other instances of this component.
        return new TextView(c);
    }

    @UiThread
    @OnMount
    static void onMount(
            ComponentContext context,
            TextView mountedDrawable,
            @FromPrepare String date) {
        mountedDrawable.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT , ViewGroup.LayoutParams.WRAP_CONTENT));
        mountedDrawable.setTextColor(ContextCompat.getColor(context.getAndroidContext() , R.color.darkColor));
        mountedDrawable.setAlpha(0.5f);
        mountedDrawable.setTextSize(10);
        mountedDrawable.setText(date == null?"":date);

    }

}