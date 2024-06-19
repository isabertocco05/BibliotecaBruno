package features.livro.datasource;

import features.livro.modelo.Livro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO implements LivroDatabase {
    private static final String DATABASE_URL = "jdbc:sqlite:biblioteca.db";
    private final List<LivroListener> listeners;

    public LivroDAO() {
        this(new ArrayList<>());
    }

    public LivroDAO(List<LivroListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void subscribe(LivroListener livroListener) {
        listeners.add(livroListener);
    }

    private void notifyDataChanged() {
        for (LivroListener listener : listeners) {
            listener.updateData();
        }
    }

    public static void criaTabelaLivros() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS livros ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "titulo TEXT NOT NULL,"
                + "autor TEXT NOT NULL,"
                + "categoria TEXT NOT NULL,"
                + "isbn INTEGER NOT NULL,"
                + "unidades INTEGER NOT NULL,"
                + "disponibilidade BOOLEAN NOT NULL DEFAULT TRUE,"
                + "tempoParaEmprestimo INTEGER NOT NULL"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void adicionarLivro(String titulo, String autor, String categoria, int isbn, int unidades, int disponibilidade, int tempoParaEmprestimo) {
        String adicionarLivro = "INSERT INTO livros (titulo, autor, categoria, isbn, unidades, disponibilidade, tempoParaEmprestimo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(adicionarLivro)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, categoria);
            pstmt.setInt(4, isbn);
            pstmt.setInt(5, unidades);
            pstmt.setInt(6, disponibilidade);
            pstmt.setInt(7, tempoParaEmprestimo);
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void deletarLivro(int isbn) {
        String deletarLivro = "DELETE FROM livros WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(deletarLivro)) {
            pstmt.setInt(1, isbn);
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void editarLivro(String titulo, String autor, String categoria, int isbn, int unidades, boolean disponibilidade, int tempoParaEmprestimo) {
        String editarLivro = "UPDATE livros SET titulo = ?, autor = ?, categoria = ?, unidades = ?, disponibilidade = ?, tempoParaEmprestimo = ? WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(editarLivro)) {
            pstmt.setString(1, titulo);
            pstmt.setString(2, autor);
            pstmt.setString(3, categoria);
            pstmt.setInt(4, unidades);
            pstmt.setBoolean(5, disponibilidade);
            pstmt.setInt(6, tempoParaEmprestimo);
            pstmt.setInt(7, isbn);
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Livro> todosLivros() {
        List<Livro> livros = new ArrayList<>();
        String selectSQL = "SELECT * FROM livros";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                Livro livro = new Livro(
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("categoria"),
                        rs.getInt("isbn"),
                        rs.getInt("unidades"),
                        rs.getBoolean("disponibilidade"),
                        rs.getInt("tempoParaEmprestimo")
                );
                livros.add(livro);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return livros;
    }

    @Override
    public Livro getLivroIsbn(int isbn) {
        String selectSQL = "SELECT * FROM livros WHERE isbn = ?";
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(selectSQL)) {
            pstmt.setInt(1, isbn);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Livro(
                            rs.getString("titulo"),
                            rs.getString("autor"),
                            rs.getString("categoria"),
                            rs.getInt("isbn"),
                            rs.getInt("unidades"),
                            rs.getBoolean("disponibilidade"),
                            rs.getInt("tempoParaEmprestimo")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void updateLivro(Livro livro) {
        String updateSQL = "UPDATE livros SET unidades = ? WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setInt(1, livro.getUnidades());
            pstmt.setInt(2, livro.getIsbn());
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void incrementarUnidades(int isbn) {
        String updateSQL = "UPDATE livros SET unidades = unidades + 1 WHERE isbn = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setInt(1, isbn);
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}