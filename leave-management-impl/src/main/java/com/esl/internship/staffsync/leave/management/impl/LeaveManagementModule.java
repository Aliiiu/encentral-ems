package com.esl.internship.staffsync.leave.management.impl;

import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import com.google.inject.AbstractModule;

public class LeaveManagementModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ILeaveRequest.class).to(DefaultLeaveRequestImpl.class);
    }
}