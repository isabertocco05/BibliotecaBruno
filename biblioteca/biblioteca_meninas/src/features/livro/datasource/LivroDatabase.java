package features.livro.datasource;

import features.livro.modelo.Livro;

import java.util.List;

public interface LivroDatabase extends LivroSubscriber {
    void adicionarLivro(String titulo, String autor, String categoria, int isbn, int unidades, int disponibilidade, int tempoParaEmprestimo);
    void deletarLivro(int isbn);
    void editarLivro(String titulo, String autor, String categoria, int isbn, int unidades, boolean disponibilidade, int tempoParaEmprestimo);
    Livro getLivroIsbn(int isbn);
    void updateLivro(Livro livro);
    void incrementarUnidades(int isbn);
    List<Livro> todosLivros();
}