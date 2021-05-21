package ru.fenix2k.Chatter.server.Utils;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;

public class BeanUtil {
    private static ValidatorFactory validatorFactory;

    public static ValidatorFactory getValidatorFactory() {
        if(validatorFactory == null)
            return Validation.buildDefaultValidatorFactory();
        return validatorFactory;
    }

}
