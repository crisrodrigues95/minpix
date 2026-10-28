package com.example.minpix.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.minpix.models.TransferenciaPix;

@Repository
public interface TransferenciaPixRepository extends JpaRepository<TransferenciaPix, Long> {

}
