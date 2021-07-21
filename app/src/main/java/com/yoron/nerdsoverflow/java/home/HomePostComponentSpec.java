/*
 *
 * Created by Obaida Al Mostarihi on 7/20/21, 6:08 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/18/21, 3:22 AM
 *
 */

package com.yoron.nerdsoverflow.java.home;

import android.text.Layout;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.SolidColor;
import com.facebook.litho.widget.Text;
import com.facebook.litho.widget.TextAlignment;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.yoron.nerdsoverflow.R;
import com.yoron.nerdsoverflow.java.UserImageNameComponent;
import com.yoron.nerdsoverflow.models.HomePostModel;

@LayoutSpec
class HomePostComponentSpec {


    @OnCreateLayout
    static Component onCreateLayout(ComponentContext c,
                                    @Prop HomePostModel post
    ) {


        return Column.create(c)
                .child(
                        Text.create(c)
                                .text(post.getTitle())
                                .textColorRes(R.color.darkColor)
                                .alignment(TextAlignment.LAYOUT_START)
                                .textSizeSp(18)
                                .maxLines(2)

                )
                .child(
                        getUserRow(c , post)
                )
                .paddingDip(YogaEdge.HORIZONTAL, 10)

                .build();
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