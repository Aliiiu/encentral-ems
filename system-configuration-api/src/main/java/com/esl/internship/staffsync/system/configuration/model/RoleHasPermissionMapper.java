package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaRoleHasPermission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleHasPermissionMapper {
    RoleHasPermissionMapper INSTANCE = Mappers.getMapper(RoleHasPermissionMapper.class);

    JpaRoleHasPermission mapRoleHasPermission(RoleHasPermission model);

    RoleHasPermission mapRoleHasPermission(JpaRoleHasPermission entity);
}
