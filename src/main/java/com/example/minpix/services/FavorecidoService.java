package com.example.minpix.services;

import com.example.minpix.models.Favorecido;
import com.example.minpix.repositories.FavorecidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FavorecidoService {

    @Autowired
    private FavorecidoRepository favorecidoRepository;

    public List<Favorecido> listar() {

        return favorecidoRepository.findAll();

    }

    public Favorecido buscarPorId(Long id) {

        Optional<Favorecido> favorecido = favorecidoRepository.findById(id);

        return favorecido.orElse(null);

    }

    public Favorecido salvar(Favorecido favorecido) {

        return favorecidoRepository.save(favorecido);

    }

    public Favorecido atualizar(Long id, Favorecido favorecidoAtualizado) {

        Favorecido favorecido = buscarPorId(id);

        if (favorecido == null) {
            return null;
        }

        favorecidoAtualizado.setId(id);

        return favorecidoRepository.save(favorecidoAtualizado);

    }

    public boolean excluir(Long id) {

        Favorecido favorecido = buscarPorId(id);

        if (favorecido == null) {
            return false;
        }

        favorecidoRepository.deleteById(id);

        return true;

    }

}