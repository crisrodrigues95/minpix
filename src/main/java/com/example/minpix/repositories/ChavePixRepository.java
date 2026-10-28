package com.example.minpix.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.minpix.models.ChavePix;

@Repository
public interface ChavePixRepository extends JpaRepository<ChavePix, Long> {

}
