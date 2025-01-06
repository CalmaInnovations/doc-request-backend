package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.PracticanteVoluntarioRepository;

import java.util.List;
import java.util.Optional;

public interface PracticanteVoluntarioService {


    List<PracticanteVoluntario> findAll() ;

    Optional<PracticanteVoluntario> findById(Long id);

    PracticanteVoluntario save(PracticanteVoluntario practicante);

    Long obtenerIdPorCorreo(String email);

    void deleteById(Long id) ;


}
