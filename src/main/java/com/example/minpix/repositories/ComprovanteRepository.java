package com.example.minpix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.minpix.models.Comprovante;

@Repository
public interface  ComprovanteRepository extends JpaRepository<Comprovante, Long> {

}
