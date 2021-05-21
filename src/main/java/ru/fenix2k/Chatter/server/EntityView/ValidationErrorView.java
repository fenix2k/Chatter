package ru.fenix2k.Chatter.server.EntityView;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class ValidationErrorView implements Serializable {
    private String field;
    private String message;
}
