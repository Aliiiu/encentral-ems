package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.QJpaOptionType;
import com.esl.internship.staffsync.system.configuration.api.IOptionType;
import com.esl.internship.staffsync.system.configuration.model.Option;
import com.esl.internship.staffsync.system.configuration.model.OptionType;
import com.google.inject.Inject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

public class DefaultOptionTypeImpl implements IOptionType {

    @Inject
    JPAApi jpaApi;

    private static final QJpaOptionType Q_JPA_OPTION_TYPE = QJpaOptionType.jpaOptionType;

    @Override
    public boolean editOptionType(final String optionId, final OptionType optionType, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(Q_JPA_OPTION_TYPE)
                .set(Q_JPA_OPTION_TYPE.optionTypeName, optionType.getOptionTypeName())
                .set(Q_JPA_OPTION_TYPE.optionTypeDescription, optionType.getOptionTypeDescription())
                .set(Q_JPA_OPTION_TYPE.dateModified, Timestamp.from(Instant.now()))
                .set(Q_JPA_OPTION_TYPE.modifiedBy, stringifyEmployee(employee, "Updated optionType value"))
                .where(Q_JPA_OPTION_TYPE.optionTypeId.eq(optionId))
                .execute() == 1;
    }

    @Override
    public Optional<OptionType> getOptionType(final String optionId) {
        final var jpaOptionType = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_OPTION_TYPE)
                .where(Q_JPA_OPTION_TYPE.optionTypeId.eq(optionId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapOptionType(jpaOptionType));
    }

    @Override
    public List<OptionType> getOptionTypes() {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_OPTION_TYPE)
                .fetch()
                .stream()
                .map(INSTANCE::mapOptionType)
                .collect(Collectors.toList());
    }

    @Override
    public List<Option> getOptionsForType(String optionTypeId) {
        final var jpaOptionType = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_OPTION_TYPE)
                .where(Q_JPA_OPTION_TYPE.optionTypeId.eq(optionTypeId))
                .fetchOne();

        if (jpaOptionType == null) {
            return List.of();
        }

        return new ArrayList<>(jpaOptionType.getOptions())
                .stream()
                .map(INSTANCE::mapOption)
                .collect(Collectors.toList());
    }
}
