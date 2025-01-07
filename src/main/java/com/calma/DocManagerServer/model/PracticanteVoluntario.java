package com.calma.DocManagerServer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "PracticanteVoluntario")
@Getter
@Setter
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




}
