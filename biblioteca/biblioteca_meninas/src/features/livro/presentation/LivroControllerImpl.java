package features.livro.presentation;

import features.livro.datasource.LivroDatabase;
import features.livro.modelo.Livro;
import features.emprestados.datasource.EmprestadosDatabase;

import javax.swing.*;
import java.awt.Component; // Adicione esta linha
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LivroControllerImpl implements LivroController {
    private LivroView livroView;
    private final LivroDatabase livroDatabase;
    private final EmprestadosDatabase emprestadosDatabase;

    public LivroControllerImpl(LivroDatabase livroDatabase, EmprestadosDatabase emprestadosDatabase) {
        this.livroDatabase = livroDatabase;
        this.emprestadosDatabase = emprestadosDatabase;
    }

    @Override
    public void setView(LivroView view) {
        this.livroView = view;
    }

    @Override
    public void emprestarLivro(int isbn) {
        Livro livro = livroDatabase.getLivroIsbn(isbn);
        if (livro != null) {
            if (livro.getUnidades() > 0) {
                livro.setUnidades(livro.getUnidades() - 1);
                livroDatabase.updateLivro(livro);
                emprestadosDatabase.adicionarEmprestado(isbn, LocalDate.now(), LocalDate.now().plusDays(livro.getTempoParaEmprestimo()));
                livroView.updateData();
                JOptionPane.showMessageDialog((Component) livroView, "Livro emprestado com sucesso.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog((Component) livroView, "Não há cópias disponíveis para empréstimo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog((Component) livroView, "Livro não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void adicionarLivro(String titulo, String autor, String categoria, int isbn, int unidades, int disponibilidade, int tempoParaEmprestimo) {
        if (titulo == null || titulo.isEmpty()) {
            showErrorMessage("Título é obrigatório");
            return;
        }
        if (autor == null || autor.isEmpty()) {
            showErrorMessage("Autor é obrigatório");
            return;
        }
        if (categoria == null || categoria.isEmpty()) {
            showErrorMessage("Categoria é obrigatória");
            return;
        }
        if (isbn <= 0) {
            showErrorMessage("ISBN é obrigatório e deve ser um número positivo");
            return;
        }
        if (unidades < 0) {
            showErrorMessage("Unidades é obrigatório e deve ser um número não negativo");
            return;
        }
        if (disponibilidade < 0) {
            showErrorMessage("Disponibilidade é obrigatória e deve ser um número não negativo");
            return;
        }
        if (tempoParaEmprestimo <= 0) {
            showErrorMessage("Tempo para Empréstimo é obrigatório e deve ser um número positivo");
            return;
        }

        System.out.println("Adicionando livro: " + titulo + ", " + autor + ", " + categoria + ", " + isbn + ", " + unidades + ", " + disponibilidade + ", " + tempoParaEmprestimo);

        livroDatabase.adicionarLivro(titulo, autor, categoria, isbn, unidades, disponibilidade, tempoParaEmprestimo);
    }

    @Override
    public void editarLivro(String titulo, String autor, String categoria, int isbn, int unidades, boolean disponibilidade, int tempoParaEmprestimo) {
        if (titulo == null || titulo.isEmpty()) {
            showErrorMessage("Título é obrigatório");
            return;
        }
        if (autor == null || autor.isEmpty()) {
            showErrorMessage("Autor é obrigatório");
            return;
        }
        if (categoria == null || categoria.isEmpty()) {
            showErrorMessage("Categoria é obrigatória");
            return;
        }
        if (isbn <= 0) {
            showErrorMessage("ISBN é obrigatório e deve ser um número positivo");
            return;
        }
        if (unidades < 0) {
            showErrorMessage("Unidades é obrigatório e deve ser um número não negativo");
            return;
        }
        if (tempoParaEmprestimo <= 0) {
            showErrorMessage("Tempo para Empréstimo é obrigatório e deve ser um número positivo");
            return;
        }

        System.out.println("Editando livro: " + titulo + ", " + autor + ", " + categoria + ", " + isbn + ", " + unidades + ", " + disponibilidade + ", " + tempoParaEmprestimo);

        livroDatabase.editarLivro(titulo, autor, categoria, isbn, unidades, disponibilidade, tempoParaEmprestimo);
    }

    @Override
    public void deletarLivro(int isbn) {
        livroDatabase.deletarLivro(isbn);
    }

    @Override
    public List<Livro> todosLivros() {
        return livroDatabase.todosLivros();
    }

    @Override
    public List<Livro> pesquisarLivros(String criterio, String textoPesquisa) {
        List<Livro> todosLivros = livroDatabase.todosLivros();
        return todosLivros.stream()
                .filter(livro -> {
                    switch (criterio) {
                        case "ISBN":
                            return String.valueOf(livro.getIsbn()).contains(textoPesquisa);
                        case "Nome":
                            return livro.getTitulo().contains(textoPesquisa);
                        case "Autor":
                            return livro.getAutor().contains(textoPesquisa);
                        case "Categoria":
                            return livro.getCategoria().contains(textoPesquisa);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    private void showErrorMessage(String msg) {
        if (livroView != null) {
            JOptionPane.showMessageDialog((Component) livroView, msg, "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
