package features.usuarios.datasource;

import features.usuarios.modelo.Usuario;

import java.util.List;

public interface UsuarioDatabase {
    void adicionarUsuario(String nome, String email, String senha, String cargo);
    void deletarUsuario(int id);
    void editarUsuario(int id, String nome, String email, String senha, String cargo);
    List<Usuario> todosUsuarios();
    boolean verificarLogin(String email, String senha);
    String verificarCargo(String email);
}
