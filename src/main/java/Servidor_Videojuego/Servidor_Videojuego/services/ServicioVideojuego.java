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

public class ServicioVideojuego implements IServicioVideojuego {

    private List<Videojuego> videojuegos = new ArrayList<>();

    @Override
    public List<Videojuego> getVideojuego() {
        return new ArrayList<>(videojuegos);
    }

    @Override
    public Videojuego setVideojuego(Videojuego videojuego) {
        this.videojuegos.removeIf(v -> v.getId() == videojuego.getId());
        this.videojuegos.add(videojuego);
        return videojuego;
    }


    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int id) {
        int index = videojuegos.indexOf(videojuegos.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null));
        if (index != -1) {
            videojuegos.set(index, videojuego);
            return videojuego;
        }
        return null;
    }

    @Override
    public boolean deleteVideojuego(int id) {
        return videojuegos.removeIf(v -> v.getId() == id);
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


    @Override
    public List<Videojuego> getVideojuego(String nombre, Integer id) {
        List<Videojuego> listaFiltrada = new ArrayList<>(videojuegos);

        if (nombre != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(vj -> vj.getNombre().equalsIgnoreCase(nombre))
                    .collect(Collectors.toList());
        }

        if (id != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(vj -> vj.getId() == id)
                    .collect(Collectors.toList());
        }

        return listaFiltrada;
    }


}


