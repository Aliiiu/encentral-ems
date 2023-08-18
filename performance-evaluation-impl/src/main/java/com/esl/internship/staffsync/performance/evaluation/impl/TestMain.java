package com.esl.internship.staffsync.performance.evaluation.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TestMain {
    public static void main (String[] args) {
        Date date = new Date();
        LocalDate ldate = LocalDate.now();


        Set<LocalDate> dateSet = new HashSet<>();

        LocalDate ldateA = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Date date2 = new Date();
        LocalDate ldateB = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        dateSet.add(ldate);
        dateSet.add(ldateA);
        dateSet.add(ldateB);

        System.out.println(dateSet);

        System.out.println(LocalTime.now());
        System.out.println(Timestamp.from(Instant.now()));
    }
}
