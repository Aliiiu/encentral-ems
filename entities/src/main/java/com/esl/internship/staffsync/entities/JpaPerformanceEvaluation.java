package com.esl.internship.staffsync.entities;

import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="performance_evaluation")
public class JpaPerformanceEvaluation {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="performance_evaluation_id", unique=true, nullable=false, length=64)
    private String performanceEvaluationId;

    @Column(name = "attendance_accuracy", nullable = false)
    private Double attendanceAccuracy;

    @Column(name = "leave_performance", nullable = false)
    private Double leavePerformance;

    @Column(name="feedback", length=2147483647)
    private String feedback;

    @Temporal(TemporalType.DATE)
    @Column(name = "evaluation_start_date", nullable = false)
    private Date evaluationStartDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "evaluation_end_date", nullable = false)
    private Date evaluationEndDate;

    @Column(name="date_created", nullable=false)
    private Timestamp dateCreated;

    //bidirectional many-to-one association to JpaEmployee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="employee_id", nullable=false)
    private JpaEmployee employee;

    //bidirectional many-to-one association to JpaEmployee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="evaluator_id")
    private JpaEmployee evaluator;


    public String getPerformanceEvaluationId() {
        return performanceEvaluationId;
    }

    public void setPerformanceEvaluationId(String performanceEvaluationId) {
        this.performanceEvaluationId = performanceEvaluationId;
    }

    public Double getAttendanceAccuracy() {
        return attendanceAccuracy;
    }

    public void setAttendanceAccuracy(Double attendanceAccuracy) {
        this.attendanceAccuracy = attendanceAccuracy;
    }

    public Double getLeavePerformance() {
        return leavePerformance;
    }

    public void setLeavePerformance(Double leavePerformance) {
        this.leavePerformance = leavePerformance;
    }

    public Date getEvaluationStartDate() {
        return evaluationStartDate;
    }

    public void setEvaluationStartDate(Date evaluationStartDate) {
        this.evaluationStartDate = evaluationStartDate;
    }

    public Date getEvaluationEndDate() {
        return evaluationEndDate;
    }

    public void setEvaluationEndDate(Date evaluationEndDate) {
        this.evaluationEndDate = evaluationEndDate;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public JpaEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(JpaEmployee employee) {
        this.employee = employee;
    }

    public JpaEmployee getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(JpaEmployee evaluator) {
        this.evaluator = evaluator;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("performanceEvaluationId", performanceEvaluationId)
                .add("attendanceAccuracy", attendanceAccuracy)
                .add("leavePerformance", leavePerformance)
                .add("evaluationStartDate", evaluationStartDate)
                .add("evaluationEndDate", evaluationEndDate)
                .add("employee", employee)
                .toString();
    }
}

