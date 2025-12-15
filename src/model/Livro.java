package model;

public class Livro {
    private Long id;
    private String nome; // Corresponde à coluna 'nome'
    private String autor; // Corresponde à coluna 'autor'

    public Livro() {
    }

    // Construtor completo (usado para leitura do BD)
    public Livro(Long id, String nome, String autor) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
    }

    // Construtor para inserção (sem ID)
    public Livro(String nome, String autor) {
        this.nome = nome;
        this.autor = autor;
    }

    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getAutor() { return autor; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setAutor(String autor) { this.autor = autor; }
    
    @Override
    public String toString() {
        return "Livro[id=" + id + ", nome=" + nome + ", autor=" + autor + "]";
    }
}