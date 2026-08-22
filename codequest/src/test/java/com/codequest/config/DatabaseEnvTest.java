package com.codequest.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DatabaseEnvTest {

    @Test
    @DisplayName("Deve retornar valor padrão quando variável não existir")
    void deveRetornarValorPadraoQuandoNaoExistir() {
        String resultado = Database.env("VARIAVEL_TOTALMENTE_INEXISTENTE_XYZ", "padrao_teste");
        assertEquals("padrao_teste", resultado);
    }

    @Test
    @DisplayName("Deve resolver porta configurada ou fallback")
    void deveResolverPorta() {
        String porta = Database.env("PORT", "7000");
        assertNotNull(porta);
    }
}
