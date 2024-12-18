package com.calma.DocManagerServer.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "etiquetas")
public class Etiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etiqueta_id")
    private Integer etiquetaId;

    @Column(name = "nombre", length = 50, nullable = false, unique = true)
    private String nombre;

    @ManyToMany(mappedBy = "etiquetas") // Relación ManyToMany con Documento
    private Set<Documento> documentos;
}