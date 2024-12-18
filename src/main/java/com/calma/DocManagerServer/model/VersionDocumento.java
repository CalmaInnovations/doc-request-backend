package com.calma.DocManagerServer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "versiones")
public class VersionDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_id")
    private Integer versionId;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private Documento documento; // Relación ManyToOne con Documento

    @Column(name = "formato_origen")
    private String formatoOrigen;

    @Column(name = "formato_destino")
    private String formatoDestino;

    @Column(name = "version_numero")
    private Integer versionNumero;

    @Column(name = "es_version_actual")
    private Boolean esVersionActual;

    @Column(name = "url")
    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Usuario usuario;

    @Column(name = "fecha_create", updatable = false)
    private Timestamp fechaCreate;

    @Column(name = "fecha_conversion")
    private Timestamp fechaConversion;

}
