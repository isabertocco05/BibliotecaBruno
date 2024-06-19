package features.usuarios.presentation;

import features.usuarios.datasource.UsuarioDAO;
import features.usuarios.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UsuarioViewImpl extends JFrame implements UsuarioView {
    private DefaultTableModel tabelaModelo;
    private JTable tabelaUsuarios;
    private UsuarioController usuarioController;

    public UsuarioViewImpl(UsuarioDAO usuarioDatabase, UsuarioController usuarioController) {
        this.usuarioController = usuarioController;
        usuarioController.setView(this);

        setTitle("Gerenciamento de Usuários");
        setSize(800, 400);
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

        tabelaModelo = new DefaultTableModel(new Object[]{"ID", "Nome", "Email", "Cargo"}, 0);
        tabelaUsuarios = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new FlowLayout());

        JButton addButton = new JButton("Adicionar Usuário");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarUsuario();
            }
        });
        bottomButtonPanel.add(addButton);

        JButton editButton = new JButton("Editar Usuário");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarUsuario();
            }
        });
        bottomButtonPanel.add(editButton);

        JButton deleteButton = new JButton("Deletar Usuário");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarUsuario();
            }
        });
        bottomButtonPanel.add(deleteButton);

        add(bottomButtonPanel, BorderLayout.SOUTH);
    }

    private void adicionarUsuario() {
        String nome = JOptionPane.showInputDialog(this, "Nome:");
        String email = JOptionPane.showInputDialog(this, "Email:");
        String senha = JOptionPane.showInputDialog(this, "Senha:");
        String cargo = JOptionPane.showInputDialog(this, "Cargo:");

        if (nome == null || nome.isEmpty() ||
                email == null || email.isEmpty() ||
                senha == null || senha.isEmpty() ||
                cargo == null || cargo.isEmpty()) {
            showErrorMessage("Todos os campos são obrigatórios.");
            return;
        }

        usuarioController.adicionarUsuario(nome, email, senha, cargo);
    }

    private void editarUsuario() {
        int rowIndex = tabelaUsuarios.getSelectedRow();
        if (rowIndex == -1) {
            showErrorMessage("Selecione um usuário para editar.");
            return;
        }

        int id = (int) tabelaModelo.getValueAt(rowIndex, 0);
        String nome = (String) tabelaModelo.getValueAt(rowIndex, 1);
        String email = (String) tabelaModelo.getValueAt(rowIndex, 2);
        String cargo = (String) tabelaModelo.getValueAt(rowIndex, 3);

        String novoNome = JOptionPane.showInputDialog(this, "Nome:", nome);
        String novoEmail = JOptionPane.showInputDialog(this, "Email:", email);
        String novaSenha = JOptionPane.showInputDialog(this, "Senha:");
        String novoCargo = JOptionPane.showInputDialog(this, "Cargo:", cargo);

        if (novoNome == null || novoNome.isEmpty() ||
                novoEmail == null || novoEmail.isEmpty() ||
                novaSenha == null || novaSenha.isEmpty() ||
                novoCargo == null || novoCargo.isEmpty()) {
            showErrorMessage("Todos os campos são obrigatórios.");
            return;
        }

        usuarioController.editarUsuario(id, novoNome, novoEmail, novaSenha, novoCargo);
    }

    private void deletarUsuario() {
        int rowIndex = tabelaUsuarios.getSelectedRow();
        if (rowIndex == -1) {
            showErrorMessage("Selecione um usuário para deletar.");
            return;
        }

        int id = (int) tabelaModelo.getValueAt(rowIndex, 0);
        usuarioController.deletarUsuario(id);
    }

    @Override
    public void updateData() {
        List<Usuario> usuarios = usuarioController.todosUsuarios();
        tabelaModelo.setRowCount(0);

        for (Usuario usuario : usuarios) {
            tabelaModelo.addRow(new Object[]{
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCargo()
            });
        }
    }

    @Override
    public void open() {
        setVisible(true);
    }

    @Override
    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }
}
