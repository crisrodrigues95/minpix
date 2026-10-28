package com.example.minpix.services;

import com.example.minpix.models.TransferenciaPix;
import com.example.minpix.repositories.TransferenciaPixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransferenciaPixService {

    @Autowired
    private TransferenciaPixRepository transferenciaPixRepository;

    public List<TransferenciaPix> listar() {

        return transferenciaPixRepository.findAll();

    }

    public TransferenciaPix buscarPorId(Long id) {

        Optional<TransferenciaPix> transferenciaPix = transferenciaPixRepository.findById(id);

        return transferenciaPix.orElse(null);

    }

    public TransferenciaPix salvar(TransferenciaPix transferenciaPix) {

        return transferenciaPixRepository.save(transferenciaPix);

    }

    public TransferenciaPix atualizar(Long id, TransferenciaPix transferenciaPixAtualizada) {

        TransferenciaPix transferenciaPix = buscarPorId(id);

        if (transferenciaPix == null) {
            return null;
        }

        transferenciaPixAtualizada.setId(id);

        return transferenciaPixRepository.save(transferenciaPixAtualizada);

    }

    public boolean excluir(Long id) {

        TransferenciaPix transferenciaPix = buscarPorId(id);

        if (transferenciaPix == null) {
            return false;
        }

        transferenciaPixRepository.deleteById(id);

        return true;

    }

}
