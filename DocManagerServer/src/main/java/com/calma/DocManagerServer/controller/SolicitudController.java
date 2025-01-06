package com.calma.DocManagerServer.controller;

import com.calma.DocManagerServer.model.PracticanteVoluntario;
import com.calma.DocManagerServer.service.PracticanteVoluntarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitud")
public class SolicitudController {

    @Autowired
    private PracticanteVoluntarioService service;

    @GetMapping
    public List<PracticanteVoluntario> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PracticanteVoluntario> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PracticanteVoluntario create(@RequestBody PracticanteVoluntario practicante) {
        return service.save(practicante);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PracticanteVoluntario> update(@PathVariable Long id, @RequestBody PracticanteVoluntario practicante) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        practicante.setIdPracticanteVoluntario(id);
        return ResponseEntity.ok(service.save(practicante));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }
}