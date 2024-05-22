package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.repositories.VideoJuegoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioVideojuego implements IServicioVideojuego {

    //private final List<Videojuego> videojuegos = new ArrayList<>();
    private final IServicioUsuario servicioUsuario;

    @Autowired
    private VideoJuegoRepository videoJuegoRepository;

    @Override
    public List<Videojuego> getVideojuego() {
        return videoJuegoRepository.findAll();
    }

    public ServicioVideojuego(IServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    public Videojuego addUserToVideojuego(int usuarioId, Videojuego videojuego) {
        Usuario usuario = servicioUsuario.buscarUserId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));

        if (videoJuegoRepository.existsById(videojuego.getId())) {
            throw new IllegalArgumentException("Un videojuego con el mismo ID ya existe");
        }

        videojuego.setUsuario(usuario);
        return videoJuegoRepository.save(videojuego);
    }


    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int usuarioId, int id) {
        return videoJuegoRepository.findById(id)
                .map(existingVideojuego -> {
                    if (existingVideojuego.getUsuario().getId() != usuarioId) {
                        throw new IllegalArgumentException("Videojuego no pertenece al usuario con ID: " + usuarioId);
                    }
                    existingVideojuego.setNombre(videojuego.getNombre());
                    existingVideojuego.setPrecio(videojuego.getPrecio());
                    existingVideojuego.setMultijugador(videojuego.isMultijugador());
                    existingVideojuego.setFechaLanzamiento(videojuego.getFechaLanzamiento());
                    return videoJuegoRepository.save(existingVideojuego);
                })
                .orElseThrow(() -> new RuntimeException("Videojuego no encontrado con ID: " + id));

    }

    @Override
    public boolean deleteVideojuego(int usuarioId, int id) {
        Optional<Videojuego> videojuego = videoJuegoRepository.findById(id);
        videojuego.ifPresent(videoJuegoRepository::delete);
        return videojuego.isPresent();
    }


    @Override
    public Optional<Videojuego> buscarVideojuegos(Integer id, String nombre) {
        return videoJuegoRepository.findByIdAndNombre(id, nombre);
    }


    @Override
    public List<Videojuego> getVideojuego(Double precio, Boolean multijugador) {
        if (precio != null && multijugador != null) {
            return videoJuegoRepository.findByPrecioAndMultijugador(precio, multijugador);
        } else if (precio != null) {
            return videoJuegoRepository.findByPrecio(precio);
        } else if (multijugador != null) {
            return videoJuegoRepository.findByMultijugador(multijugador);
        } else {
            return videoJuegoRepository.findAll();
        }
    }

    @Override
    public List<Videojuego> getVideojuegosDeUsuario(int usuarioId) {
        Usuario usuario = servicioUsuario.buscarUsuario(usuarioId, null)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + usuarioId));
        return new ArrayList<>(usuario.getVideojuegos());
    }

    @Override
    public Usuario getUsuarioDeVideojuego(int videojuegoId) {
        Videojuego videojuego = videoJuegoRepository.findById(videojuegoId)
                .orElseThrow(() -> new RuntimeException("Videojuego no encontrado con ID: " + videojuegoId));
        return videojuego.getUsuario();
    }




    public boolean existeVideojuegoConId(int id) {

        return videoJuegoRepository.existsById(id);
    }



}


