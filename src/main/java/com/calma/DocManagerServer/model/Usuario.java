package com.calma.DocManagerServer.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"nombre", "correo"})})
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @Column(name = "apellido", length = 50, nullable = false)
    private String apellido;


    @Column(name = "correo", length = 100, nullable = false, unique = true)
    private String correo;

    @Column(name = "contrasena", length = 255, nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private TipoRol rol;

    @Column(name = "estado")
    private Integer estado;

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

    @ManyToMany
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<TipoRol> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(rol.getRol()));
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}