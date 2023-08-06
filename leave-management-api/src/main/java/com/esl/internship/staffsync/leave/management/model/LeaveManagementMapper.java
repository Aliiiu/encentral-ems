package com.esl.internship.staffsync.leave.management.model;

import com.encentral.staffsync.entity.JpaLeaveRequest;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveManagementMapper {

    LeaveManagementMapper INSTANCE = Mappers.getMapper(LeaveManagementMapper.class);

    @Mappings({

            @Mapping(target = "employee.employeeId", source = "employeeId"),
            @Mapping(target = "approver.employeeId", source = "approverId"),
            @Mapping(target = "leaveType.optionId", source = "leaveTypeId")
    })
    JpaLeaveRequest leaveRequestToJpaLeaveRequest(LeaveRequest leaveRequest);

    @Mappings({

            @Mapping(target = "employeeId", source = "employee.employeeId"),
            @Mapping(target = "approverId", source = "approver.employeeId"),
            @Mapping(target = "leaveTypeId", source = "leaveType.optionId")
    })
    LeaveRequest jpaLeaveRequestToLeaveRequest(JpaLeaveRequest jpaLeaveRequest);

    LeaveRequest dtoToLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO);
}
