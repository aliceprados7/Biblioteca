package api;

import static spark.Spark.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dao.ClienteDAO;
import model.Cliente;
import api.ClienteController;

public class ClienteController {
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void setupRoutes() {
        // Define que todas as respostas serão JSON
        before("/clientes", (req, res) -> res.type("application/json"));
        before("/clientes/*", (req, res) -> res.type("application/json"));
        
        // ROTA POST: Cadastrar novo cliente
        post("/clientes", (req, res) -> {
            try {
                Cliente novoCliente = gson.fromJson(req.body(), Cliente.class);
                clienteDAO.inserir(novoCliente);
                res.status(201); // Created
                return gson.toJson(novoCliente);
            } catch (Exception e) {
                res.status(400); // Bad Request
                return "{\"mensagem\": \"Erro ao processar JSON do cliente.\"}";
            }
        });

        // ROTA GET: Listar todos os clientes
        get("/clientes", (req, res) -> {
            return gson.toJson(clienteDAO.buscarTodos());
        });
        
        // ROTA GET: Buscar cliente por ID
        get("/clientes/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                Cliente cliente = clienteDAO.buscarPorId(id);
                if (cliente != null) {
                    return gson.toJson(cliente);
                } else {
                    res.status(404);
                    return "{\"mensagem\": \"Cliente com ID " + id + " não encontrado\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"mensagem\": \"ID inválido\"}";
            }
        });
        
        // ROTA PUT: Atualizar cliente existente
        put("/clientes/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                Cliente clienteAtualizado = gson.fromJson(req.body(), Cliente.class);
                clienteAtualizado.setId(id);
                
                Cliente clienteExistente = clienteDAO.buscarPorId(id);
                if (clienteExistente != null) {
                    clienteDAO.atualizar(clienteAtualizado);
                    return gson.toJson(clienteAtualizado);
                } else {
                    res.status(404);
                    return "{\"mensagem\": \"Cliente com ID " + id + " não encontrado\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"mensagem\": \"ID inválido\"}";
            }
        });
        
        // ROTA DELETE: Remover cliente
        delete("/clientes/:id", (req, res) -> {
            try {
                Long id = Long.parseLong(req.params(":id"));
                Cliente cliente = clienteDAO.buscarPorId(id);
                if (cliente != null) {
                    clienteDAO.deletar(id);
                    return "{\"mensagem\": \"Cliente removido com sucesso\"}";
                } else {
                    res.status(404);
                    return "{\"mensagem\": \"Cliente com ID " + id + " não encontrado\"}";
                }
            } catch (NumberFormatException e) {
                res.status(400);
                return "{\"mensagem\": \"ID inválido\"}";
            }
        });
    }
}
