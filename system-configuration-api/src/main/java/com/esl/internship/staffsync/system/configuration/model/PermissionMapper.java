package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaPermission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    JpaPermission mapPermission(Permission model);

    Permission mapPermission(JpaPermission entity);
}
