package com.esl.internship.staffsync.employee.management.model;

import com.encentral.staffsync.entity.JpaDepartment;
import com.encentral.staffsync.entity.JpaDepartmentHead;
import com.encentral.staffsync.entity.JpaEmergencyContact;
import com.encentral.staffsync.entity.JpaEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeManagementMapper {

    EmployeeManagementMapper INSTANCE = Mappers.getMapper(EmployeeManagementMapper.class);

//    JpaEmployee mapEmployee(Employee model);
//    JpaDepartment mapDepartment(Department model);

//    @Mappings({
//            @Mapping(target = "roleId", source = "role.roleId"),
//            @Mapping(target = "stateOfOrigin", source = "stateOfOrigin.optionValue"),
//            @Mapping(target = "countryOfOrigin", source = "countryOfOrigin.optionValue"),
//            @Mapping(target = "highestCertification", source = "highestCertification.optionValue"),
//            @Mapping(target = "employeeMaritalStatus", source = "employeeMaritalStatus.optionValue"),
//    })
//    Employee mapEmployee(JpaEmployee entity);

//    Department mapDepartment(JpaDepartment entity);
//    DepartmentHead mapDepartmentHead(JpaDepartmentHead entity);
//    EmergencyContact mapEmergencyContact(JpaEmergencyContact entity);
//    EmployeeDocument mapEmployeeDocument(JpaDepartment entity);

}
