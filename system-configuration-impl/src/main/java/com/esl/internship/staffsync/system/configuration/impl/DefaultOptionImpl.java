package com.esl.internship.staffsync.system.configuration.impl;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.entities.QJpaOption;
import com.esl.internship.staffsync.system.configuration.api.IOption;
import com.esl.internship.staffsync.system.configuration.model.Option;
import com.google.inject.Inject;
import com.querydsl.jpa.impl.JPAQueryFactory;
import play.db.jpa.JPAApi;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.esl.internship.staffsync.commons.util.Utility.stringifyEmployee;
import static com.esl.internship.staffsync.system.configuration.model.SystemConfigurationMapper.INSTANCE;

public class DefaultOptionImpl implements IOption {

    @Inject
    JPAApi jpaApi;

    private static final QJpaOption Q_JPA_OPTION = QJpaOption.jpaOption;

    @Override
    public Option addOption(Option option, Employee employee) {
        final var jpaOption = INSTANCE.mapOption(option);
        jpaOption.setOptionId(UUID.randomUUID().toString());
        jpaOption.setCreatedBy(stringifyEmployee(employee));
        jpaOption.setDateCreated(Timestamp.from(Instant.now()));
        jpaApi.em().persist(jpaOption);
        return INSTANCE.mapOption(jpaOption);
    }

    @Override
    public boolean editOption(final String optionId, final String value, Employee employee) {
        return new JPAQueryFactory(jpaApi.em()).update(Q_JPA_OPTION)
                .set(Q_JPA_OPTION.optionValue, value)
                .set(Q_JPA_OPTION.dateModified, Timestamp.from(Instant.now()))
                .set(Q_JPA_OPTION.modifiedBy, stringifyEmployee(employee, "Updated option value"))
                .where(Q_JPA_OPTION.optionId.eq(optionId))
                .execute() == 1;
    }

    @Override
    public Optional<Option> getOption(final String optionId) {
        final var jpaOption = new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_OPTION)
                .where(Q_JPA_OPTION.optionId.eq(optionId))
                .fetchOne();
        return Optional.ofNullable(INSTANCE.mapOption(jpaOption));
    }

    @Override
    public List<Option> getOptions() {
        return new JPAQueryFactory(jpaApi.em()).selectFrom(Q_JPA_OPTION)
                .fetch()
                .stream()
                .map(INSTANCE::mapOption)
                .collect(Collectors.toList());
    }

    @Override
    public boolean optionBelongsToOptionType(String optionId, String optionTypeId) {
        final var option = getOption(optionId).orElseThrow();
        return option.getOptionType().getOptionTypeId().equals(optionTypeId);
    }
}
