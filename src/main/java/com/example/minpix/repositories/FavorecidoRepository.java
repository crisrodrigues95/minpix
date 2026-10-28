package com.example.minpix.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.minpix.models.Favorecido;

public interface FavorecidoRepository extends JpaRepository<Favorecido, Long> {

}
