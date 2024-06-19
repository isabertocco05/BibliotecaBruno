package features.emprestados.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "emprestados")
public class Emprestados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int livroId;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;

    public Emprestados(int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.livroId = livroId;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public Emprestados() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivroId() {
        return livroId;
    }

    public void setLivroId(int livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }
}
