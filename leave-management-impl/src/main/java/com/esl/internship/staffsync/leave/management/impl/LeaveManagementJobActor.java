package com.esl.internship.staffsync.leave.management.impl;

import akka.actor.ActorSystem;
import com.esl.internship.staffsync.leave.management.api.ILeaveRequest;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class LeaveManagementJobActor {

    private final ActorSystem actorSystem;

    private final ExecutionContext executionContext;

    @Inject
    ILeaveRequest iLeaveRequest;

    @Inject
    JPAApi jpaApi;

    @Inject
    public LeaveManagementJobActor(ActorSystem actorSystem, ExecutionContext executionContext) {
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        this.initialize();
    }

    private void initialize() {
        this.actorSystem
                .scheduler()
                .schedule(
                        Duration.create(0, TimeUnit.SECONDS), // initialDelay
                        Duration.create(24, TimeUnit.HOURS), // interval
                        this::callMethod,
                        this.executionContext);
    }

    private void callMethod() {
        jpaApi.withTransaction(()->{
            iLeaveRequest.closeCompletedLeave();
        });
    }
}