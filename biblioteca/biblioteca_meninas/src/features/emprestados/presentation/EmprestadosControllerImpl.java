package features.emprestados.presentation;

import features.emprestados.datasource.EmprestadosDatabase;
import features.emprestados.modelo.Emprestados;
import features.livro.datasource.LivroDatabase;

import java.time.LocalDate;
import java.util.List;

public class EmprestadosControllerImpl implements EmprestadosController {
    private EmprestadosView emprestadosView;
    private final EmprestadosDatabase emprestadosDatabase;
    private final LivroDatabase livroDatabase;

    public EmprestadosControllerImpl(EmprestadosDatabase emprestadosDatabase, LivroDatabase livroDatabase) {
        this.emprestadosDatabase = emprestadosDatabase;
        this.livroDatabase = livroDatabase;
    }

    @Override
    public void setView(EmprestadosView view) {
        this.emprestadosView = view;
    }

    @Override
    public void adicionarEmprestado(int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        if (livroId <= 0) {
            showErrorMessage("ID do livro é obrigatório e deve ser maior que zero");
        } else if (dataEmprestimo == null) {
            showErrorMessage("Data de empréstimo é obrigatória");
        } else if (dataDevolucao == null) {
            showErrorMessage("Data de devolução é obrigatória");
        } else {
            emprestadosDatabase.adicionarEmprestado(livroId, dataEmprestimo, dataDevolucao);
            emprestadosView.updateData();
        }
    }

    @Override
    public void devolverEmprestado(int id, int livroId) {
        if (id <= 0) {
            showErrorMessage("ID do empréstimo é obrigatório e deve ser maior que zero");
        } else {
            emprestadosDatabase.devolverEmprestado(id);
            livroDatabase.incrementarUnidades(livroId);
            emprestadosView.updateData();
        }
    }

    @Override
    public List<Emprestados> getEmprestados() {
        return emprestadosDatabase.getEmprestados();
    }

    private void showErrorMessage(String msg) {
        if (emprestadosView != null) {
            emprestadosView.showErrorMessage(msg);
        }
    }
}
