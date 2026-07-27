package com.codequest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Configuração central de acesso ao banco de dados PostgreSQL.
 * Lê as credenciais de variáveis de ambiente (com valores padrão para dev local)
 * e executa as migrations (CREATE TABLE IF NOT EXISTS) na inicialização,
 * o que evita depender de ferramenta externa de migration nesse projeto simples.
 */
public class Database {

    private static HikariDataSource dataSource;

    public static void init() {
        String host = env("DB_HOST", "localhost");
        String port = env("DB_PORT", "5432");
        String name = env("DB_NAME", "codequest");
        String user = env("DB_USER", "postgres");
        String pass = env("DB_PASSWORD", "postgres");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + ":" + port + "/" + name);
        config.setUsername(user);
        config.setPassword(pass);
        config.setMaximumPoolSize(10);
        config.setDriverClassName("org.postgresql.Driver");

        dataSource = new HikariDataSource(config);

        migrate();
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static void migrate() {
        String createModulos = """
                CREATE TABLE IF NOT EXISTS modulos (
                    id SERIAL PRIMARY KEY,
                    numero INT NOT NULL,
                    titulo VARCHAR(255) NOT NULL,
                    descricao TEXT,
                    topicos INT NOT NULL DEFAULT 0,
                    disponivel BOOLEAN NOT NULL DEFAULT FALSE,
                    ordem INT NOT NULL DEFAULT 0
                )
                """;

        String createQuestoes = """
                CREATE TABLE IF NOT EXISTS questoes (
                    id SERIAL PRIMARY KEY,
                    modulo_id INT NOT NULL REFERENCES modulos(id) ON DELETE CASCADE,
                    enunciado TEXT NOT NULL,
                    alternativa_a VARCHAR(255) NOT NULL,
                    alternativa_b VARCHAR(255) NOT NULL,
                    alternativa_c VARCHAR(255) NOT NULL,
                    alternativa_d VARCHAR(255) NOT NULL,
                    correta CHAR(1) NOT NULL,
                    ordem INT NOT NULL DEFAULT 0
                )
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createModulos);
            stmt.execute(createQuestoes);
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao rodar as migrations do banco", e);
        }
    }

    private static String env(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }
}
