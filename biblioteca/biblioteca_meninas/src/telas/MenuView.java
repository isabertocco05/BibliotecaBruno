package telas;

import di.ServiceLocator;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class MenuView extends JFrame {
    private JButton usuarioTela;
    private JButton livrosTela;
    private JButton emprestimoTela;

    public MenuView(String cargo) {
        setTitle("Menu");
        setSize(400, 300);
        setResizable(false);
        setLayout(null);
        setLocationRelativeTo(null);

        if (Objects.equals(cargo, "admin")) {
            usuarioTela = new JButton("Usuários");
            usuarioTela.setBounds(100, 25, 200, 50);
            add(usuarioTela);

            livrosTela = new JButton("Livros");
            livrosTela.setBounds(100, 100, 200, 50);
            add(livrosTela);

            emprestimoTela = new JButton("Empréstimos");
            emprestimoTela.setBounds(100, 175, 200, 50);
            add(emprestimoTela);

            setVisible(true);

            usuarioTela.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        ServiceLocator.getInstance().getUsuarioView().open();
                    });
                }
            });

            livrosTela.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        ServiceLocator.getInstance().getLivroView().open();
                    });
                }
            });

            emprestimoTela.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        ServiceLocator.getInstance().getEmprestadosView().open();
                    });
                }
            });

        } else if (Objects.equals(cargo, "funcionario")) {
            livrosTela = new JButton("Livros");
            livrosTela.setBounds(100, 75, 200, 50);
            add(livrosTela);

            emprestimoTela = new JButton("Empréstimos");
            emprestimoTela.setBounds(100, 175, 200, 50);
            add(emprestimoTela);

            setVisible(true);

            livrosTela.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        ServiceLocator.getInstance().getLivroView().open();
                    });
                }
            });
            emprestimoTela.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        ServiceLocator.getInstance().getEmprestadosView().open();
                    });
                }
            });
        }
    }
}
