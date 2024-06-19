package features.emprestados.datasource;

import features.emprestados.modelo.Emprestados;

import java.time.LocalDate;
import java.util.List;

public interface EmprestadosDatabase {
    void adicionarEmprestado(int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucao);
    void devolverEmprestado(int id);
    List<Emprestados> getEmprestados();
}
