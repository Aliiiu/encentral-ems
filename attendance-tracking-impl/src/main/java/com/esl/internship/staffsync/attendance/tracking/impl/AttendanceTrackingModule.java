package com.esl.internship.staffsync.attendance.tracking.impl;

import com.esl.internship.staffsync.attendance.tracking.api.IAttendanceTracking;
import com.google.inject.AbstractModule;

public class AttendanceTrackingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IAttendanceTracking.class).to(DefaultAttendanceTrackingImpl.class);
    }
}
