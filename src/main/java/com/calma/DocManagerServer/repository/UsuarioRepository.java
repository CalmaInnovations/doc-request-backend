package com.calma.DocManagerServer.repository;

import com.calma.DocManagerServer.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}
