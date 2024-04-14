package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;

import java.util.List;
import java.util.Optional;

public interface IServicioVideojuego {

    Videojuego setVideojuego(Videojuego videojuego);  // Método para agregar o reemplazar un videojuego
    Videojuego updateVideojuego(Videojuego videojuego, int id);  // Método para actualizar un videojuego
    boolean deleteVideojuego(int id);  // Método para eliminar un videojuego
    Optional<Videojuego> buscarVideojuegos(Integer id, String nombre, Double precio);  // Método para buscar videojuegos por varios atributos

    List<Videojuego> getVideojuego();
}

