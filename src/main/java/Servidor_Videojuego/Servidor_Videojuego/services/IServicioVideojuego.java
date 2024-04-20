package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;

import java.util.List;
import java.util.Optional;

public interface IServicioVideojuego {

    Videojuego addVideojuego(Videojuego videojuego, int usuarioId);  // Método para agregar o reemplazar un videojuego
    Videojuego updateVideojuego(Videojuego videojuego, int id);  // Método para actualizar un videojuego
    boolean deleteVideojuego(int id);  // Método para eliminar un videojuego
    Optional<Videojuego> buscarVideojuegos(Integer id, String nombre);  // Método para buscar videojuegos por varios atributos
    List<Videojuego> getVideojuego(); //Listar todos los videojuegos
    List<Videojuego> getVideojuego(Double precio, Boolean multijugador); //Listar por 2 atributos

    boolean existeVideojuegoConId(int id);//Comprobar si ya existe un videojuego con el mismo ID

}

