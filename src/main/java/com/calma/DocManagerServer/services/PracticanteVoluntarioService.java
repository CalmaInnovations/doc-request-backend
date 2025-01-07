package com.calma.DocManagerServer.services;

import com.calma.DocManagerServer.model.PracticanteVoluntario;

import java.util.List;
import java.util.Optional;

public interface PracticanteVoluntarioService {


    List<PracticanteVoluntario> findAll() ;

    Optional<PracticanteVoluntario> findById(Long id);

    PracticanteVoluntario save(PracticanteVoluntario practicante);

    void deleteById(Long id) ;


}
