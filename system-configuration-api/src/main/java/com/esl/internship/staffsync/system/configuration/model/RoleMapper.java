package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    JpaRole mapRole(Role model);

    Role mapRole(JpaRole entity);
}
