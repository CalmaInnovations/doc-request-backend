package com.calma.DocManagerServer.repository;

import com.calma.DocManagerServer.model.NumeroOficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NumeroOficioRepository extends JpaRepository<NumeroOficio, Long> {

}

