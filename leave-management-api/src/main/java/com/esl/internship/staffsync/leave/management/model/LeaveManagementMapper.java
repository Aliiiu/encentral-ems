package com.esl.internship.staffsync.leave.management.model;

import com.encentral.staffsync.entity.JpaEmployee;
import com.encentral.staffsync.entity.JpaLeaveRequest;
import com.encentral.staffsync.entity.JpaOption;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.Optional;

@Mapper
public interface LeaveManagementMapper {

    LeaveManagementMapper INSTANCE = Mappers.getMapper(LeaveManagementMapper.class);


    JpaLeaveRequest leaveRequestToJpaLeaveRequest(LeaveRequest leaveRequest);

    LeaveRequest jpaLeaveRequestToLeaveRequest(JpaLeaveRequest jpaLeaveRequest);

    LeaveRequestEmployee jpaEmployeeToLeaveRequestEmployee(JpaEmployee jpaEmployee);

    JpaEmployee leaveRequestEmployeeToJpaEmployee(LeaveRequestEmployee leaveRequestEmployee);

    LeaveOptionType jpaOptionToLeaveOptionType(JpaOption jpaOption);

    JpaOption leaveOptionTypeToJpaOption(LeaveOptionType leaveOptionType);

    default LeaveRequest creationDtoToLeaveRequest(CreateLeaveRequestDTO createLeaveRequestDTO){
        if ( createLeaveRequestDTO == null ) {
            return null;
        }

        LeaveRequestEmployee lre =  new LeaveRequestEmployee();
        lre.setEmployeeId(createLeaveRequestDTO.getEmployeeId());

        LeaveOptionType leaveOptionType = new LeaveOptionType();
        leaveOptionType.setOptionId(createLeaveRequestDTO.getLeaveTypeId());

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setEmployee(lre);
        leaveRequest.setLeaveType(leaveOptionType);
        leaveRequest.setReason( createLeaveRequestDTO.getReason() );
        leaveRequest.setStartDate( createLeaveRequestDTO.getStartDate() );
        createLeaveRequestDTO.getEndDate().ifPresent((endDate)->{
            leaveRequest.setEndDate(endDate);
        });

        return leaveRequest;
    }
}
