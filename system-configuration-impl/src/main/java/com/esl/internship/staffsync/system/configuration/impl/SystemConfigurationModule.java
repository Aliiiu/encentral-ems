package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.system.configuration.api.IMyApi;
import com.esl.internship.staffsync.system.configuration.api.IPermissionApi;
import com.esl.internship.staffsync.system.configuration.api.IRoleApi;
import com.esl.internship.staffsync.system.configuration.api.IRoleHasPermissionApi;
import com.google.inject.AbstractModule;

public class SystemConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IMyApi.class).to(MyApiImpl.class);
        bind(IRoleApi.class).to(DefaultRoleApiImpl.class);
        bind(IPermissionApi.class).to(DefaultPermissionApiImpl.class);
        bind(IRoleHasPermissionApi.class).to(DefaultRoleHasPermissionApiImpl.class);
    }
}
