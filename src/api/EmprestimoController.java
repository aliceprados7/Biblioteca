package api;

import static spark.Spark.*;

import com.google.gson.Gson;
import dao.EmprestimoDAO;
import model.Emprestimo;
import model.Cliente; // Importa Cliente
import model.Livro;   // Importa Livro
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// OBS: Assumindo que você moverá o LocalDateTimeAdapter e a configuração GSON para uma classe UTILS separada
// Por enquanto, usarei a mesma lógica aqui.

public class EmprestimoController {
    private static final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    // ----------------------------------------------------
    // GSON TypeAdapter para LocalDateTime (COPIADO DO SEU PADRÃO)
    // ----------------------------------------------------
    private static class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public JsonElement serialize(LocalDateTime localDateTime, java.lang.reflect.Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDateTime));
        }

        @Override
        public LocalDateTime deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null || json.getAsString().isEmpty()) {
                return null;
            }
            return LocalDateTime.parse(json.getAsString(), formatter);
        }
    }
    
    // Cria a instância do Gson com o adaptador para LocalDateTime
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();
    
    public static void setupRoutes() {
        
        before("/emprestimos", (req, res) -> res.type("application/json"));
        before("/emprestimos/:id", (req, res) -> res.type("application/json"));
        
        // ROTA POST: Registrar novo empréstimo
        post("/emprestimos", (req, res) -> {
            try {
                JsonObject json = gson.fromJson(req.body(), JsonObject.class);
                
                // Extrai IDs e cria objetos parciais (somente com ID)
                Long idCliente = json.getAsJsonObject("cliente").get("id").getAsLong();
                Long idLivro = json.getAsJsonObject("livro").get("id").getAsLong();
                
                Cliente cliente = new Cliente(idCliente, null, null, null);
                Livro livro = new Livro(idLivro, null, null);

                // Datas (usa o Gson com adapter para parsear datas)
                LocalDateTime dataEmprestimo = gson.fromJson(json.get("data_emprestimo"), LocalDateTime.class);
                LocalDateTime dataDevolucao = null;
                if (json.has("data_devolucao") && !json.get("data_devolucao").isJsonNull()) {
                    dataDevolucao = gson.fromJson(json.get("data_devolucao"), LocalDateTime.class);
                }

                Emprestimo novoEmprestimo = new Emprestimo(dataEmprestimo, dataDevolucao, cliente, livro);
                
                emprestimoDAO.inserir(novoEmprestimo);

                res.status(201);
                return gson.toJson(novoEmprestimo);

            } catch (Exception e) {
                res.status(500);
                System.out.println("Erro ao processar requisição POST de Empréstimo: " + e.getMessage());
                e.printStackTrace();
                return "{\"mensagem\": \"Erro ao registrar empréstimo. Verifique IDs e formato de data.\"}";
            }
        });

        // ROTA GET: Listar todos os empréstimos
        get("/emprestimos", (req, res) -> {
            return gson.toJson(emprestimoDAO.buscarTodos());
        });
        
        // ... (PUT e DELETE seriam adicionados aqui se necessário, seguindo o padrão)
    }
}