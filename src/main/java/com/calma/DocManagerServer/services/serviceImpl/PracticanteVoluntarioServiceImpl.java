package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.PracticanteVoluntarioRepository;
import com.calma.DocManagerServer.services.DocumentoService;
import com.calma.DocManagerServer.services.PracticanteVoluntarioService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service

public class PracticanteVoluntarioServiceImpl implements PracticanteVoluntarioService {
    @Autowired
    private PracticanteVoluntarioRepository repository;

    @Override
    public List<PracticanteVoluntario> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PracticanteVoluntario> findById(Long id) {


        return repository.findById(id);
    }

    @Override
    public PracticanteVoluntario save(PracticanteVoluntario practicante) {
        return null;
    }


    @Override
    public Long obtenerIdPorCorreo(String correo) {
        return repository.findIdByCorreoElectronico(correo);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
