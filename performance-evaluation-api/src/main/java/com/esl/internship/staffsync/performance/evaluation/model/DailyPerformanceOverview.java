package com.esl.internship.staffsync.performance.evaluation.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DailyPerformanceOverview {

    private double totalHoursWorked;

    private LocalDate day;

    private LocalTime firstCheckInTime;

    private LocalTime lastCheckOutTime;

    private int totalCheckIns;

    private int totalCheckOuts;

    private double performanceRating;

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(double totalHoursWorked) {
        this.totalHoursWorked = totalHoursWorked;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public LocalTime getFirstCheckInTime() {
        return firstCheckInTime;
    }

    public void setFirstCheckInTime(LocalTime firstCheckInTime) {
        this.firstCheckInTime = firstCheckInTime;
    }

    public LocalTime getLastCheckOutTime() {
        return lastCheckOutTime;
    }

    public void setLastCheckOutTime(LocalTime lastCheckOutTime) {
        this.lastCheckOutTime = lastCheckOutTime;
    }

    public int getTotalCheckIns() {
        return totalCheckIns;
    }

    public void setTotalCheckIns(int totalCheckIns) {
        this.totalCheckIns = totalCheckIns;
    }

    public int getTotalCheckOuts() {
        return totalCheckOuts;
    }

    public void setTotalCheckOuts(int totalCheckOuts) {
        this.totalCheckOuts = totalCheckOuts;
    }

    public double getPerformanceRating() {
        return performanceRating;
    }

    public void setPerformanceRating(double performanceRating) {
        this.performanceRating = performanceRating;
    }
}
