package com.calma.DocManagerServer.services.serviceImpl;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.repository.PracticanteVoluntarioRepository;
import com.calma.DocManagerServer.services.PracticanteVoluntarioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class PracticanteVoluntarioServiceImpl implements PracticanteVoluntarioService {
    private final PracticanteVoluntarioRepository repository;

    public PracticanteVoluntarioServiceImpl(PracticanteVoluntarioRepository repository) {
        this.repository = repository;
    }

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
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

}
