package com.esl.internship.staffsync.performance.evaluation.model;

import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.JpaPerformanceEvaluation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

@Mapper
public interface PerformanceEvaluationMapper {
    PerformanceEvaluationMapper INSTANCE = Mappers.getMapper(PerformanceEvaluationMapper.class);

    @Mappings({
            @Mapping(target = "employeeId", source = "employee.employeeId"),
            @Mapping(target = "firstName", source = "employee.firstName"),
            @Mapping(target = "lastName", source = "employee.lastName"),
            @Mapping(target = "email", source = "employee.employeeEmail"),
            @Mapping(target = "evaluator", source = "evaluator")
    })
    PerformanceEvaluation mapPerformanceEvaluation(JpaPerformanceEvaluation entity);
}