package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
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

    private final List<Videojuego> videojuegos = new ArrayList<>();
    private final IServicioUsuario servicioUsuario;

    @Override
    public List<Videojuego> getVideojuego() {
        return new ArrayList<>(videojuegos);
    }

    @Override
    public Videojuego addVideojuego(Videojuego videojuego, int usuarioId) {
        Usuario usuario = servicioUsuario.buscarUsuario(usuarioId, null)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con el ID especificado no existe."));

        videojuegos.add(videojuego);
        servicioUsuario.addVideojuegoToUsuario(usuarioId, videojuego);

        return videojuego;
    }



    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int id) {
        Optional<Videojuego> existingVideojuego = videojuegos.stream()
                .filter(v -> v.getId() == id)
                .findFirst();

        if (existingVideojuego.isPresent()) {
            if (videojuego.getId() == id) {
                videojuegos.set(videojuegos.indexOf(existingVideojuego.get()), videojuego);
                return videojuego;
            }
            throw new IllegalArgumentException("El ID del videojuego no coincide con el ID en la ruta");
        }
        throw new RuntimeException("Videojuego no encontrado con ID: " + id);
    }

    @Override
    public boolean deleteVideojuego(int id) {
        return videojuegos.removeIf(v -> v.getId() == id);
    }

    @Override
    public Optional<Videojuego> buscarVideojuegos(Integer id, String nombre) {
        return videojuegos.stream()
                .filter(videojuego ->
                        (id == null || videojuego.getId() == id) &&
                                (nombre == null || videojuego.getNombre().equalsIgnoreCase(nombre)))
                .findFirst();
    }


    @Override
    public List<Videojuego> getVideojuego(Double precio, Boolean multijugador) {
        List<Videojuego> listaFiltrada = new ArrayList<>(videojuegos);

        if (precio != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(vj -> vj.getPrecio() == precio)
                    .collect(Collectors.toList());
        }
        if (multijugador != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(vj -> vj.isMultijugador() == multijugador)
                    .collect(Collectors.toList());
        }

        return listaFiltrada;
    }


    public boolean existeVideojuegoConId(int id) {

        List<Videojuego> listaVideojuegos = new ArrayList<>(videojuegos);
        return listaVideojuegos.stream().anyMatch(vj -> vj.getId() == id);
    }



}


