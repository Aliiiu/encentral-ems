package com.esl.internship.staffsync.commons.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Date;

public class EndDateAfterStartDateValidator implements ConstraintValidator<EndDateAfterStartDate, DateRangeDTO> {

    @Override
    public void initialize(EndDateAfterStartDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(DateRangeDTO dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }

        if (dto.getEndDate().isPresent()) {
            Date startDate = dto.getStartDate();
            Date endDate = dto.getEndDate().get();
            return endDate == null || endDate.after(startDate);
        }
        return true;
    }
}
