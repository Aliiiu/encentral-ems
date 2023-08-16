package com.esl.internship.staffsync.attendance.tracking.model;

import com.esl.internship.staffsync.entities.JpaAttendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;


@Mapper
public interface AttendanceMapper {
    AttendanceMapper INSTANCE = Mappers.getMapper(AttendanceMapper.class);

    @Mappings({
            @Mapping(source = "employee.employeeId", target = "employeeId"),
            @Mapping(source = "employee.firstName", target = "firstName"),
            @Mapping(source = "employee.lastName", target = "lastName"),
            @Mapping(source = "employee.department.departmentName", target = "departmentName")
    })
    Attendance jpaAttendanceToAttendance(JpaAttendance jpaAttendance);
}