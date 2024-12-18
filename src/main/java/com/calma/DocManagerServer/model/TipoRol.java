package com.calma.DocManagerServer.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_rol")
public class TipoRol {
    @Id
    @Column(name = "rol_id")
    private Integer rolId;

    @Column(name = "rol", length = 100, nullable = false)
    private String rol;
}