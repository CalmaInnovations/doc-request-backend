package com.calma.DocManagerServer.repository;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PracticanteVoluntarioRepository extends JpaRepository<PracticanteVoluntario, Long> {

    Long findIdByCorreoElectronico(String correo);
}
