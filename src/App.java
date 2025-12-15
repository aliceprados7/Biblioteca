import static spark.Spark.*;
import api.LivroController;
import api.EmprestimoController;

public class App {
    public static void main(String[] args) {
        // Configura a porta (usa variável de ambiente ou padrão 4567)
        port(4567);
        
        // Habilita CORS
        enableCORS();
        
        // Configura o local dos arquivos estáticos (frontend)
        String frontendPath = System.getProperty("user.dir") + "/front-end";
        staticFiles.externalLocation(frontendPath);
        
        // Registra as rotas da API
        LivroController.setupRoutes();
        EmprestimoController.setupRoutes();
        
        // Redireciona a raiz para index.html
        get("/", (req, res) -> {
            res.redirect("/html/index.html");
            return null;
        });
        
        System.out.println("Servidor rodando em http://localhost:4567");
    }
    
    // Habilita CORS para todas as rotas
    private static void enableCORS() {
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.type("application/json");
        });
    }
}
