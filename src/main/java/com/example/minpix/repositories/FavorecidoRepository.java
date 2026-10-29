package com.example.minpix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.minpix.models.Favorecido;

@Repository
public interface FavorecidoRepository extends JpaRepository<Favorecido, Long> {

}
