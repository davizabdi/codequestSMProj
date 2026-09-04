package com.codequest.repository;

import com.codequest.config.Database;
import com.codequest.model.Etapa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EtapaRepository {

    public List<Etapa> findByModuloId(int moduloId) {
        String sql = "SELECT * FROM etapas WHERE modulo_id = ? ORDER BY ordem, numero";
        List<Etapa> etapas = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduloId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    etapas.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar etapas do módulo " + moduloId, e);
        }
        return etapas;
    }

    public Optional<Etapa> findByModuloIdAndNumero(int moduloId, int numero) {
        String sql = "SELECT * FROM etapas WHERE modulo_id = ? AND numero = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, moduloId);
            stmt.setInt(2, numero);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar etapa " + numero + " do módulo " + moduloId, e);
        }
        return Optional.empty();
    }

    private Etapa map(ResultSet rs) throws SQLException {
        return new Etapa(
                rs.getInt("id"),
                rs.getInt("modulo_id"),
                rs.getInt("numero"),
                rs.getString("titulo"),
                rs.getString("objetivo"),
                rs.getString("explicacao"),
                rs.getString("exemplo"),
                rs.getInt("ordem")
        );
    }
}
