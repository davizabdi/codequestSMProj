package com.codequest.service;

import com.codequest.model.Etapa;
import com.codequest.repository.EtapaRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class EtapaService {

    private final EtapaRepository repository;

    public EtapaService() {
        this(new EtapaRepository());
    }

    public EtapaService(EtapaRepository repository) {
        this.repository = repository;
    }

    public List<Etapa> listarPorModulo(int moduloId) {
        return repository.findByModuloId(moduloId);
    }

    public Etapa buscarPorModuloENumero(int moduloId, int numero) {
        return repository.findByModuloIdAndNumero(moduloId, numero)
                .orElseThrow(() -> new NoSuchElementException("Etapa " + numero + " não encontrada para o módulo " + moduloId));
    }
}
