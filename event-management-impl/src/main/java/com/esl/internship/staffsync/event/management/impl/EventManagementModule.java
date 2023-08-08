package com.esl.internship.staffsync.event.management.impl;

import com.esl.internship.staffsync.event.management.api.IEventApi;
import com.google.inject.AbstractModule;

public class EventManagementModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IEventApi.class).to(DefaultEventApiImpl.class);
    }
}
