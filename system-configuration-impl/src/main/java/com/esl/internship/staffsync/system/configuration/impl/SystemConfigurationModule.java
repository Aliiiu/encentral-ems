package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.system.configuration.api.*;
import com.google.inject.AbstractModule;

public class SystemConfigurationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IAppConfigApi.class).to(DefaultAppConfigApiImpl.class);
        bind(INotification.class).to(DefaultNotificationImpl.class);
        bind(INotificationTemplate.class).to(DefaultNotificationTemplateImpl.class);
        bind(IRoleApi.class).to(DefaultRoleApiImpl.class);
        bind(IPermissionApi.class).to(DefaultPermissionApiImpl.class);
        bind(IRoleHasPermissionApi.class).to(DefaultRoleHasPermissionApiImpl.class);
        bind(IOption.class).to(DefaultOptionImpl.class);
        bind(IOptionType.class).to(DefaultOptionTypeImpl.class);
    }
}
