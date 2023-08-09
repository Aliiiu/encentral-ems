/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.esl.internship.staffsync.binder;

import com.esl.internship.staffsync.leave.management.impl.LeaveManagementModule;
import com.esl.internship.staffsync.event.management.impl.EventManagementModule;
import com.esl.internship.staffsync.system.configuration.impl.SystemConfigurationModule;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;


/**
 * @author poseidon
 */
public class Module extends AbstractModule implements AkkaGuiceSupport {


    @Override
    protected void configure() {

        super.configure();

        bind(BigBang.class).asEagerSingleton();

        install(new SystemConfigurationModule());

        install(new LeaveManagementModule());

        install(new EventManagementModule());

    }
}
