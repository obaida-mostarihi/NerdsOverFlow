/*
 *
 * Created by Obaida Al Mostarihi on 7/13/21, 3:19 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/13/21, 3:19 PM
 *
 */

package com.yoron.nerdsoverflow.java;

import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.Column;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.SolidColor;
import com.facebook.litho.widget.Text;
import com.facebook.litho.widget.TextAlignment;
import com.facebook.yoga.YogaEdge;
import com.yoron.nerdsoverflow.R;
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
                        UserImageNameComponent.create(c).user(post.getUser())
                        .timestamp(post.getTimestamp())
                )
                .paddingDip(YogaEdge.HORIZONTAL , 10)

                .build();
    }
}