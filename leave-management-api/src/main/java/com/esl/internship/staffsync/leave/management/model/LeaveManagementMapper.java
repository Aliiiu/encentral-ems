package com.esl.internship.staffsync.leave.management.model;

import com.encentral.staffsync.entity.JpaEmployee;
import com.encentral.staffsync.entity.JpaLeaveRequest;
import com.encentral.staffsync.entity.JpaOption;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LeaveManagementMapper {

    LeaveManagementMapper INSTANCE = Mappers.getMapper(LeaveManagementMapper.class);


    JpaLeaveRequest leaveRequestToJpaLeaveRequest(LeaveRequest leaveRequest);

    LeaveRequest jpaLeaveRequestToLeaveRequest(JpaLeaveRequest jpaLeaveRequest);

    LeaveRequestEmployee jpaEmployeeToLeaveRequestEmployee(JpaEmployee jpaEmployee);

    JpaEmployee leaveRequestEmployeeToJpaEmployee(LeaveRequestEmployee leaveRequestEmployee);

    LeaveOptionType jpaOptionToLeaveOptionType(JpaOption jpaOption);

    JpaOption leaveOptionTypeToJpaOption(LeaveOptionType leaveOptionType);

    @Mappings({
            @Mapping(target="employee.employeeId", source ="employeeId"),
            @Mapping(target="leaveType.optionId", source = "leaveTypeId")
    })
    LeaveRequest creationDtoToLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO);

}
