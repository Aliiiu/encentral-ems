package com.esl.internship.staffsync.system.configuration.model;

import com.encentral.staffsync.entity.JpaAppConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SystemConfigurationMapper {

    SystemConfigurationMapper INSTANCE = Mappers.getMapper(SystemConfigurationMapper.class);

    JpaAppConfig mapAppConfig(AppConfig model);

    AppConfig mapAppConfig(JpaAppConfig entity);
}
