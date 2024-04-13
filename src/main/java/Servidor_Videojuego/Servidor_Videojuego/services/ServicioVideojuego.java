package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ServicioVideojuego implements IServicioVideojuego{
    private Videojuego videojuego;

    private List<Videojuego> videojuegos = new ArrayList<>();

    @Override
    public Videojuego getVideojuego() {
        return videojuego;
    }

    @Override
    public Videojuego setVideojuego(Videojuego videojuego) {
        this.videojuegos.removeIf(v -> v.getId() == videojuego.getId());
        this.videojuegos.add(videojuego);
        return videojuego;
    }

    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int id) {

        if (id == this.videojuego.getId()) {
            this.videojuego = videojuego;
        }
        return videojuego;
    }

    @Override
    public boolean deleteVideojuego(int id) {
        if (this.videojuego != null && id == this.videojuego.getId()) {
            this.videojuego = null;
            return true;
        }
        return false;
    }

    @Override
    public Optional<Videojuego> buscarVideojuegos(Integer id, String nombre, Double precio) {
        return videojuegos.stream()
                .filter(videojuego ->
                        (id == null || videojuego.getId() == id) &&
                                (nombre == null || videojuego.getNombre().equalsIgnoreCase(nombre)) &&
                                (precio == null || videojuego.getPrecio() == precio))
                .findFirst();
    }
}