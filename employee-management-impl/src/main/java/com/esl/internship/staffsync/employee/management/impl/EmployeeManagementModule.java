package com.esl.internship.staffsync.employee.management.impl;

import com.esl.internship.staffsync.employee.management.api.*;
import com.google.inject.AbstractModule;

public class EmployeeManagementModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDepartmentApi.class).to(DefaultDepartmentApiImpl.class);
        bind(IEmployeeApi.class).to(DefaultEmployeeApiImpl.class);
        bind(IDepartmentHeadApi.class).to(DefaultDepartmentHeadApiImpl.class);
        bind(IEmployeeEmergencyContactApi.class).to(DefaultEmployeeEmergencyContactApiImpl.class);
        bind(IPasswordManagementApi.class).to(PasswordManagementApiBcryptImpl.class);
    }
}
