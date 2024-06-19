package features.usuarios.presentation;

import features.usuarios.datasource.UsuarioDatabase;
import features.usuarios.modelo.Usuario;

import java.util.List;

public class UsuarioControllerImpl implements UsuarioController {
    private UsuarioView usuarioView;
    private final UsuarioDatabase usuarioDatabase;

    public UsuarioControllerImpl(UsuarioDatabase usuarioDatabase) {
        this.usuarioDatabase = usuarioDatabase;
    }

    @Override
    public void setView(UsuarioView view) {
        this.usuarioView = view;
    }

    @Override
    public void adicionarUsuario(String nome, String email, String senha, String cargo) {
        if (nome == null || nome.isEmpty()) {
            showErrorMessage("Nome é obrigatório");
            return;
        }
        if (email == null || email.isEmpty()) {
            showErrorMessage("Email é obrigatório");
            return;
        }
        if (senha == null || senha.isEmpty()) {
            showErrorMessage("Senha é obrigatória");
            return;
        }
        if (cargo == null || cargo.isEmpty()) {
            showErrorMessage("Cargo é obrigatório");
            return;
        }

        usuarioDatabase.adicionarUsuario(nome, email, senha, cargo);
        usuarioView.updateData();
    }

    @Override
    public void deletarUsuario(int id) {
        usuarioDatabase.deletarUsuario(id);
        usuarioView.updateData();
    }

    @Override
    public void editarUsuario(int id, String nome, String email, String senha, String cargo) {
        if (nome == null || nome.isEmpty()) {
            showErrorMessage("Nome é obrigatório");
            return;
        }
        if (email == null || email.isEmpty()) {
            showErrorMessage("Email é obrigatório");
            return;
        }
        if (senha == null || senha.isEmpty()) {
            showErrorMessage("Senha é obrigatória");
            return;
        }
        if (cargo == null || cargo.isEmpty()) {
            showErrorMessage("Cargo é obrigatório");
            return;
        }

        usuarioDatabase.editarUsuario(id, nome, email, senha, cargo);
        usuarioView.updateData();
    }

    @Override
    public List<Usuario> todosUsuarios() {
        return usuarioDatabase.todosUsuarios();
    }

    private void showErrorMessage(String msg) {
        if (usuarioView != null) {
            usuarioView.showErrorMessage(msg);
        }
    }
}
