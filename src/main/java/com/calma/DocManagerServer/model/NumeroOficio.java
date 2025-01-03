package com.calma.DocManagerServer.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "NumeroOficio")
@Getter
@Setter
public class NumeroOficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idNumeroOficio;
    @Column(nullable = true, unique = true)
    private int numeroOficio;


}
