package com.codequest.service;

import com.codequest.model.Modulo;
import com.codequest.repository.ModuloRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ModuloServiceTest {

    private ModuloService moduloService;
    private StubModuloRepository stubRepository;

    @BeforeEach
    public void setUp() {
        stubRepository = new StubModuloRepository();
        moduloService = new ModuloService(stubRepository);
    }

    @Test
    @DisplayName("Deve listar todos os módulos disponíveis")
    public void testListarModulos() {
        List<Modulo> modulos = moduloService.listar();
        assertEquals(1, modulos.size());
        assertEquals("Introdução à Lógica de Programação", modulos.get(0).getTitulo());
    }

    @Test
    @DisplayName("Deve buscar um módulo por ID com sucesso")
    public void testBuscarModuloSucesso() {
        Modulo modulo = moduloService.buscar(1);
        assertNotNull(modulo);
        assertEquals(1, modulo.getNumero());
    }

    @Test
    @DisplayName("Deve lançar exceção quando o módulo não for encontrado")
    public void testBuscarModuloNaoEncontrado() {
        assertThrows(NoSuchElementException.class, () -> {
            moduloService.buscar(999);
        });
    }

    private static class StubModuloRepository extends ModuloRepository {
        @Override
        public List<Modulo> findAll() {
            return List.of(new Modulo(1, 1, "Introdução à Lógica de Programação", "Descrição", 5, true, 1));
        }

        @Override
        public Optional<Modulo> findById(int id) {
            if (id == 1) {
                return Optional.of(new Modulo(1, 1, "Introdução à Lógica de Programação", "Descrição", 5, true, 1));
            }
            return Optional.empty();
        }
    }
}
