package features.emprestados.datasource;

import features.emprestados.modelo.Emprestados;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmprestadosDAO implements EmprestadosDatabase, EmprestadosSubscriber {
    private static final String DATABASE_URL = "jdbc:sqlite:biblioteca.db";
    private final List<EmprestadosListener> listeners;

    public EmprestadosDAO() {
        this(new ArrayList<>());
    }

    public EmprestadosDAO(List<EmprestadosListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void subscribe(EmprestadosListener emprestadosListener) {
        listeners.add(emprestadosListener);
    }

    private void notifyDataChanged() {
        for (EmprestadosListener listener : listeners) {
            listener.updateData();
        }
    }

    public static void criaTabelaEmprestimo() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS emprestados ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "livroId INTEGER NOT NULL,"
                + "dataEmprestimo DATE NOT NULL,"
                + "dataDevolucao DATE NOT NULL,"
                + "FOREIGN KEY (livroId) REFERENCES livros(isbn)"
                + ");";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void adicionarEmprestado(int livroId, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        String adicionarEmprestimo = "INSERT INTO emprestados (livroId, dataEmprestimo, dataDevolucao) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(adicionarEmprestimo)) {
            pstmt.setInt(1, livroId);
            pstmt.setDate(2, Date.valueOf(dataEmprestimo));
            pstmt.setDate(3, Date.valueOf(dataDevolucao));
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void devolverEmprestado(int id) {
        String deleteSQL = "DELETE FROM emprestados WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            notifyDataChanged();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Emprestados> getEmprestados() {
        List<Emprestados> emprestados = new ArrayList<>();
        String selectSQL = "SELECT * FROM emprestados";

        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int livroId = rs.getInt("livroId");
                LocalDate dataEmprestimo = rs.getDate("dataEmprestimo").toLocalDate();
                LocalDate dataDevolucao = rs.getDate("dataDevolucao").toLocalDate();

                Emprestados emprestado = new Emprestados();
                emprestado.setId(id);
                emprestado.setLivroId(livroId);
                emprestado.setDataEmprestimo(dataEmprestimo);
                emprestado.setDataDevolucao(dataDevolucao);

                emprestados.add(emprestado);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return emprestados;
    }
}
