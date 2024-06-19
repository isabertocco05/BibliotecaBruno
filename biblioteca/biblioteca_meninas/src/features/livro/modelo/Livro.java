package features.livro.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class Livro {

    @Id
    @GeneratedValue
    private int id;
    private String titulo;
    private String autor;
    private String categoria;
    private int isbn;
    private int unidades;
    private boolean disponibilidade;
    private int tempoParaEmprestimo;

    public Livro(String titulo, String autor, String categoria, int isbn, int unidades, boolean disponibilidade, int tempoParaEmprestimo) {
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.isbn = isbn;
        this.unidades = unidades;
        this.disponibilidade = disponibilidade;
        this.tempoParaEmprestimo = tempoParaEmprestimo;
    }

    public Livro() {}

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getIsbn() {
        return isbn;
    }

    public void setIsbn(int isbn) {
        this.isbn = isbn;
    }

    public int getUnidades() {
        return unidades;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public boolean isDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(boolean disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public int getTempoParaEmprestimo() {
        return tempoParaEmprestimo;
    }

    public void setTempoParaEmprestimo(int tempoParaEmprestimo) {
        this.tempoParaEmprestimo = tempoParaEmprestimo;
    }
}