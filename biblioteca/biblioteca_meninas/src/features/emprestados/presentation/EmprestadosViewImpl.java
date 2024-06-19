package features.emprestados.presentation;

import features.emprestados.datasource.EmprestadosListener;
import features.emprestados.modelo.Emprestados;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmprestadosViewImpl extends JFrame implements EmprestadosView, EmprestadosListener {
    private DefaultTableModel tabela;
    private JTable emprestadosTable;
    private EmprestadosController emprestadosController;

    public EmprestadosViewImpl(EmprestadosController emprestadosController) {
        this.emprestadosController = emprestadosController;
        emprestadosController.setView(this);

        setTitle("Livros Emprestados");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeUI();
        updateData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel topButtonPanel = new JPanel();
        topButtonPanel.setLayout(new FlowLayout());

        JButton backButton = new JButton("← Voltar");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        topButtonPanel.add(backButton);

        add(topButtonPanel, BorderLayout.NORTH);

        tabela = new DefaultTableModel(new Object[]{"ID", "Livro ID", "Data Empréstimo", "Data Devolução"}, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 1) {
                    return Integer.class;
                }
                return String.class;
            }
        };
        emprestadosTable = new JTable(tabela);
        JScrollPane scrollPane = new JScrollPane(emprestadosTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new FlowLayout());

        JButton devolverButton = new JButton("Devolver Livro");
        devolverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                devolverLivro();
            }
        });
        bottomButtonPanel.add(devolverButton);

        add(bottomButtonPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void devolverLivro() {
        int selectedRow = emprestadosTable.getSelectedRow();
        if (selectedRow != -1) {
            int emprestimoId = (int) tabela.getValueAt(selectedRow, 0);
            int livroId = (int) tabela.getValueAt(selectedRow, 1);
            emprestadosController.devolverEmprestado(emprestimoId, livroId);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um empréstimo para devolver.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void getEmprestados(List<Emprestados> emprestimos) {
        tabela.setRowCount(0);

        for (Emprestados emprestimo : emprestimos) {
            tabela.addRow(new Object[]{
                    emprestimo.getId(),
                    emprestimo.getLivroId(),
                    emprestimo.getDataEmprestimo().toString(),
                    emprestimo.getDataDevolucao().toString()
            });
        }
    }

    @Override
    public void open() {
        setVisible(true);
        updateData();
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void updateData() {
        List<Emprestados> emprestimos = emprestadosController.getEmprestados();
        getEmprestados(emprestimos);
    }
}
