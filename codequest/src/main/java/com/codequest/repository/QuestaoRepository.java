package com.codequest.repository;

import com.codequest.config.Database;
import com.codequest.model.Questao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestaoRepository {

    public List<Questao> findByModulo(int moduloId) {
        String sql = "SELECT * FROM questoes WHERE modulo_id = ? ORDER BY ordem, id";
        List<Questao> questoes = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduloId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    questoes.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar questões do módulo " + moduloId, e);
        }
        return questoes;
    }

    public Optional<Questao> findById(int id) {
        String sql = "SELECT * FROM questoes WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar questão " + id, e);
        }
        return Optional.empty();
    }

    public Questao create(Questao questao) {
        String sql = """
                INSERT INTO questoes
                    (modulo_id, enunciado, alternativa_a, alternativa_b, alternativa_c, alternativa_d, correta, ordem)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            bind(stmt, questao);
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    questao.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar questão", e);
        }
        return questao;
    }

    public void update(Questao questao) {
        String sql = """
                UPDATE questoes
                SET modulo_id = ?, enunciado = ?, alternativa_a = ?, alternativa_b = ?,
                    alternativa_c = ?, alternativa_d = ?, correta = ?, ordem = ?
                WHERE id = ?
                """;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            bind(stmt, questao);
            stmt.setInt(9, questao.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar questão " + questao.getId(), e);
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM questoes WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir questão " + id, e);
        }
    }

    private void bind(PreparedStatement stmt, Questao questao) throws SQLException {
        stmt.setInt(1, questao.getModuloId());
        stmt.setString(2, questao.getEnunciado());
        stmt.setString(3, questao.getAlternativaA());
        stmt.setString(4, questao.getAlternativaB());
        stmt.setString(5, questao.getAlternativaC());
        stmt.setString(6, questao.getAlternativaD());
        stmt.setString(7, questao.getCorreta());
        stmt.setInt(8, questao.getOrdem());
    }

    private Questao map(ResultSet rs) throws SQLException {
        return new Questao(
                rs.getInt("id"),
                rs.getInt("modulo_id"),
                rs.getString("enunciado"),
                rs.getString("alternativa_a"),
                rs.getString("alternativa_b"),
                rs.getString("alternativa_c"),
                rs.getString("alternativa_d"),
                rs.getString("correta"),
                rs.getInt("ordem")
        );
    }
}
