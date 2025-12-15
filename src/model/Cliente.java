package model;

public class Cliente {

    private Long id;
    private String nome;
    private String telefone;
    private String cpf;

    // Construtor padrão
    public Cliente() {
    }

    // Construtor completo (para leitura do BD)
    public Cliente(Long id, String nome, String telefone, String cpf) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
    }
    
    // Construtor para inserção (sem ID)
    public Cliente(String nome, String telefone, String cpf) {
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
    }
    
    // Construtor parcial (usado em DAOs para referenciar apenas pelo ID)
    public Cliente(Long id) {
        this.id = id;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    // Método toString
    @Override
    public String toString() {
        return "Cliente[id=" + id + ", nome=" + nome + ", telefone=" + telefone + ", cpf=" + cpf + "]";
    }
}