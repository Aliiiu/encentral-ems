package com.esl.internship.staffsync.leave.management.model;

import com.esl.internship.staffsync.entities.JpaEmployee;
import com.esl.internship.staffsync.entities.JpaLeaveRequest;
import com.esl.internship.staffsync.entities.JpaOption;
import com.esl.internship.staffsync.leave.management.dto.CreateLeaveRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.Map;

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

    default Map<String, Integer> getDurationAndEmployeeId(JpaLeaveRequest jpaLeaveRequest){
        Map<String, Integer> durationIdMap = new HashMap<>();
        durationIdMap.putIfAbsent(jpaLeaveRequest.getEmployee().getEmployeeId(), jpaLeaveRequest.getDuration());
        return durationIdMap;
    }
}
