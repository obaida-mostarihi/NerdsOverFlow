/*
 *
 * Created by Obaida Al Mostarihi on 8/6/21, 3:02 PM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 8/6/21, 3:02 PM
 *
 */

package com.yoron.nerdsoverflow.java.home;

import android.view.View;

import com.facebook.litho.ClickEvent;
import com.facebook.litho.Column;
import com.facebook.litho.StateValue;
import com.facebook.litho.annotations.FromEvent;
import com.facebook.litho.annotations.OnCreateInitialState;
import com.facebook.litho.annotations.OnEvent;
import com.facebook.litho.annotations.OnUpdateState;
import com.facebook.litho.annotations.Param;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.annotations.State;
import com.facebook.litho.sections.Children;
import com.facebook.litho.sections.LoadingEvent;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.SectionLifecycle;
import com.facebook.litho.sections.annotations.GroupSectionSpec;
import com.facebook.litho.sections.annotations.OnBindService;
import com.facebook.litho.sections.annotations.OnCreateChildren;
import com.facebook.litho.sections.annotations.OnCreateService;
import com.facebook.litho.sections.annotations.OnRefresh;
import com.facebook.litho.sections.annotations.OnUnbindService;
import com.facebook.litho.sections.annotations.OnViewportChanged;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.OnCheckIsSameContentEvent;
import com.facebook.litho.sections.common.OnCheckIsSameItemEvent;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.Progress;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.litho.widget.SolidColor;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.google.firebase.firestore.DocumentReference;
import com.yoron.nerdsoverflow.R;
import com.yoron.nerdsoverflow.classes.DataOrException;
import com.yoron.nerdsoverflow.java.OnPostClickedEvent;
import com.yoron.nerdsoverflow.models.HomePostModel;
import com.yoron.nerdsoverflow.view_models.HomePostsViewModel;
import com.yoron.nerdsoverflow.view_models.UserProfileViewModel;

import java.util.Collections;
import java.util.List;

@GroupSectionSpec(events = {OnPostClickedEvent.class})
class UserPostsDiffSectionSectionSpec {

    @OnCreateInitialState
    static void createInitialState(
            final SectionContext c,
            StateValue<DataOrException<List<HomePostModel>, Exception>> posts,
            StateValue<Integer> start,
            StateValue<Integer> count,
            StateValue<Boolean> isFetching,
            StateValue<Boolean> isEmpty
    ) {
        start.set(0);
        count.set(7);
        isFetching.set(false);
        isEmpty.set(false);
        posts.set(new DataOrException<>(Collections.emptyList(), new Exception()));
    }



    @OnCreateChildren
    static Children onCreateChildren(
            SectionContext c,
            @State DataOrException<List<HomePostModel>, Exception> posts, @State boolean isEmpty) {
        Children.Builder builder = new Children.Builder();

        builder.child(
                DataDiffSection.<HomePostModel>create(c)
                        .data(posts == null ? Collections.emptyList() : posts.getData())
                        .renderEventHandler(UserPostsDiffSectionSection.onRender(c))


        );

        if (posts != null && posts.getData() != null && !posts.getData().isEmpty() && !isEmpty)
            builder.child(
                    SingleComponentSection.create(c).component(Column.create(c)
                            .child(
                                    Progress.create(c)
                                            .widthDip(40)
                                            .heightDip(40)
                                            .alignSelf(YogaAlign.CENTER)
                                            .build()
                            )
                    ).key("progress").build()
            );

        return builder.build();
    }

    @OnEvent(RenderEvent.class)
    static RenderInfo onRender(
            SectionContext c,
            @FromEvent int index,
            @FromEvent HomePostModel model,
            @Prop UserProfileViewModel viewModel) {

        return ComponentRenderInfo.create()
                .component(
                        Column.create(c)
                                .child(
                                        HomePostComponent.create(c)
                                                .post(model)
                                                .clickable(true)
                                                .focusable(true)
                                                .paddingDip(YogaEdge.VERTICAL , 10)
                                                .backgroundAttr(android.R.attr.selectableItemBackground)
                                                .clickHandler(UserPostsDiffSectionSection.onPostClicked(c, model ))
                                )
                                .child(
                                        SolidColor.create(c)
                                                .colorRes(R.color.darkColor)
                                                .widthPercent(100)
                                                .heightDip(0.5f)
                                                .alpha(0.3f)
                                                .paddingDip(YogaEdge.HORIZONTAL , 10)

                                ).key(index + "")


                )
                .build();
    }


