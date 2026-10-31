package com.example.minpix.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum StatusPixEnum {
    PENDENTE,
    AGENDADA,
    CONCLUIDA,
    CANCELADA,
    GERADO,
    ATUALIZADO,
    EXPIRADO,
    FALHA;

    @JsonCreator
    public static StatusPixEnum fromValue(String value) {
        if (value == null) {
            return null;
        }

        return Arrays.stream(values())
                .filter(status -> status.name().equalsIgnoreCase(value.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Status inválido: " + value + ". Valores permitidos: "
                                + Arrays.toString(values())
                ));
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}
