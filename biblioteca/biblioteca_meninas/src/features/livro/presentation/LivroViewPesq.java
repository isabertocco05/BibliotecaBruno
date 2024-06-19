package features.livro.presentation;

import features.livro.modelo.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LivroViewPesq extends JFrame {
    public LivroViewPesq(List<Livro> livros) {
        setTitle("Resultados da Pesquisa");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Titulo", "Autor", "Categoria", "ISBN", "Unidades", "Disponibilidade", "Tempo Para Emprestimo"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Livro livro : livros) {
            Object[] rowData = {
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getCategoria(),
                    livro.getIsbn(),
                    livro.getUnidades(),
                    livro.isDisponibilidade(),
                    livro.getTempoParaEmprestimo()
            };
            tableModel.addRow(rowData);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
}