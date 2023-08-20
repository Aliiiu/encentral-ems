package com.esl.internship.staffsync.performance.evaluation.impl;

import com.esl.internship.staffsync.performance.evaluation.api.IPerformanceEvaluationApi;
import com.google.inject.AbstractModule;

public class PerformanceEvaluationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IPerformanceEvaluationApi.class).to(DefaultPerformanceEvaluationApiImpl.class);
    }
}
