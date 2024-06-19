package di;

import features.emprestados.datasource.EmprestadosDAO;
import features.emprestados.datasource.EmprestadosDatabase;
import features.emprestados.presentation.EmprestadosController;
import features.emprestados.presentation.EmprestadosControllerImpl;
import features.emprestados.presentation.EmprestadosView;
import features.emprestados.presentation.EmprestadosViewImpl;
import features.livro.datasource.LivroDAO;
import features.livro.datasource.LivroDatabase;
import features.livro.presentation.LivroController;
import features.livro.presentation.LivroControllerImpl;
import features.livro.presentation.LivroViewImpl;
import features.usuarios.datasource.UsuarioDAO;
import features.usuarios.datasource.UsuarioDatabase;
import features.usuarios.presentation.UsuarioController;
import features.usuarios.presentation.UsuarioControllerImpl;
import features.usuarios.presentation.UsuarioView;
import features.usuarios.presentation.UsuarioViewImpl;

public class ServiceLocator {
    private static ServiceLocator instance;

    private UsuarioDatabase usuarioDatabase;
    private UsuarioController usuarioController;
    private UsuarioView usuarioView;

    private EmprestadosDatabase emprestadosDatabase;
    private EmprestadosController emprestadosController;
    private EmprestadosView emprestadosView;

    private LivroDatabase livroDatabase;
    private LivroController livroController;
    private LivroViewImpl livroView;

    private ServiceLocator() {
        UsuarioDAO.createDatabase();

        usuarioDatabase = new UsuarioDAO();
        usuarioController = new UsuarioControllerImpl(usuarioDatabase);
        usuarioView = new UsuarioViewImpl((UsuarioDAO) usuarioDatabase, usuarioController);
        usuarioController.setView(usuarioView);

        livroDatabase = new LivroDAO();
        emprestadosDatabase = new EmprestadosDAO();
        emprestadosController = new EmprestadosControllerImpl(emprestadosDatabase, livroDatabase);
        emprestadosView = new EmprestadosViewImpl(emprestadosController);
        emprestadosController.setView(emprestadosView);
        EmprestadosDAO.criaTabelaEmprestimo();

        livroController = new LivroControllerImpl(livroDatabase, emprestadosDatabase);
        livroView = new LivroViewImpl(livroDatabase, livroController);
        livroController.setView(livroView);
        LivroDAO.criaTabelaLivros();
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }

    public UsuarioView getUsuarioView() {
        return usuarioView;
    }

    public EmprestadosView getEmprestadosView() {
        return emprestadosView;
    }

    public LivroViewImpl getLivroView() {
        return livroView;
    }
}
