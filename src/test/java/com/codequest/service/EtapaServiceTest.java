package com.codequest.service;

import com.codequest.model.Etapa;
import com.codequest.repository.EtapaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EtapaServiceTest {

    private EtapaService etapaService;
    private StubEtapaRepository stubRepository;

    @BeforeEach
    public void setUp() {
        stubRepository = new StubEtapaRepository();
        etapaService = new EtapaService(stubRepository);
    }

    @Test
    @DisplayName("Deve listar todas as etapas de um módulo")
    public void testListarPorModulo() {
        List<Etapa> etapas = etapaService.listarPorModulo(1);
        assertEquals(2, etapas.size());
        assertEquals("O que é Lógica de Programação?", etapas.get(0).getTitulo());
    }

    @Test
    @DisplayName("Deve buscar uma etapa específica com sucesso")
    public void testBuscarPorModuloENumeroSucesso() {
        Etapa etapa = etapaService.buscarPorModuloENumero(1, 1);
        assertNotNull(etapa);
        assertEquals(1, etapa.getNumero());
        assertEquals("O que é Lógica de Programação?", etapa.getTitulo());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a etapa não for encontrada")
    public void testBuscarPorModuloENumeroNaoEncontrado() {
        assertThrows(NoSuchElementException.class, () -> {
            etapaService.buscarPorModuloENumero(1, 99);
        });
    }

    private static class StubEtapaRepository extends EtapaRepository {
        @Override
        public List<Etapa> findByModuloId(int moduloId) {
            if (moduloId == 1) {
                return List.of(
                        new Etapa(1, 1, 1, "O que é Lógica de Programação?", "Objetivo 1", "Explicação 1", "Exemplo 1", 1),
                        new Etapa(2, 1, 2, "Algoritmos", "Objetivo 2", "Explicação 2", "Exemplo 2", 2)
                );
            }
            return List.of();
        }

        @Override
        public Optional<Etapa> findByModuloIdAndNumero(int moduloId, int numero) {
            if (moduloId == 1 && numero == 1) {
                return Optional.of(new Etapa(1, 1, 1, "O que é Lógica de Programação?", "Objetivo 1", "Explicação 1", "Exemplo 1", 1));
            }
            return Optional.empty();
        }
    }
}
