package com.esl.internship.staffsync.system.configuration.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.AppConfig;

import java.util.List;
import java.util.Optional;

public interface IAppConfigApi {

    AppConfig addAppConfig(AppConfig appConfig, Employee employee);

    boolean updateAppConfig(String appConfigId, AppConfig appConfig, Employee employee);

    Optional<AppConfig> getAppConfig(String appConfigId);

    List<AppConfig> getAppConfigs();

    boolean deleteAppConfig(String appConfigId);

}
