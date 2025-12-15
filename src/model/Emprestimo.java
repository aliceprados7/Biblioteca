package model;

import java.time.LocalDateTime;

public class Emprestimo {

    private Long id;
    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataDevolucao; // Pode ser NULL
    private Cliente cliente; // Objeto completo, não apenas ID
    private Livro livro;   // Objeto completo, não apenas ID

    public Emprestimo() {
    }

    // Construtor completo (para leitura do BD)
    public Emprestimo(Long id, LocalDateTime dataEmprestimo, LocalDateTime dataDevolucao, Cliente cliente, Livro livro) {
        this.id = id;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.cliente = cliente;
        this.livro = livro;
    }
    
    // Construtor para inserção (sem ID do empréstimo)
    public Emprestimo(LocalDateTime dataEmprestimo, LocalDateTime dataDevolucao, Cliente cliente, Livro livro) {
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.cliente = cliente;
        this.livro = livro;
    }


    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(LocalDateTime dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public LocalDateTime getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(LocalDateTime dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }

    @Override
    public String toString() {
        return "Emprestimo [id=" + id + ", dataEmprestimo=" + dataEmprestimo 
               + ", dataDevolucao=" + dataDevolucao + ", cliente=" + cliente.getNome() 
               + ", livro=" + livro.getNome() + "]";
    }
}