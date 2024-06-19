package features.livro.presentation;

import features.livro.modelo.Livro;

import java.util.List;

public interface LivroController {
    void setView(LivroView view);
    void adicionarLivro(String titulo, String autor, String categoria, int isbn, int unidades, int disponibilidade, int tempoParaEmprestimo);
    void deletarLivro(int isbn);
    void editarLivro(String titulo, String autor, String categoria, int isbn, int unidades, boolean disponibilidade, int tempoParaEmprestimo);
    void emprestarLivro(int isbn);
    List<Livro> todosLivros();
    List<Livro> pesquisarLivros(String criterio, String textoPesquisa);
}