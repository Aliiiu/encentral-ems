package com.esl.internship.staffsync.employee.management.model;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.document.management.dto.DocumentDTO;
import com.esl.internship.staffsync.document.management.dto.DocumentUpdateDTO;
import com.esl.internship.staffsync.document.management.model.Document;
import com.esl.internship.staffsync.employee.management.dto.*;
import com.esl.internship.staffsync.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeManagementMapper {

    EmployeeManagementMapper INSTANCE = Mappers.getMapper(EmployeeManagementMapper.class);

    @Mappings({
            @Mapping(source = "roleId", target = "role.roleId"),
            @Mapping(source = "departmentId", target = "department.departmentId"),
            @Mapping(source = "employeeGenderOptionId", target = "employeeGender.optionValue"),
            @Mapping(source = "stateOfOriginOptionId", target = "stateOfOrigin.optionValue"),
            @Mapping(source = "countryOfOriginOptionId", target = "countryOfOrigin.optionValue"),
            @Mapping(source = "highestCertificationOptionId", target = "highestCertification.optionValue"),
            @Mapping(source = "stateOfOriginOptionId", target = "employeeMaritalStatus.optionValue"),
    })
    JpaEmployee mapEmployee(EmployeeDTO modelDto);

    JpaDepartment mapDepartment(DepartmentDTO modelDto);

    @Mappings({
            @Mapping(source = "employeeId", target = "employee.employeeId"),
            @Mapping(source = "contactGenderOptionId", target = "contactGender.optionValue")
    })
    JpaEmergencyContact mapEmergencyContact(EmergencyContactDTO entity);

    @Mappings({
            @Mapping(target = "roleId", source = "role.roleId"),
            @Mapping(target = "profilePictureId", source = "profilePictureUrl"),
            @Mapping(target = "departmentId", source = "department.departmentId"),
            @Mapping(target = "employeeGender", source = "employeeGender.optionValue"),
            @Mapping(target = "stateOfOrigin", source = "stateOfOrigin.optionValue"),
            @Mapping(target = "countryOfOrigin", source = "countryOfOrigin.optionValue"),
            @Mapping(target = "highestCertification", source = "highestCertification.optionValue"),
            @Mapping(target = "employeeMaritalStatus", source = "employeeMaritalStatus.optionValue"),
    })
    Employee mapEmployee(JpaEmployee entity);

    @Mappings({
            @Mapping(target = "departmentHeadEmployeeId", source = "departmentHead.employee.employeeId")
    })
    Department mapDepartment(JpaDepartment entity);

    DepartmentHead mapDepartmentHead(JpaDepartmentHead entity);


    @Mappings({
            @Mapping(target = "employeeId", source = "employee.employeeId"),
            @Mapping(target = "contactGender", source = "contactGender.optionValue")
    })
    EmergencyContact mapEmergencyContact(JpaEmergencyContact entity);

    @Mappings({
            @Mapping(target = "employeeId", source = "employee.employeeId"),
            @Mapping(target = "documentType", source = "documentType.optionValue"),
            @Mapping(target = "documentId", source = "document.documentId"),
            @Mapping(target = "documentName", source = "document.documentName"),
            @Mapping(target = "documentDescription", source = "document.documentDescription")
    })
    EmployeeDocument mapEmployeeDocument(JpaEmployeeHasDocument entity);

    DocumentDTO mapDocumentDTO(EmployeeDocumentDTO modelDTO);
    DocumentUpdateDTO mapDocumentUpdateDTO(EmployeeDocumentDTO modelDTO);

    @Mappings({
            @Mapping(target = "approverEmployeeId", source = "approver.employeeId"),
            @Mapping(target = "employeeId", source = "employee.employeeId")
    })
    EmployeeUpdateRequest mapEmployeeUpdateRequest(JpaEmployeeUpdateRequest entity);

    JpaEmployeeUpdateRequest mapEmployeeUpdateRequestDTO(EmployeeUpdateRequestDTO modelDTO);

}
