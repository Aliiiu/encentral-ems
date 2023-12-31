package com.esl.internship.staffsync.authentication.impl;

import akka.actor.ActorSystem;
import com.esl.internship.staffsync.authentication.api.IAuthentication;
import play.db.jpa.JPAApi;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import java.util.concurrent.TimeUnit;

public class AuthJobActor {

    private final ActorSystem actorSystem;

    private final ExecutionContext executionContext;

    @Inject
    IAuthentication iAuthentication;

    @Inject
    JPAApi jpaApi;

    @Inject
    public AuthJobActor(ActorSystem actorSystem, ExecutionContext executionContext) {
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
            iAuthentication.resetLogInAttempts();
        });
    }
}
