package com.calma.DocManagerServer.dto;

import java.time.LocalDate;

public class DatosDTO {
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

    // Getters y Setters
    public String getNombresApellidos() {
        return nombresApellidos;
    }

    public void setNombresApellidos(String nombresApellidos) {
        this.nombresApellidos = nombresApellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getUniversidadOInstituto() {
        return universidadOInstituto;
    }

    public void setUniversidadOInstituto(String universidadOInstituto) {
        this.universidadOInstituto = universidadOInstituto;
    }

    public String getCodigoEstudiante() {
        return codigoEstudiante;
    }

    public void setCodigoEstudiante(String codigoEstudiante) {
        this.codigoEstudiante = codigoEstudiante;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getTipoPracticas() {
        return tipoPracticas;
    }

    public void setTipoPracticas(String tipoPracticas) {
        this.tipoPracticas = tipoPracticas;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLiderArea() {
        return liderArea;
    }

    public void setLiderArea(String liderArea) {
        this.liderArea = liderArea;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    @Override
    public String toString() {
        return "DatosPracticasRequest{" +
                "nombresApellidos='" + nombresApellidos + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                ", dni='" + dni + '\'' +
                ", celular='" + celular + '\'' +
                ", universidadOInstituto='" + universidadOInstituto + '\'' +
                ", codigoEstudiante='" + codigoEstudiante + '\'' +
                ", carrera='" + carrera + '\'' +
                ", tipoPracticas='" + tipoPracticas + '\'' +
                ", area='" + area + '\'' +
                ", liderArea='" + liderArea + '\'' +
                ", puesto='" + puesto + '\'' +
                ", fechaIngreso=" + fechaIngreso +
                ", fechaSalida=" + fechaSalida +
                '}';
    }
}