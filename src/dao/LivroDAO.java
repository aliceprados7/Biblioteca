package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Livro;
import util.ConnectionFactory; // Mantenha ConnectionFactory (seu código)

public class LivroDAO {

    // ------------------------------------
    // READ (Todos)
    // ------------------------------------
    public List<Livro> buscarTodos() {
        List<Livro> livros = new ArrayList<>();
        String sql = "SELECT id, nome, autor FROM livro";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Livro livro = new Livro(
                    rs.getLong("id"),
                    rs.getString("nome"),
                    rs.getString("autor")
                );
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livros: " + e.getMessage());
            e.printStackTrace();
        }
        return livros;
    }

    // ------------------------------------
    // READ BY ID
    // ------------------------------------
    public Livro buscarPorId(Long id) {
        Livro livro = null;
        String sql = "SELECT id, nome, autor FROM livro WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    livro = new Livro(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("autor")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar livro por ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
        return livro;
    }

    // ------------------------------------
    // CREATE
    // ------------------------------------
    public void inserir(Livro livro) {
        String sql = "INSERT INTO livro (nome, autor) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, livro.getNome());
            stmt.setString(2, livro.getAutor());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    livro.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao inserir livro: " + livro.getNome() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ------------------------------------
    // UPDATE
    // ------------------------------------
    public void atualizar(Livro livro) {
        String sql = "UPDATE livro SET nome = ?, autor = ? WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livro.getNome());
            stmt.setString(2, livro.getAutor());
            stmt.setLong(3, livro.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar livro ID: " + livro.getId() + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------
    // DELETE
    // ------------------------------------
    public void deletar(Long id) {
        String sql = "DELETE FROM livro WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar livro ID: " + id + ". Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}