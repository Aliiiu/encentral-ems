package com.esl.internship.staffsync.announcement.management.impl.actor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.esl.internship.staffsync.entities.*;
import com.esl.internship.staffsync.entities.enums.NotificationStatus;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AnnouncementDispatchActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(AnnouncementDispatchActor.AnnouncementDispatchData.class, this::dispatch)
                .build();
    }

    public static Props create() {
        return Props.create(AnnouncementDispatchActor.class);
    }

    public void dispatch(AnnouncementDispatchData announcementDispatchData) {

        Collection<JpaEmployee> recipients;
        if (announcementDispatchData.recipientDepartment == null)
            recipients = new JPAQueryFactory(announcementDispatchData.jpaApi.em())
                    .selectFrom(QJpaEmployee.jpaEmployee)
                    .fetch();
        else
            recipients = announcementDispatchData.recipientDepartment.getEmployees();

        for (JpaEmployee employee : recipients) {
            JpaAnnouncementRecipient recipient = new JpaAnnouncementRecipient();
            recipient.setAnnouncementRecipientId(UUID.randomUUID().toString());
            recipient.setAnnouncement(announcementDispatchData.announcement);
            recipient.setEmployee(employee);
            recipient.setStatus(NotificationStatus.UNREAD);
            recipient.setDateCreated(Timestamp.from(Instant.now()));

            announcementDispatchData.jpaApi.em().persist(recipient);

        }

    }

    public static final class AnnouncementDispatchData {
        private JpaAnnouncement announcement;

        private JpaDepartment recipientDepartment;

        private JPAApi jpaApi;

        public AnnouncementDispatchData(JpaAnnouncement announcement, JpaDepartment recipientDepartment, JPAApi jpaApi) {
            this.announcement = announcement;
            this.recipientDepartment = recipientDepartment;
            this.jpaApi = jpaApi;
        }

        public JpaAnnouncement getAnnouncement() {
            return announcement;
        }

        public void setAnnouncement(JpaAnnouncement announcement) {
            this.announcement = announcement;
        }

        public JpaDepartment getRecipientDepartment() {
            return recipientDepartment;
        }

        public void setRecipientDepartment(JpaDepartment recipientDepartment) {
            this.recipientDepartment = recipientDepartment;
        }

        public JPAApi getJpaApi() {
            return jpaApi;
        }

        public void setJpaApi(JPAApi jpaApi) {
            this.jpaApi = jpaApi;
        }
    }
}