    @OnEvent(ClickEvent.class)
    static void onPostClicked(SectionContext c, @FromEvent View view, @Param HomePostModel post ) {
//        homePostListeners.onPostClicked(c, post);
        UserPostsDiffSectionSection.dispatchOnPostClickedEvent(UserPostsDiffSectionSection.getOnPostClickedEventHandler(c), post , c);
    }




    @OnCreateService
    static UserProfileViewModel onCreateService(
            final SectionContext c,
            @State int start,
            @State int count,
            @Prop UserProfileViewModel viewModel

    ) {

        return viewModel;
    }


    @OnBindService
    static void onBindService(final SectionContext c, final UserProfileViewModel service

    ) {

        service.registerLoadingEvent(UserPostsDiffSectionSection.onDataLoaded(c));

    }

    @OnUnbindService
    static void onUnbindService(final SectionContext c, final UserProfileViewModel service) {

        service.unregisterLoadingEvent();


    }


    @OnEvent(HomePostsEvent.class)
    static void onDataLoaded(final SectionContext c, @FromEvent DataOrException<List<HomePostModel>, Exception> posts
            , @FromEvent boolean isEmpty) {
        UserPostsDiffSectionSection.updateData(c, posts, isEmpty);
        UserPostsDiffSectionSection.setFetching(c, false);
        SectionLifecycle.dispatchLoadingEvent(c, false, LoadingEvent.LoadingState.SUCCEEDED, null);
    }


    @OnUpdateState
    static void updateData(
            final StateValue<DataOrException<List<HomePostModel>, Exception>> posts,
            final StateValue<Integer> start,
            final StateValue<Boolean> isEmpty,
            @Param DataOrException<List<HomePostModel>, Exception> newPosts,
            @Param boolean updateEmpty
    ) {

        isEmpty.set(updateEmpty);
        posts.set(newPosts);

    }


    @OnRefresh
    static void onRefresh(
            SectionContext c,
            final UserProfileViewModel service,
            @State DataOrException<List<HomePostModel>, Exception> posts,
            @State int start,
            @State int count,
            @Prop DocumentReference userRef

    ) {
//        HomePostsDiffSectionSection.updateData(c, posts);

        service.refreshData(userRef);
    }


    @OnUpdateState
    static void setFetching(final StateValue<Boolean> isFetching, @Param boolean fetch) {
        isFetching.set(fetch);
    }

    @OnUpdateState
    static void updateStartParam(final StateValue<Integer> start, @Param int newStart) {
        start.set(newStart);
    }


    @OnViewportChanged
    static void onViewportChanged(
            SectionContext c,
            int firstVisiblePosition,
            int lastVisiblePosition,
            int firstFullyVisibleIndex,
            int lastFullyVisibleIndex,
            int totalCount,
            UserProfileViewModel service,
            @State DataOrException<List<HomePostModel>, Exception> posts,
            @State int start,
            @State int count,
            @State boolean isFetching,
            @Prop DocumentReference userRef

            ) {


        if (posts.getData() != null && totalCount == posts.getData().size() && !isFetching) {
            service.loadMorePosts(posts.getData().get(totalCount - 1).getDocumentSnapshot(),userRef);
            UserPostsDiffSectionSection.setFetching(c, true);

            UserPostsDiffSectionSection.updateStartParam(c, posts.getData().size());
        }
//        if(isFetching)
//        HomePostsDiffSectionSection.requestSmoothFocus(c , totalCount + 1 , SmoothScrollAlignmentType.SNAP_TO_CENTER);

    }
}