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
public class ServicioVideojuego implements IServicioVideojuego {

    private final List<Videojuego> videojuegos = new ArrayList<>();
    private final IServicioUsuario servicioUsuario;

    @Override
    public List<Videojuego> getVideojuego() {
        return new ArrayList<>(videojuegos);
    }

    public ServicioVideojuego(IServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int usuarioId, int id) {
        Usuario usuario = servicioUsuario.buscarUsuario(usuarioId, null)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        Videojuego existingVideojuego = usuario.getVideojuegos().stream()
                .filter(v -> v.getId() == id && v.getUsuarioId() == usuarioId)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Videojuego no encontrado con ID: " + id + " para este usuario"));

        existingVideojuego.setNombre(videojuego.getNombre());
        existingVideojuego.setPrecio(videojuego.getPrecio());
        existingVideojuego.setMultijugador(videojuego.isMultijugador());
        existingVideojuego.setFechaLanzamiento(videojuego.getFechaLanzamiento());

        return existingVideojuego;
    }

    @Override
    public boolean deleteVideojuego(int usuarioId, int id) {
        Usuario usuario = servicioUsuario.buscarUsuario(usuarioId, null)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        return usuario.getVideojuegos().removeIf(v -> v.getId() == id && v.getUsuarioId() == usuarioId);
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

    @Override
    public List<Videojuego> getVideojuegosDeUsuario(int usuarioId) {
        Usuario usuario = servicioUsuario.buscarUsuario(usuarioId, null)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        return usuario.getVideojuegos();
    }

    @Override
    public Usuario getUsuarioDeVideojuego(int videojuegoId) {
        return videojuegos.stream()
                .filter(v -> v.getId() == videojuegoId)
                .findFirst()
                .map(v -> servicioUsuario.buscarUsuario(v.getId(), null).orElse(null))
                .orElseThrow(() -> new RuntimeException("No se encontró un usuario para el videojuego con ID: " + videojuegoId));
    }




    public boolean existeVideojuegoConId(int id) {

        List<Videojuego> listaVideojuegos = new ArrayList<>(videojuegos);
        return listaVideojuegos.stream().anyMatch(vj -> vj.getId() == id);
    }



}


