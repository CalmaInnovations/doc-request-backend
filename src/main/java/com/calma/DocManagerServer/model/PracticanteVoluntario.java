package com.calma.DocManagerServer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "PracticanteVoluntario")
public class PracticanteVoluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPracticanteVoluntario;

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
    private String fechaIngreso;
    private String fechaSalida;

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Long getIdPracticanteVoluntario() {
        return idPracticanteVoluntario;
    }

    public void setIdPracticanteVoluntario(Long idPracticanteVoluntario) {
        this.idPracticanteVoluntario = idPracticanteVoluntario;
    }

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



}
