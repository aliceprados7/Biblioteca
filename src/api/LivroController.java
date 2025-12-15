package api;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.LivroDAO;
import model.Livro;

public class LivroController {
    private static final LivroDAO livroDAO = new LivroDAO();

    // Reutiliza o Gson sem o adaptador de data (Livro não precisa)
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void setupRoutes() {
        
        // Define que todas as respostas serão JSON
        before("/livros", (req, res) -> res.type("application/json"));
        before("/livros/:id", (req, res) -> res.type("application/json"));
        
        // ROTA POST: Cadastrar novo livro
        post("/livros", (req, res) -> {
            try {
                Livro novoLivro = gson.fromJson(req.body(), Livro.class);
                livroDAO.inserir(novoLivro);
                res.status(201); // Created
                return gson.toJson(novoLivro);
            } catch (Exception e) {
                res.status(400); // Bad Request
                return "{\"mensagem\": \"Erro ao processar JSON para livro.\"}";
            }
        });

        // ROTA GET: Listar todos os livros
        get("/livros", (req, res) -> {
            return gson.toJson(livroDAO.buscarTodos());
        });
        
        // ROTA GET: Buscar livro por ID
        get("/livros/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                Livro livro = livroDAO.buscarPorId(id);
                if (livro != null) {
                    return gson.toJson(livro);
                } else {
                    res.status(404);
                    return "{\"mensagem\": \"Livro com ID " + id + " não encontrado\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"mensagem\": \"Formato de ID inválido\"}";
            }
        });

        // ROTA PUT: Atualizar livro
        put("/livros/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                Livro livroParaAtualizar = gson.fromJson(req.body(), Livro.class);
                
                if (livroDAO.buscarPorId(id) == null) {
                    res.status(404);
                    return "{\"mensagem\": \"Livro não encontrado para atualização.\"}";
                }
                
                livroParaAtualizar.setId(id);
                livroDAO.atualizar(livroParaAtualizar);
                
                res.status(200);
                return gson.toJson(livroParaAtualizar);
            } catch (Exception e) {
                res.status(500);
                return "{\"mensagem\": \"Erro ao atualizar livro.\"}";
            }
        });
        
        // ROTA DELETE: Deletar livro
        delete("/livros/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                livroDAO.deletar(id);
                res.status(204); // No Content
                return "";
            } catch (Exception e) {
                // Captura exceção de integridade referencial se o Livro estiver emprestado
                if (e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException) {
                    res.status(409); 
                    return "{\"mensagem\": \"Não é possível deletar. Livro está sendo referenciado em um Empréstimo.\"}";
                }
                res.status(400); 
                return "{\"mensagem\": \"Erro ao deletar livro.\"}";
            }
        });
    }
}