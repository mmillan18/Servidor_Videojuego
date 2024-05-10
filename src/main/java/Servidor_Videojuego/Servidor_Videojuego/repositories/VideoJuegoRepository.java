package Servidor_Videojuego.Servidor_Videojuego.repositories;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoJuegoRepository extends JpaRepository<Videojuego, Integer> {
    List<Videojuego> findByPrecio(Double precio);
    List<Videojuego> findByMultijugador(Boolean multijugador);
    List<Videojuego> findByPrecioAndMultijugador(Double precio, Boolean multijugador);
    Optional<Videojuego> findByIdAndNombre(Integer id, String nombre);
}
