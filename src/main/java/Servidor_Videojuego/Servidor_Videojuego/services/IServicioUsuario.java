package Servidor_Videojuego.Servidor_Videojuego.services;
import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IServicioUsuario {

    // Métodos para manejar usuarios
    Usuario addUsuario(Usuario usuario); // Método para agregar un nuevo usuario
    Usuario addVideojuegoToUsuario(int usuarioId, Videojuego videojuego); // Método para añadir un videojuego a la lista de videojuegos de un usuario específico

    Usuario updateUsuario(Usuario usuario, int id); // Método para actualizar un usuario existente

    boolean deleteUsuario(int id); // Método para eliminar un usuario por su ID
    Optional<Usuario> buscarUsuario(Integer id, String nombre);  // Método para buscar videojuegos por varios atributos
    List<Usuario> getUsuarios(); // Listar todos los usuarios
    List<Usuario> getUsuarios(Double estatura, Boolean esPremium); //Listar por 2 atributos
    boolean existeUsuarioConId(int id);//Comprobar si ya existe un videojuego con el mismo ID
}
