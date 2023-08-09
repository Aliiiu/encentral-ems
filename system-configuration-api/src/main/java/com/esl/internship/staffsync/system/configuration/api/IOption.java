package com.esl.internship.staffsync.system.configuration.api;

import com.esl.internship.staffsync.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.Option;

import java.util.List;
import java.util.Optional;

public interface IOption {

    Option addOption(Option option, Employee employee);

    boolean editOption(String optionId, String value, Employee employee);

    Optional<Option> getOption(String optionId);

    List<Option> getOptions();

    boolean optionBelongsToOptionType(String optionId, String optionTypeId);
}
