package features.emprestados.presentation;

import features.emprestados.modelo.Emprestados;

import java.time.LocalDate;
import java.util.List;

public interface EmprestadosController {
    void setView(EmprestadosView view);
    void adicionarEmprestado(int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucao);
    void devolverEmprestado(int id, int livroId);
    List<Emprestados> getEmprestados();
}
