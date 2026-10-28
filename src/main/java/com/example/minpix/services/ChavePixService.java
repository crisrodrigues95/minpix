package com.example.minpix.services;

import com.example.minpix.models.ChavePix;
import com.example.minpix.repositories.ChavePixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChavePixService {

    @Autowired
    private ChavePixRepository chavePixRepository;

    public List<ChavePix> listar() {
        return chavePixRepository.findAll();
    }

    public ChavePix buscarPorId(Long id) {

        Optional<ChavePix> chavePix = chavePixRepository.findById(id);

        return chavePix.orElse(null);
    }

    public ChavePix salvar(ChavePix chavePix) {

        return chavePixRepository.save(chavePix);

    }

    public ChavePix atualizar(Long id, ChavePix chavePixAtualizada) {

        ChavePix chavePix = buscarPorId(id);

        if (chavePix == null) {
            return null;
        }

        chavePixAtualizada.setId(id);

        return chavePixRepository.save(chavePixAtualizada);

    }

    public boolean excluir(Long id) {

        ChavePix chavePix = buscarPorId(id);

        if (chavePix == null) {
            return false;
        }

        chavePixRepository.deleteById(id);

        return true;

    }

}