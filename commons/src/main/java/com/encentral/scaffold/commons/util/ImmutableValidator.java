package com.encentral.scaffold.commons.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.bval.jsr.ApacheValidationProvider;
import org.slf4j.Logger;
import play.libs.Json;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static play.libs.Json.toJson;

public class ImmutableValidator {

    private static final Validator VALIDATOR = Validation.byProvider(ApacheValidationProvider.class)
            .configure().buildValidatorFactory().getValidator();

    private static final Logger LOGGER = getLogger(ImmutableValidator.class);

    private ImmutableValidator() {

    }

    public static <T> Form<T> validate(byte[] json, Class<T> aClass) {
        try {
            final var value = Json.mapper().readValue(json, aClass);
            return gettForm(value);
        } catch (Exception e) {
            LOGGER.error("Failed to validate class {}", aClass, e);
            return new Form(null, true, toJson(e.getMessage()));
        }

    }

    public static <T> Form<T> validate(JsonNode json, Class<T> aClass) {
        try {
            final var value = Json.mapper().treeToValue(json, aClass);
            return gettForm(value);
        } catch (Exception e) {
            LOGGER.error("Failed to validate {} to class {}", new Object[]{json, aClass, e});
            return new Form(null, true, toJson(e.getMessage()));
        }

    }

    private static <T> Form<T> gettForm(T value) {
        final var validate = VALIDATOR.validate(value);
        if (validate.isEmpty()) {
            return new Form<>(value, false, null);
        }
        return new Form<>(
                null,
                true,
                toJson(validate
                        .stream()
                        .collect(toMap(ConstraintViolation::getPropertyPath, ConstraintViolation::getMessage)))
        );
    }

    public static class Form<T> {
        public final T value;
        public final boolean hasError;
        public final JsonNode error;

        private Form(T value, boolean hasError, JsonNode error) {
            this.value = !hasError ? Objects.requireNonNull(value) : null;
            this.hasError = hasError;
            this.error = error;
        }
    }
}
