package ru.fenix2k.Chatter.server.Exception;

import ru.fenix2k.Chatter.server.View.ValidationErrorView;

import java.util.Set;

public class EntityValidationException extends RuntimeException {

    private Set<ValidationErrorView> validationErrors;

    public EntityValidationException(String message, Set<ValidationErrorView> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public Set<ValidationErrorView> getValidationErrors() {
        return validationErrors;
    }

}
