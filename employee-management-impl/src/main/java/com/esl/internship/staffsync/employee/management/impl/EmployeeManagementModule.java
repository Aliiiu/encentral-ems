package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.employee.management.api.IDepartmentApi;
import com.esl.internship.staffsync.employee.management.api.IEmployeeApi;
import com.esl.internship.staffsync.employee.management.api.IEmployeeEmergencyContactApi;
import com.google.inject.AbstractModule;

public class EmployeeManagementModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDepartmentApi.class).to(DefaultDepartmentApiImpl.class);
        bind(IEmployeeApi.class).to(DefaultEmployeeApiImpl.class);
        bind(IEmployeeEmergencyContactApi.class).to(DefaultEmployeeEmergencyContactApiImpl.class);
    }
}
