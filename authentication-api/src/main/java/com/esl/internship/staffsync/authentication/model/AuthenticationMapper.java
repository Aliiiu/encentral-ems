package com.esl.internship.staffsync.authentication.model;

import com.esl.internship.staffsync.entities.JpaEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    EmployeeAuthInfo authEmployeeSliceToEmployeeAuthInfo(AuthEmployeeSlice authEmployeeSlice);

    @Mappings({
            @Mapping(source="role.roleId", target="roleId")
    })
    AuthEmployeeSlice jpaEmployeeToAuthEmployeeSlice(JpaEmployee jpaEmployee);
}
