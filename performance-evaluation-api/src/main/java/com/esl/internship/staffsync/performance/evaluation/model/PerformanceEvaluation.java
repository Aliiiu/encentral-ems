package com.esl.internship.staffsync.performance.evaluation.model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class PerformanceEvaluation {

    private String performanceEvaluationId;

    private String employeeId;

    private String firstName;

    private String lastName;

    private String email;

    private double attendanceAccuracy;

    private double leavePerformance;

    private String feedback;

    private String evaluator;

    private LocalDate evaluationStartDate;

    private LocalDate evaluationEndDate;

    private Timestamp dateCreated;

    public String getPerformanceEvaluationId() {
        return performanceEvaluationId;
    }

    public void setPerformanceEvaluationId(String performanceEvaluationId) {
        this.performanceEvaluationId = performanceEvaluationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getAttendanceAccuracy() {
        return attendanceAccuracy;
    }

    public void setAttendanceAccuracy(double attendanceAccuracy) {
        this.attendanceAccuracy = attendanceAccuracy;
    }

    public double getLeavePerformance() {
        return leavePerformance;
    }

    public void setLeavePerformance(double leavePerformance) {
        this.leavePerformance = leavePerformance;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }

    public LocalDate getEvaluationStartDate() {
        return evaluationStartDate;
    }

    public void setEvaluationStartDate(LocalDate evaluationStartDate) {
        this.evaluationStartDate = evaluationStartDate;
    }

    public LocalDate getEvaluationEndDate() {
        return evaluationEndDate;
    }

    public void setEvaluationEndDate(LocalDate evaluationEndDate) {
        this.evaluationEndDate = evaluationEndDate;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }
}
