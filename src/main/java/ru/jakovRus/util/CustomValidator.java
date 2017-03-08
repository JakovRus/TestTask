package ru.jakovRus.util;

import com.vaadin.data.Validator;

/**
 * @author Jakov Rogov
 */
public class CustomValidator implements Validator {
    private String errorMessage;
    private int maxLength;

    public CustomValidator(String errorMessage, int maxLength) {
        this.errorMessage = errorMessage;
        this.maxLength = maxLength;
    }

    @Override
    public void validate(Object value) throws InvalidValueException {
        if (value instanceof String) {
            String strValue = (String) value;
            boolean empty = strValue.trim().isEmpty();
            boolean overflow = strValue.length() > maxLength;
            if (empty || overflow) {
                throw new InvalidValueException(errorMessage);
            }
        } else {
            throw new InvalidValueException(errorMessage);
        }
    }
}
