package com.calma.DocManagerServer.service;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.PracticanteVoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PracticanteVoluntarioService {

    @Autowired
    private PracticanteVoluntarioRepository repository;

    public List<PracticanteVoluntario> findAll() {
        return repository.findAll();
    }

    public Optional<PracticanteVoluntario> findById(Long id) {
        return repository.findById(id);
    }

    public PracticanteVoluntario save(PracticanteVoluntario practicante) {
        return repository.save(practicante);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}