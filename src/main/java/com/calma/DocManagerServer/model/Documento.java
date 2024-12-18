package com.calma.DocManagerServer.model;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documentos")
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_id")
    private Integer documentoId;

    @Column(name = "titulo", length = 200, nullable = false)
    private String titulo;

    @Column(name = "url", length = 500)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "formato", nullable = false)
    private FormatoDocumento formato;

    @ManyToOne
    @JoinColumn(name = "user_create", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_create"))
    private Usuario userCreate;

    @ManyToOne
    @JoinColumn(name = "user_update", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_update"))
    private Usuario userUpdate;

    @ManyToOne
    @JoinColumn(name = "user_delete", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_delete"))
    private Usuario userDelete;

    @Column(name = "fecha_create", nullable = false, updatable = false)
    private Timestamp fechaCreate;

    @Column(name = "fecha_update")
    private Timestamp fechaUpdate;

    @Column(name = "fecha_delete")
    private Timestamp fechaDelete;

    //Enum para el formato del documento
    public enum FormatoDocumento {
        DOC, DOCX, PDF, TXT, XLS, XLSX
    }

    // Relación ManyToMany con Etiqueta
    @ManyToMany
    @JoinTable(
            name = "documento_etiqueta", // Tabla intermedia
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "etiqueta_id")
    )
    private Set<Etiqueta> etiquetas;

    // Relación ManyToMany con Carpeta
    @ManyToMany
    @JoinTable(
            name = "documento_carpeta", // Tabla intermedia
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "carpeta_id")
    )
    private Set<Carpetas> carpetas;

    @OneToMany(mappedBy = "documento")
    private Set<VersionDocumento> versiones;
}