/*
 *
 * Created by Obaida Al Mostarihi on 7/20/21, 6:09 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/20/21, 6:08 AM
 *
 */

package com.yoron.nerdsoverflow.java.fullPost;

import android.view.View;

import com.facebook.litho.ClickEvent;
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.EventHandler;
import com.facebook.litho.LongClickEvent;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Image;
import com.facebook.litho.widget.SolidColor;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaPositionType;
import com.yoron.nerdsoverflow.R;
import com.yoron.nerdsoverflow.java.CodeComponentView;
import com.yoron.nerdsoverflow.java.OnCodeClickEvent;
import com.yoron.nerdsoverflow.java.UserImageNameComponent;
import com.yoron.nerdsoverflow.models.HomePostModel;

@LayoutSpec(events = OnCodeClickEvent.class)
class FullPostTopComponentSpec {


    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop HomePostModel post
    ) {
        return Column.create(c)
                .child(
                        Text.create(c)
                                .text(post.getTitle())
                                .textSizeSp(18)
                                .textColorRes(R.color.darkColor)
                                .marginDip(YogaEdge.HORIZONTAL, 16)
                                .extraSpacingDip(3)

                )
                .child(
                        Text.create(c)
                                .text(post.getQuestion())
                                .marginDip(YogaEdge.TOP, 16)
                                .textSizeSp(14)
                                .alpha(0.9f)
                                .textColorRes(R.color.darkColor)
                                .extraSpacingDip(3)

                )
                .child(
                        Column.create(c).child(
                                CodeComponentView.create(c)
                                        .code(post.getCode())
                                        .widthPercent(100)
                                        .heightDip(100)
                                        .marginDip(YogaEdge.TOP , 16)
                                        .clickable(true)
                        ).child(
                                Image.create(c)
                                        .drawableRes(R.drawable.ic_fullscreen)
                                        .positionType(YogaPositionType.ABSOLUTE)
                                        .alignSelf(YogaAlign.FLEX_END)
                                        .clickable(true)
                                        .foregroundAttr(android.R.attr.selectableItemBackgroundBorderless)

                                        .clickHandler(FullPostTopComponent.onCodeClicked(c , post.getCode()))
                        )


                )
                .child(
                        getUserRow(c , post)
                        .marginDip(YogaEdge.TOP , 16)
                )
                .child(
                        SolidColor.create(c)
                                .colorRes(R.color.darkColor)
                                .widthPercent(100)
                                .heightDip(0.5f)
                                .alpha(0.3f)
                                .marginDip(YogaEdge.TOP , 10)
                )
                .build();
    }

    @OnEvent(ClickEvent.class)
    static void onCodeClicked(ComponentContext c, @FromEvent View view , @Param String code) {
        EventHandler handler = FullPostTopComponent.getOnCodeClickEventHandler(c);
        if(handler != null)
        FullPostTopComponent.dispatchOnCodeClickEvent(handler , code);
    }

    private static Row.Builder getUserRow(ComponentContext c , HomePostModel post){
        Row.Builder userImageNameAnsweredRowBuilder = Row.create(c).widthPercent(100);
        userImageNameAnsweredRowBuilder.child(
                UserImageNameComponent.create(c).user(post.getUser())
                        .timestamp(post.getTimestamp())
        );
        if (post.getAnswered() != null && post.getAnswered())
            userImageNameAnsweredRowBuilder.child(
                    Text.create(c).text("Answered")
                            .textColorRes(R.color.greenColor)
                            .textSizeSp(12)
                            .backgroundRes(R.drawable.answered_shape)
                            .paddingDip(YogaEdge.HORIZONTAL, 7)
                            .paddingDip(YogaEdge.VERTICAL, 3)
                            .alignSelf(YogaAlign.CENTER)
            );
        return userImageNameAnsweredRowBuilder;
    }
}