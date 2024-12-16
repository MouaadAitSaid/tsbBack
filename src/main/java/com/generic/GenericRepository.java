package com.generic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    // Ce repository hérite de JpaRepository pour toutes les opérations CRUD
    // et de JpaSpecificationExecutor pour le filtrage et la recherche dynamique
}