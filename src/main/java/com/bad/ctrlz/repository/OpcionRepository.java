package com.bad.ctrlz.repository;

import com.bad.ctrlz.model.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Integer> { }
