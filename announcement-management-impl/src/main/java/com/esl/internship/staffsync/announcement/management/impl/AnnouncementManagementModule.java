package com.esl.internship.staffsync.announcement.management.impl;

import com.esl.internship.staffsync.announcement.management.api.IAnnouncementManagementApi;
import com.google.inject.AbstractModule;

public class AnnouncementManagementModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAnnouncementManagementApi.class).to(DefaultAnnouncementManagementApiImpl.class);
    }
}
