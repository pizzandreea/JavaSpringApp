package com.soupapp.soup.utils;
import jakarta.validation.ConstraintViolation;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    public static <T> Map<String, String> getErrors(Set<ConstraintViolation<T>> validationResult) {
//        path pana la proprietatea care nu a respectat constraint-ul
//        .getInvalidValue - valoarea care nu a respectat validarea
        return validationResult.stream()
                .collect(Collectors.toMap(x -> x.getPropertyPath().toString(),
                        x -> x.getMessage()));
    }

}
