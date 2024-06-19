package telas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import features.usuarios.datasource.UsuarioDAO;

public class LoginView extends JFrame {
    private JLabel usuarioLabel;
    private JTextField usuarioField;
    private JLabel senhaLabel;
    private JPasswordField senhaField;
    private JButton loginButton;
    private UsuarioDAO usuarioDAO;

    public LoginView() {
        setTitle("Tela de login");
        setSize(400, 300);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);
        usuarioDAO = new UsuarioDAO();
        UsuarioDAO.createDatabase();
        usuarioLabel = new JLabel("Usuário:");
        usuarioField = new JTextField();
        usuarioLabel.setBounds(100, 25, 200, 30);
        usuarioField.setBounds(100, 60, 200, 30);

        add(usuarioLabel);
        add(usuarioField);


        senhaLabel = new JLabel("Senha:");
        senhaField = new JPasswordField();
        senhaLabel.setBounds(100, 95, 200, 30);
        senhaField.setBounds(100, 130, 200, 30);
        add(senhaLabel);
        add(senhaField);

        loginButton = new JButton("Entrar");
        loginButton.setBounds(100, 185, 200, 50);
        loginButton.setBackground(Color.MAGENTA);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = usuarioField.getText();
                String senha = new String(senhaField.getPassword());
                if (email.isEmpty() || senha.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.");
                } else {
                    if (usuarioDAO.verificarLogin(email, senha)) {
                        String cargo = usuarioDAO.verificarCargo(email);
                        System.out.println(cargo);
                        if ("admin".equalsIgnoreCase(cargo)) {
                            JOptionPane.showMessageDialog(null, "Login realizado como administrador!");
                            MenuView menuView = new MenuView("admin");
                            menuView.setVisible(true);
                        } else if ("funcionario".equalsIgnoreCase(cargo)) {
                            JOptionPane.showMessageDialog(null, "Login realizado como funcionário!");
                            MenuView menuView = new MenuView("funcionario");
                            menuView.setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Cargo desconhecido.");
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "Email ou senha incorretos.");
                    }
                }
            }
        });

        setVisible(true);
    }
}
