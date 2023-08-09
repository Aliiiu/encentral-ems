package com.esl.internship.staffsync.system.configuration.api;

import com.encentral.scaffold.commons.model.Employee;
import com.esl.internship.staffsync.system.configuration.model.Option;
import com.esl.internship.staffsync.system.configuration.model.OptionType;

import java.util.List;
import java.util.Optional;

public interface IOptionType {

    boolean editOptionType(String optionTypeId, OptionType optionType, Employee employee);

    Optional<OptionType> getOptionType(String optionTypeId);

    List<OptionType> getOptionTypes();

    List<Option> getOptionsForType(String optionTypeId);
}
