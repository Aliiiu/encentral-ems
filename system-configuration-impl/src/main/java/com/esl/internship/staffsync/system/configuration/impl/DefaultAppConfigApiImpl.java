package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.QJpaAppConfig;
import com.esl.internship.staffsync.system.configuration.api.IAppConfigApi;
import com.esl.internship.staffsync.system.configuration.model.AppConfig;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import javax.inject.Inject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

/**
 * @author YAKUBU
 * @dateCreated 03/07/2023
 * @description General implementation of IMyApi
 */
public class DefaultAppConfigApiImpl implements IAppConfigApi {

    @Inject
    JPAApi jpaApi;

    private static final QJpaAppConfig qJpaAppConfig = QJpaAppConfig.jpaAppConfig;

    /**
     * @author YAKUBU
     * @dateCreated 03/08/2023
     * @description Creates new app config
     *
     * @param appConfig The app config data to create
     * @param employee The employee creating this record
     *
     * @return AppConfig
     */
    @Override
    public AppConfig addAppConfig(AppConfig appConfig, Employee employee) {
        final var jpaAppConfig = INSTANCE.mapAppConfig(appConfig);
        jpaAppConfig.setAppConfigId(UUID.randomUUID().toString());
        jpaAppConfig.setCreatedBy(stringifyEmployee(employee));
        jpaAppConfig.setDateCreated(Timestamp.from(Instant.now()));
        jpaApi.em().persist(jpaAppConfig);
        return INSTANCE.mapAppConfig(jpaAppConfig);
    }

    /**
     * @author YAKUBU
     * @dateCreated 03/08/2023
     * @description Creates new app config
     *
     * @param appConfigId The id of the app config to update
     * @param appConfig The app config data to update from
     * @param employee The employee updating this record
     *
     * @return AppConfig
     *
     * @lastModifiedBy Yakubu
     * @lastDateModified 05/07/2023
     * @lastModificationReason Added remark
     */
    @Override
    public boolean updateAppConfig(String appConfigId, AppConfig appConfig, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(qJpaAppConfig)
                .set(qJpaAppConfig.configurationValue, appConfig.getConfigurationValue())
                .set(qJpaAppConfig.dateModified, Timestamp.from(Instant.now()))
                .set(qJpaAppConfig.modifiedBy, stringifyEmployee(employee, "Updated config value"))
                .where(qJpaAppConfig.appConfigId.eq(appConfigId))
                .execute() == 1;
    }

    @Override
    public Optional<AppConfig> getAppConfig(String appConfigId) {
        final var jpaAppConfig = new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAppConfig)
                .where(qJpaAppConfig.appConfigId.eq(appConfigId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapAppConfig(jpaAppConfig));
    }

    @Override
    public List<AppConfig> getAppConfigs() {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(qJpaAppConfig)
                .fetch()
                .stream()
                .map(INSTANCE::mapAppConfig)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteAppConfig(String appConfigId) {
        return new JPAQueryFactory(jpaApi.em()).delete(qJpaAppConfig)
                .where(qJpaAppConfig.appConfigId.eq(appConfigId))
                .execute() == 1;
    }
}
