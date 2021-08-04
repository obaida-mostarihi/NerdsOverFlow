/*
 *
 * Created by Obaida Al Mostarihi on 7/19/21, 6:10 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/19/21, 6:10 PM
 *
 */

package com.yoron.nerdsoverflow.custom_views;


import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.yoron.nerdsoverflow.R;


public class CustomBottomSheet extends LinearLayout {

    private static final int VIEW_MARGIN = 0;
    private float mCardMovement = 0, mSidesOffSets = 0;
    private float fingerOffSet = 0;
    private int l, t, r, b;
    private boolean isTouchingTop = false;
    boolean isClosed = true;

    private Paint bottomSheetPaint, paint;
    private Path bottomSheetPath;
    float[] radii = {0, 0, 0, 0, 0, 0, 0, 0};


    private BottomSheetListener bottomSheetListener;

    public CustomBottomSheet(@NonNull Context context) {
        super(context);
        init();

    }

    public CustomBottomSheet(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();


    }

    public CustomBottomSheet(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //..
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mCardMovement = displayMetrics.heightPixels;

        bottomSheetPaint = new Paint();
        paint = new Paint();

        setFocusableInTouchMode(true);
        requestFocus();
        setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (bottomSheetListener != null)
                    bottomSheetListener.bottomSheetState(false);
                animateToBottom();
            }
            return !isClosed;
        });
        bottomSheetPaint.setStyle(Paint.Style.FILL);
        bottomSheetPaint.setStrokeWidth(10f);
        bottomSheetPaint.setColor(ContextCompat.getColor(getContext(), R.color.lightColor));
        paint.setColor(ContextCompat.getColor(getContext(), R.color.darkColor));
        bottomSheetPaint.setShadowLayer(10, 0, 0, getResources().getColor(android.R.color.black));
        paint.setAntiAlias(true);
        bottomSheetPaint.setAntiAlias(true);
        bottomSheetPath = new Path();
        radii[0] = 50;
        radii[1] = 50;
        radii[2] = 50;
        radii[3] = 50;
        postInvalidate();

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (getChildAt(0) != null){
            getChildAt(0).layout(l + (int) mSidesOffSets, (int) (t +  mCardMovement), r - (int) mSidesOffSets, (int) (b + mCardMovement));
        }

        bottomSheetPath.reset();
        bottomSheetPath.addRoundRect(new RectF(mSidesOffSets, mCardMovement < 0 ? 0 : mCardMovement, getMeasuredWidth() - mSidesOffSets, getMeasuredHeight()), radii, Path.Direction.CW);

        if (mCardMovement >= getMeasuredHeight()) {
            setTranslationZ(-10f);

        } else {
            setTranslationZ(1000f);

        }

        bottomSheetPath.close();


        canvas.drawPath(bottomSheetPath, bottomSheetPaint);
        canvas.drawRoundRect(new RectF((getMeasuredWidth() / 2) - 50, mCardMovement + 30, (getMeasuredWidth() / 2) + 50, mCardMovement + 60), 20, 20, paint);

        super.dispatchDraw(canvas);

    }


    public void animateToTop() {
        isClosed = false;

        if (bottomSheetListener != null)
            bottomSheetListener.bottomSheetState(true);
        ValueAnimator animator = ValueAnimator.ofFloat(mCardMovement, 0);
//        ValueAnimator sidesAnimator = ValueAnimator.ofFloat(mSidesOffSets, 0f);
        animate(animator);
    }

    private void animateToCenter() {
        isClosed = false;

        if (bottomSheetListener != null)
            bottomSheetListener.bottomSheetState(true);
        ValueAnimator animator = ValueAnimator.ofFloat(mCardMovement, (float) getMeasuredHeight() / 2);
//        ValueAnimator sidesAnimator = ValueAnimator.ofFloat(mSidesOffSets,50f);
        animate(animator);


    }

    public void animateToBottom() {
        isClosed = true;

        ValueAnimator animator = ValueAnimator.ofFloat(mCardMovement, getMeasuredHeight());
//        ValueAnimator sidesAnimator = ValueAnimator.ofFloat(mSidesOffSets,50f);
        animate(animator);


    }


    private void animate(ValueAnimator animator) {
        animator.addUpdateListener(animation -> {
            Float value = (Float) animation.getAnimatedValue();

            mCardMovement = (float) value;



            if (getChildAt(0) != null){
                getChildAt(0).layout(l + (int) mSidesOffSets, (int) (t +  value), r - (int) mSidesOffSets, (int) (b + value));
            }

            postInvalidate();

        });
        animator.start();

    }


    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {

        final int count = getChildCount();

        int row = 0;// which row lay you view relative to parent
        int lengthX = arg1; // right position of child relative to parent
        int lengthY = arg2; // bottom position of child relative to parent
        for (int i = 0; i < count; i++) {

            final View child = this.getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();

            lengthX += width + VIEW_MARGIN;
            lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
                    + arg2;
            if (lengthX > arg3) {
                lengthX = width + VIEW_MARGIN + arg1;
                row++;
                lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height
                        + arg2;

            }

            l = lengthX - width;
            t = lengthY - height + 100;

            r = lengthX;
            b = lengthY;


            child.layout(l, t, r, b);
        }


    }

    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//
//        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//
//        if (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED) {
//            throw new IllegalStateException("Must measure with an exact size");
//        }
//
//        final int width = MeasureSpec.getSize(widthMeasureSpec);
//        final int height = MeasureSpec.getSize(heightMeasureSpec);
//
//
//
//        final int contentWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, width);
//        final int contentHeightMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, height);
//
//        getChildAt(0).measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
//
//
//
//        setMeasuredDimension(width, height);


        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            // measure


            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }


    private float getOffHalfPercent(float original, float discounted) {


        return (((original - discounted) / original) * 100) / 2;

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                fingerOffSet = mCardMovement - event.getRawY();

                isTouchingTop = y >= mCardMovement && y <= mCardMovement + 200;

                return true;
            case MotionEvent.ACTION_UP:


                if (isTouchingTop) {
                    if (y < getMeasuredHeight() / 3) {
                        animateToTop();
                    } else if (y > getMeasuredHeight() / 1.5)
                        animateToBottom();

                    else
                        animateToCenter();
                }else{
                    if(y<mCardMovement)
                        animateToBottom();
                }
                return true;

            case MotionEvent.ACTION_MOVE:

                if (isTouchingTop) {



//                    if(mCardMovement<=getMeasuredHeight()/2 && mCardMovement>=0){
//                        mSidesOffSets =50- getOffHalfPercent(getMeasuredHeight()/2,mCardMovement);
//                    }
//
                    if (mCardMovement >= 0) {
                        mCardMovement = (int) event.getRawY() + (int) fingerOffSet;

                        getChildAt(0).layout(l + (int) mSidesOffSets, t + (int) mCardMovement, r - (int) mSidesOffSets, b + (int) mCardMovement);
                    }
                    postInvalidate();


                }

                return true;


        }
        return true;

    }


    public void setBottomSheetListener(BottomSheetListener bottomSheetListener) {
        this.bottomSheetListener = bottomSheetListener;
    }

    public interface BottomSheetListener {
        void bottomSheetState(boolean isOpened);
    }

}//Class

