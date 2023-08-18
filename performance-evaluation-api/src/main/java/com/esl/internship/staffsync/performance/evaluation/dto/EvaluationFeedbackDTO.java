package com.esl.internship.staffsync.performance.evaluation.dto;

import javax.validation.constraints.NotNull;

public class EvaluationFeedbackDTO {

    @NotNull(message = "Evaluation feedback required")
    private String feedback;

    private String performanceEvaluationId;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getPerformanceEvaluationId() {
        return performanceEvaluationId;
    }

    public void setPerformanceEvaluationId(String performanceEvaluationId) {
        this.performanceEvaluationId = performanceEvaluationId;
    }

}
