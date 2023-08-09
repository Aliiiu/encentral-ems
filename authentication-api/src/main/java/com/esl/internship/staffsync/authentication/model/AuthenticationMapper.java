package com.esl.internship.staffsync.authentication.model;

import com.encentral.staffsync.entity.JpaEmployee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthenticationMapper {

    AuthenticationMapper INSTANCE = Mappers.getMapper(AuthenticationMapper.class);

    @Mappings({
            @Mapping(source="role.roleId", target="roleId")
    })
    EmployeeAuthInfo jpaEmployeeToEmployeeAuthInfo(JpaEmployee jpaEmployee);
}
