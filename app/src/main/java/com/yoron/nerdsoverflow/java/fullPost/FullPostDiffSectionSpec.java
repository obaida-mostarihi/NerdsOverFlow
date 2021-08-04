/*
 *
 * Created by Obaida Al Mostarihi on 7/20/21, 6:09 AM
 * Copyright (c) 2021 . All rights reserved.
 * Last modified 7/20/21, 6:07 AM
 *
 */

package com.yoron.nerdsoverflow.java.fullPost;

import com.facebook.litho.Column;
import com.facebook.litho.EventHandler;
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
import com.facebook.litho.sections.annotations.OnUnbindService;
import com.facebook.litho.sections.annotations.OnViewportChanged;
import com.facebook.litho.sections.common.DataDiffSection;
import com.facebook.litho.sections.common.RenderEvent;
import com.facebook.litho.sections.common.SingleComponentSection;
import com.facebook.litho.widget.ComponentRenderInfo;
import com.facebook.litho.widget.Progress;
import com.facebook.litho.widget.RenderInfo;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.yoron.nerdsoverflow.classes.DataOrException;
import com.yoron.nerdsoverflow.java.OnCodeClickEvent;
import com.yoron.nerdsoverflow.models.AnswerModel;
import com.yoron.nerdsoverflow.models.HomePostModel;
import com.yoron.nerdsoverflow.view_models.AnswersViewModel;


import java.util.Collections;
import java.util.List;

@GroupSectionSpec
class FullPostDiffSectionSpec {


    @OnCreateInitialState
    static void createInitialState(
            final SectionContext c,
            StateValue<DataOrException<List<AnswerModel>, Exception>> answers,
            StateValue<Integer> start,
            StateValue<Integer> count,
            StateValue<Boolean> isFetching,
            StateValue<Boolean> isEmpty
    ) {
        start.set(0);
        count.set(7);
        isFetching.set(false);
        isEmpty.set(false);
        answers.set(new DataOrException<>(Collections.emptyList(), new Exception()));
    }


    @OnCreateChildren
    static Children onCreateChildren(
            SectionContext c,
            @State DataOrException<List<AnswerModel>, Exception> answers, @State boolean isEmpty,
            @Prop HomePostModel post , @Prop EventHandler<OnCodeClickEvent> onCodeClickEventHandler, @State boolean isFetching
    ) {

        Children.Builder builder = new Children.Builder();

        if (post != null)
            builder.child(
                    SingleComponentSection.create(c).component(
                            FullPostTopComponent.create(c)
                                    .onCodeClickEventHandler(onCodeClickEventHandler)
                                    .post(post)
                                    .paddingDip(YogaEdge.HORIZONTAL, 16)
                                    .key("top")
                    )
            );

        builder.child(
                DataDiffSection.<AnswerModel>create(c)
                        .data(answers == null ? Collections.emptyList() : answers.getData())
                        .renderEventHandler(FullPostDiffSection.onRender(c))


        );

        if (answers != null && answers.getData() != null && !answers.getData().isEmpty() && !isEmpty && isFetching)
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
            @FromEvent AnswerModel model, @Prop EventHandler<OnCodeClickEvent> onCodeClickEventHandler) {
        return ComponentRenderInfo.create()
                .component(
                        AnswerComponent.create(c)
                                .answerModel(model)
                                .onCodeClickEventHandler(onCodeClickEventHandler)
                                .paddingDip(YogaEdge.HORIZONTAL, 40)
                                .paddingDip(YogaEdge.TOP, 24)
                                .key(index + "")
                )
                .build();
    }


    @OnCreateService
    static AnswersViewModel onCreateService(
            final SectionContext c,
            @State int start,
            @State int count,
            @Prop AnswersViewModel viewModel,
            @Prop HomePostModel post

    ) {

        if (post != null && post.getPostId() != null) {

            viewModel.loadAnswers(post.getPostId());

        }
        return viewModel;
    }

    @OnBindService
    static void onBindService(final SectionContext c, final AnswersViewModel service

    ) {

        service.registerLoadingEvent(FullPostDiffSection.onDataLoaded(c));

    }

    @OnUnbindService
    static void onUnbindService(final SectionContext c, final AnswersViewModel service) {

        service.unregisterLoadingEvent();


    }


    @OnEvent(AnswersEvent.class)
    static void onDataLoaded(final SectionContext c, @FromEvent DataOrException<List<AnswerModel>, Exception> answers
            , @FromEvent boolean isEmpty) {
        FullPostDiffSection.updateData(c, answers, isEmpty);
        FullPostDiffSection.setFetching(c, false);
        SectionLifecycle.dispatchLoadingEvent(c, false, LoadingEvent.LoadingState.SUCCEEDED, null);
    }


    @OnUpdateState
    static void updateData(
            final StateValue<DataOrException<List<AnswerModel>, Exception>> answers,
            final StateValue<Integer> start,
            final StateValue<Boolean> isEmpty,
            @Param DataOrException<List<AnswerModel>, Exception> newPosts,
            @Param boolean updateEmpty
    ) {

        isEmpty.set(updateEmpty);
        answers.set(newPosts);

    }


//    @OnRefresh
//    static void onRefresh(
//            SectionContext c,
//            final AnswersViewModel service,
//            @State DataOrException<List<AnswersViewModel>, Exception> answers,
//            @State int start,
//            @State int count
//    ) {
//
////        service.refreshData();
//    }


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
            AnswersViewModel service,
            @Prop HomePostModel post,
            @State DataOrException<List<AnswerModel>, Exception> answers,
            @State int start,
            @State int count,
            @State boolean isFetching

    ) {


        if (totalCount != 0 && answers.getData() != null && totalCount == answers.getData().size() && !isFetching) {
            if (post.getPostId() != null) {
                service.loadMoreAnswers(answers.getData().get(totalCount - 1).getDocumentSnapshot(), post.getPostId());
                FullPostDiffSection.setFetching(c, true);

                FullPostDiffSection.updateStartParam(c, answers.getData().size());
            }
        }
//        if(isFetching)
//        HomePostsDiffSectionSection.requestSmoothFocus(c , totalCount + 1 , SmoothScrollAlignmentType.SNAP_TO_CENTER);

    }

}