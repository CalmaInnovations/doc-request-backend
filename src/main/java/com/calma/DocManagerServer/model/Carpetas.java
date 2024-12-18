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
@Table(name = "carpetas")
public class Carpetas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carpeta_id")
    private Integer carpetaId;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "carpeta_padre", referencedColumnName = "carpeta_id", foreignKey = @ForeignKey(name = "FK_carpeta_padre"))
    private Carpetas carpetaPadre;

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

    @ManyToMany(mappedBy = "carpetas")
    private Set<Documento> documentos;
}