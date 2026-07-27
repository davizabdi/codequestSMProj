package com.codequest.service;

import com.codequest.model.Modulo;
import com.codequest.repository.ModuloRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class ModuloService {

    private final ModuloRepository repository = new ModuloRepository();

    public List<Modulo> listar() {
        return repository.findAll();
    }

    public Modulo buscar(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Módulo " + id + " não encontrado"));
    }

    public Modulo criar(Modulo modulo) {
        validar(modulo);
        return repository.create(modulo);
    }

    public void atualizar(int id, Modulo modulo) {
        buscar(id); // garante que existe, senão lança NoSuchElementException
        validar(modulo);
        modulo.setId(id);
        repository.update(modulo);
    }

    public void excluir(int id) {
        buscar(id);
        repository.delete(id);
    }

    private void validar(Modulo modulo) {
        if (modulo.getTitulo() == null || modulo.getTitulo().isBlank()) {
            throw new IllegalArgumentException("O título do módulo é obrigatório");
        }
        if (modulo.getNumero() <= 0) {
            throw new IllegalArgumentException("O número do módulo deve ser maior que zero");
        }
    }
}
