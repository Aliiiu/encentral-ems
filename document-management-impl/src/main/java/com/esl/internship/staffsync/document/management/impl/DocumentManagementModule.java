package com.esl.internship.staffsync.document.management.impl;

import com.esl.internship.staffsync.document.management.api.IDocumentManagementApi;
import com.google.inject.AbstractModule;

public class DocumentManagementModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IDocumentManagementApi.class).to(DefaultDocumentManagementImpl.class);
    }
}
