package com.example.minpix.services;


import com.example.minpix.models.Comprovante;
import com.example.minpix.repositories.ComprovanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComprovanteService {

    @Autowired
    private ComprovanteRepository comprovanteRepository;

    public List<Comprovante> listar() {

        return comprovanteRepository.findAll();

    }

    public Comprovante buscarPorId(Long id) {

        Optional<Comprovante> comprovante = comprovanteRepository.findById(id);

        return comprovante.orElse(null);

    }

    public Comprovante salvar(Comprovante comprovante) {

        return comprovanteRepository.save(comprovante);

    }

    public Comprovante atualizar(Long id, Comprovante comprovanteAtualizado) {

        Comprovante comprovante = buscarPorId(id);

        if (comprovante == null) {
            return null;
        }

        comprovanteAtualizado.setId(id);

        return comprovanteRepository.save(comprovanteAtualizado);

    }

    public boolean excluir(Long id) {

        Comprovante comprovante = buscarPorId(id);

        if (comprovante == null) {
            return false;
        }

        comprovanteRepository.deleteById(id);

        return true;

    }

}
