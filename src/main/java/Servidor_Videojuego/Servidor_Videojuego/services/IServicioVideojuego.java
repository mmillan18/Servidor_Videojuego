package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;

import java.util.List;
import java.util.Optional;

public interface IServicioVideojuego {


    Videojuego updateVideojuego(Videojuego videojuego, int usuarioId, int id);
    boolean deleteVideojuego(int usuarioId, int id);
    Optional<Videojuego> buscarVideojuegos(Integer id, String nombre);
    List<Videojuego> getVideojuegosDeUsuario(int usuarioId);
    Usuario getUsuarioDeVideojuego(int id);
    List<Videojuego> getVideojuego();
    List<Videojuego> getVideojuego(Double precio, Boolean multijugador);
    boolean existeVideojuegoConId(int id);

}

