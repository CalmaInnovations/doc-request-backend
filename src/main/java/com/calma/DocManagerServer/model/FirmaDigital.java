package com.calma.DocManagerServer.model;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "firmas_digitales")
public class FirmaDigital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "firma_id")
    private Integer firmaId;

    @ManyToOne
    @JoinColumn(name = "documento_id", referencedColumnName = "documento_id", foreignKey = @ForeignKey(name = "FK_documento_firma"))
    private Documento documento;

    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_usuario_firma"))
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "user_create", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_create_firma"))
    private Usuario userCreate;

    @ManyToOne
    @JoinColumn(name = "user_update", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_update_firma"))
    private Usuario userUpdate;

    @ManyToOne
    @JoinColumn(name = "user_delete", referencedColumnName = "usuario_id", foreignKey = @ForeignKey(name = "FK_user_delete_firma"))
    private Usuario userDelete;

    @Column(name = "fecha_create", nullable = false, updatable = false)
    private Timestamp fechaCreate;

    @Column(name = "fecha_update")
    private Timestamp fechaUpdate;

    @Column(name = "fecha_delete")
    private Timestamp fechaDelete;

    @Lob
    @Column(name = "firma", nullable = false)
    private byte[] firma;
}