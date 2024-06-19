package features.usuarios.presentation;

import features.usuarios.modelo.Usuario;

import java.util.List;

public interface UsuarioController {
    void setView(UsuarioView view);
    void adicionarUsuario(String nome, String email, String senha, String cargo);
    void deletarUsuario(int id);
    void editarUsuario(int id, String nome, String email, String senha, String cargo);
    List<Usuario> todosUsuarios();
}
