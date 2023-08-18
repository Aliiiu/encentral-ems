package com.esl.internship.staffsync.performance.evaluation.impl;

import java.time.LocalDate;
import java.util.Date;

public class TestMain {
    public static void main (String[] args) {
        Date date = new Date();
        LocalDate ldate = LocalDate.now();




        System.out.println(date);
        System.out.println(date.getTime());

        System.out.println(ldate);

        System.out.println(date.equals(new Date()));
    }
}
