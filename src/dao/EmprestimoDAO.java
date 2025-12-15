package dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.Emprestimo;
import model.Cliente;
import model.Livro;
import util.ConnectionFactory;

public class EmprestimoDAO {

    // ------------------------------------
    // READ (Todos com JOINs)
    // ------------------------------------
    public List<Emprestimo> buscarTodos() {
        List<Emprestimo> emprestimos = new ArrayList<>();
        String sql = "SELECT e.id AS id_emprestimo, e.data_emprestimo, e.data_devolucao, " +
                     "c.id AS id_cliente, c.nome AS nome_cliente, c.cpf, c.telefone, " +
                     "l.id AS id_livro, l.nome AS nome_livro, l.autor " +
                     "FROM emprestimo e " +
                     "JOIN cliente c ON e.id_cliente = c.id " +
                     "JOIN livro l ON e.id_livro = l.id";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getLong("id_cliente"),
                    rs.getString("nome_cliente"),
                    rs.getString("telefone"),
                    rs.getString("cpf")
                );

                Livro livro = new Livro(
                    rs.getLong("id_livro"),
                    rs.getString("nome_livro"),
                    rs.getString("autor")
                );

                Timestamp tsEmprestimo = rs.getTimestamp("data_emprestimo");
                Timestamp tsDevolucao  = rs.getTimestamp("data_devolucao");

                LocalDateTime dataEmprestimo = (tsEmprestimo != null) ? tsEmprestimo.toLocalDateTime() : null;
                LocalDateTime dataDevolucao  = (tsDevolucao != null) ? tsDevolucao.toLocalDateTime() : null;

                Emprestimo emprestimo = new Emprestimo(
                    rs.getLong("id_emprestimo"),
                    dataEmprestimo,
                    dataDevolucao,
                    cliente,
                    livro
                );
                emprestimos.add(emprestimo);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar empréstimos: " + e.getMessage());
            e.printStackTrace();
        }
        return emprestimos;
    }

    // ------------------------------------
    // CREATE (Registrar Empréstimo)
    // ------------------------------------
    public void inserir(Emprestimo emprestimo) {
        String sql = "INSERT INTO emprestimo (id_cliente, id_livro, data_emprestimo, data_devolucao) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (emprestimo.getCliente() == null || emprestimo.getCliente().getId() == null ||
                emprestimo.getLivro() == null || emprestimo.getLivro().getId() == null) {
                throw new IllegalArgumentException("Cliente e Livro devem ter IDs válidos.");
            }

            stmt.setLong(1, emprestimo.getCliente().getId());
            stmt.setLong(2, emprestimo.getLivro().getId());
            
            // Tratamento de LocalDateTime para DB (Timestamp)
            stmt.setTimestamp(3, Timestamp.valueOf(emprestimo.getDataEmprestimo()));

            if (emprestimo.getDataDevolucao() != null) {
                stmt.setTimestamp(4, Timestamp.valueOf(emprestimo.getDataDevolucao()));
            } else {
                stmt.setNull(4, Types.TIMESTAMP); // Define como NULL se não houver data de devolução
            }

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    emprestimo.setId(rs.getLong(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar empréstimo. Detalhes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ... (Os métodos buscarPorId, alterar, deletar seriam similares aos de ConsultaDAO)
}