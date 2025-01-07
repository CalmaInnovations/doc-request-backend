package com.calma.DocManagerServer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DatosDTO {
    // Getters y Setters
    private String nombresApellidos;
    private String correoElectronico;
    private String dni;
    private String celular;
    private String universidadOInstituto;
    private String codigoEstudiante;
    private String carrera;
    private String tipoPracticas;
    private String area;
    private String liderArea;
    private String puesto;
    private LocalDate fechaIngreso;
    private LocalDate fechaSalida;

    @Override
    public String toString() {
        return "DatosPracticasRequest{" +
                "NOMBRES Y APELLIDOS='" + nombresApellidos + '\'' +
                ", CORREO ELECTRÓNICO='" + correoElectronico + '\'' +
                ", DNI='" + dni + '\'' +
                ", CELULAR='" + celular + '\'' +
                ", Univesidad o instituto='" + universidadOInstituto + '\'' +
                ", CODIGO DE ESTUDIANTE='" + codigoEstudiante + '\'' +
                ", CARRERA='" + carrera + '\'' +
                ", TIPO DE PRÁCTICAS='" + tipoPracticas + '\'' +
                ", ÁREA='" + area + '\'' +
                ", LÍDER DEL ÁREA='" + liderArea + '\'' +
                ", PUESTO='" + puesto + '\'' +
                ", INGRESO=" + fechaIngreso +
                ", SALIDA=" + fechaSalida +
                '}';
    }
}