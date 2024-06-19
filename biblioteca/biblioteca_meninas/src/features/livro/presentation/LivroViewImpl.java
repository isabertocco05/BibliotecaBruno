package features.livro.presentation;

import features.livro.datasource.LivroListener;
import features.livro.datasource.LivroSubscriber;
import features.livro.modelo.Livro;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LivroViewImpl extends JFrame implements LivroView, LivroListener {
    private JTextField campoPesquisa;
    private JComboBox<String> criterioPesquisa;
    private JButton botaoPesquisar;
    private JButton botaoAdicionar;
    private JButton botaoDeletar;
    private JButton botaoEditar;
    private JButton botaoEmprestar;
    private JTable tabelaLivros;
    private DefaultTableModel tabelaModelo;
    private final LivroController livroController;

    public LivroViewImpl(LivroSubscriber livroSubscriber, LivroController livroController) {
        setTitle("Gerenciamento de Livros");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        livroSubscriber.subscribe(this);
        this.livroController = livroController;
        livroController.setView(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout());
        JButton backButton = new JButton("← Voltar");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        searchPanel.add(backButton);
        campoPesquisa = new JTextField(20);
        searchPanel.add(campoPesquisa);

        criterioPesquisa = new JComboBox<>(new String[]{"ISBN", "Nome", "Autor", "Categoria"});
        searchPanel.add(criterioPesquisa);

        botaoPesquisar = new JButton("🔍");
        botaoPesquisar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarLivro();
            }
        });
        searchPanel.add(botaoPesquisar);

        add(searchPanel, BorderLayout.NORTH);

        tabelaModelo = new DefaultTableModel(new Object[]{"Título", "Autor", "Categoria", "ISBN", "Unidades", "Disponibilidade", "Tempo para Empréstimo"}, 0);
        tabelaLivros = new JTable(tabelaModelo);
        JScrollPane scrollPane = new JScrollPane(tabelaLivros);
        add(scrollPane, BorderLayout.CENTER);

        JPanel bottomButtonPanel = new JPanel();
        bottomButtonPanel.setLayout(new FlowLayout());

        botaoAdicionar = new JButton("Adicionar Livro");
        botaoAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarLivro();
            }
        });
        bottomButtonPanel.add(botaoAdicionar);

        botaoDeletar = new JButton("Deletar Livro");
        botaoDeletar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletarLivro();
            }
        });
        bottomButtonPanel.add(botaoDeletar);

        botaoEditar = new JButton("Editar Livro");
        botaoEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarLivro();
            }
        });
        bottomButtonPanel.add(botaoEditar);

        botaoEmprestar = new JButton("Emprestar Livro");
        botaoEmprestar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emprestarLivro();
            }
        });
        bottomButtonPanel.add(botaoEmprestar);

        add(bottomButtonPanel, BorderLayout.SOUTH);
    }

    private void emprestarLivro() {
        int selectedRow = tabelaLivros.getSelectedRow();
        if (selectedRow != -1) {
            int isbn = (int) tabelaModelo.getValueAt(selectedRow, 3);
            livroController.emprestarLivro(isbn);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um livro para emprestar.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void pesquisarLivro() {
        String textoPesquisa = campoPesquisa.getText();
        String criterio = (String) criterioPesquisa.getSelectedItem();
        List<Livro> livros = livroController.pesquisarLivros(criterio, textoPesquisa);

        atualizarTabela(livros);
    }

    private void adicionarLivro() {
        String titulo = JOptionPane.showInputDialog(this, "Título:");
        String autor = JOptionPane.showInputDialog(this, "Autor:");
        String categoria = JOptionPane.showInputDialog(this, "Categoria:");
        String isbnStr = JOptionPane.showInputDialog(this, "ISBN:");
        String unidadesStr = JOptionPane.showInputDialog(this, "Unidades:");
        String disponibilidadeStr = JOptionPane.showInputDialog(this, "Disponibilidade:");
        String tempoParaEmprestimoStr = JOptionPane.showInputDialog(this, "Tempo para Empréstimo:");

        if (titulo == null || titulo.isEmpty() ||
                autor == null || autor.isEmpty() ||
                categoria == null || categoria.isEmpty() ||
                isbnStr == null || isbnStr.isEmpty() ||
                unidadesStr == null || unidadesStr.isEmpty() ||
                disponibilidadeStr == null || disponibilidadeStr.isEmpty() ||
                tempoParaEmprestimoStr == null || tempoParaEmprestimoStr.isEmpty()) {
            showErrorMessage("Todos os campos são obrigatórios.");
            return;
        }

        try {
            int isbn = Integer.parseInt(isbnStr);

            // Verifica se o ISBN já existe na tabela de livros
            boolean isbnJaExiste = verificarISBNExistente(isbn);
            if (isbnJaExiste) {
                showErrorMessage("Já existe um livro com o ISBN informado.");
                return;
            }

            int unidades = Integer.parseInt(unidadesStr);
            int disponibilidade = Integer.parseInt(disponibilidadeStr);
            int tempoParaEmprestimo = Integer.parseInt(tempoParaEmprestimoStr);

            livroController.adicionarLivro(titulo, autor, categoria, isbn, unidades, disponibilidade, tempoParaEmprestimo);
        } catch (NumberFormatException e) {
            showErrorMessage("ISBN, Unidades, Disponibilidade e Tempo para Empréstimo devem ser números inteiros.");
        }
    }

    private boolean verificarISBNExistente(int isbn) {
        // Obtém a lista de livros atualmente na tabela
        List<Livro> livros = livroController.todosLivros();

        // Verifica se algum livro na lista possui o mesmo ISBN
        for (Livro livro : livros) {
            if (livro.getIsbn() == isbn) {
                return true; // ISBN encontrado, já existe um livro com este ISBN
            }
        }
        return false; // ISBN não encontrado, pode cadastrar o livro
    }

    private void editarLivro() {
        int rowIndex = tabelaLivros.getSelectedRow();
        if (rowIndex == -1) {
            showErrorMessage("Selecione um livro para editar.");
            return;
        }

        // Obtém os dados do livro da linha selecionada na tabela
        String titulo = (String) tabelaModelo.getValueAt(rowIndex, 0);
        String autor = (String) tabelaModelo.getValueAt(rowIndex, 1);
        String categoria = (String) tabelaModelo.getValueAt(rowIndex, 2);
        int isbn = (int) tabelaModelo.getValueAt(rowIndex, 3);
        int unidades = (int) tabelaModelo.getValueAt(rowIndex, 4);
        boolean disponibilidade = ((String) tabelaModelo.getValueAt(rowIndex, 5)).equalsIgnoreCase("Sim");
        int tempoParaEmprestimo = (int) tabelaModelo.getValueAt(rowIndex, 6);

        // Exibe um formulário para editar os dados do livro
        String novoTitulo = JOptionPane.showInputDialog(this, "Título:", titulo);
        String novoAutor = JOptionPane.showInputDialog(this, "Autor:", autor);
        String novaCategoria = JOptionPane.showInputDialog(this, "Categoria:", categoria);
        String novasUnidades = JOptionPane.showInputDialog(this, "Unidades:", unidades);
        String novaDisponibilidade = JOptionPane.showInputDialog(this, "Disponibilidade:", disponibilidade ? "Sim" : "Não");
        String novoTempoParaEmprestimo = JOptionPane.showInputDialog(this, "Tempo para Empréstimo:", tempoParaEmprestimo);

        if (novoTitulo == null || novoTitulo.isEmpty() ||
                novoAutor == null || novoAutor.isEmpty() ||
                novaCategoria == null || novaCategoria.isEmpty() ||
                novasUnidades == null || novasUnidades.isEmpty() ||
                novaDisponibilidade == null || novaDisponibilidade.isEmpty() ||
                novoTempoParaEmprestimo == null || novoTempoParaEmprestimo.isEmpty()) {
            showErrorMessage("Todos os campos são obrigatórios.");
            return;
        }

        try {
            int novasUnidadesInt = Integer.parseInt(novasUnidades);
            boolean novaDisponibilidadeBool = novaDisponibilidade.equalsIgnoreCase("Sim");
            int novoTempoParaEmprestimoInt = Integer.parseInt(novoTempoParaEmprestimo);

            // Atualiza os dados do livro através do controller
            livroController.editarLivro(novoTitulo, novoAutor, novaCategoria, isbn, novasUnidadesInt, novaDisponibilidadeBool, novoTempoParaEmprestimoInt);
            List<Livro> livros = livroController.pesquisarLivros("ISBN", "");
            atualizarTabela(livros);
        } catch (NumberFormatException e) {
            showErrorMessage("Unidades e Tempo para Empréstimo devem ser números inteiros.");
        }
    }

    private void deletarLivro() {
        int rowIndex = tabelaLivros.getSelectedRow();
        if (rowIndex == -1) {
            showErrorMessage("Selecione um livro para deletar.");
            return;
        }

        int isbn = (int) tabelaModelo.getValueAt(rowIndex, 3); // ISBN está na coluna 3

        livroController.deletarLivro(isbn);
        List<Livro> livros = livroController.pesquisarLivros("ISBN", "");
        atualizarTabela(livros);
    }

    private void atualizarTabela(List<Livro> livros) {
        tabelaModelo.setRowCount(0); // Limpa a tabela

        for (Livro livro : livros) {
            tabelaModelo.addRow(new Object[]{
                    livro.getTitulo(),
                    livro.getAutor(),
                    livro.getCategoria(),
                    livro.getIsbn(),
                    livro.getUnidades(),
                    livro.isDisponibilidade() ? "Sim" : "Não",
                    livro.getTempoParaEmprestimo()
            });
        }
    }

    public void configurarVisibilidadeBotoes(String cargo) {
        if ("funcionario".equalsIgnoreCase(cargo)) {
            // Esconder os botões de adicionar, deletar e editar
            botaoAdicionar.setVisible(false);
            botaoDeletar.setVisible(false);
            botaoEditar.setVisible(false);
        } else {
            // Mostrar os botões de adicionar, deletar e editar
            botaoAdicionar.setVisible(true);
            botaoDeletar.setVisible(true);
            botaoEditar.setVisible(true);
        }
    }

    @Override
    public void updateData() {
        List<Livro> livros = livroController.todosLivros();
        atualizarTabela(livros);
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
