package com.esl.internship.staffsync.authentication.impl;

import com.esl.internship.staffsync.authentication.api.IAuthentication;
import com.google.inject.AbstractModule;

public class AuthenticationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IAuthentication.class).to(DefaultAuthenticationImpl.class);
    }
}