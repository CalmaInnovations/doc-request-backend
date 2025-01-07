package com.calma.DocManagerServer.exception;

import java.util.List;

public class DatosNoCoincidenException extends RuntimeException{
    private List<String> errores;

    public DatosNoCoincidenException(List<String> errores) {
        super("Errores de validación de datos");
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}
