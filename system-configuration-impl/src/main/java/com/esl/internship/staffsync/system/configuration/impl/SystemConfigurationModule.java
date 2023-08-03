package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.system.configuration.api.IMyApi;
import com.google.inject.AbstractModule;

public class SystemConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IMyApi.class).to(MyApiImpl.class);
    }
}
