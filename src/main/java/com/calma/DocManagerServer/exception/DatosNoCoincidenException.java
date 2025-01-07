package com.calma.DocManagerServer.exception;

import lombok.Getter;

import java.util.List;
@Getter
public class DatosNoCoincidenException extends RuntimeException{
    private final List<String> errores;

    public DatosNoCoincidenException(List<String> errores) {
        super("Errores de validación de datos");
        this.errores = errores;
    }
}
