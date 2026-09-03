package com.codequest.repository;

import com.codequest.config.Database;
import com.codequest.model.Modulo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuloRepository {

    public List<Modulo> findAll() {
        String sql = "SELECT * FROM modulos ORDER BY ordem, numero";
        List<Modulo> modulos = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                modulos.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar módulos", e);
        }
        return modulos;
    }

    public Optional<Modulo> findById(int id) {
        String sql = "SELECT * FROM modulos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar módulo " + id, e);
        }
        return Optional.empty();
    }

    public Modulo create(Modulo modulo) {
        String sql = """
                INSERT INTO modulos (numero, titulo, descricao, topicos, disponivel, ordem)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bind(stmt, modulo);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    modulo.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar módulo", e);
        }
        return modulo;
    }

    public void update(Modulo modulo) {
        String sql = """
                UPDATE modulos
                SET numero = ?, titulo = ?, descricao = ?, topicos = ?, disponivel = ?, ordem = ?
                WHERE id = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bind(stmt, modulo);
            stmt.setInt(7, modulo.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar módulo " + modulo.getId(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM modulos WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir módulo " + id, e);
        }
    }

    private void bind(PreparedStatement stmt, Modulo modulo) throws SQLException {
        stmt.setInt(1, modulo.getNumero());
        stmt.setString(2, modulo.getTitulo());
        stmt.setString(3, modulo.getDescricao());
        stmt.setInt(4, modulo.getTopicos());
        stmt.setBoolean(5, modulo.isDisponivel());
        stmt.setInt(6, modulo.getOrdem());
    }

    private Modulo map(ResultSet rs) throws SQLException {
        return new Modulo(
                rs.getInt("id"),
                rs.getInt("numero"),
                rs.getString("titulo"),
                rs.getString("descricao"),
                rs.getInt("topicos"),
                rs.getBoolean("disponivel"),
                rs.getInt("ordem")
        );
    }
}
