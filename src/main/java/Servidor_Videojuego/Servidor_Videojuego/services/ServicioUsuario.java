package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")

public class ServicioUsuario implements IServicioUsuario {

    private List<Usuario> usuarios = new ArrayList<>();
    private IServicioVideojuego servicioVideojuego;

    public ServicioUsuario(IServicioVideojuego servicioVideojuego) {
        this.servicioVideojuego = servicioVideojuego;
    }

    @Override
    public Usuario addUsuario(Usuario usuario) {
        if (existeUsuarioConId(usuario.getId())) {
            throw new IllegalArgumentException("Un usuario con el mismo ID ya existe");
        }
        usuarios.add(usuario);
        return usuario;
    }

    @Override
    public Usuario updateUsuario(Usuario usuario, int id) {
        Optional<Usuario> existingUsuarioOpt = usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        if (!existingUsuarioOpt.isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        Usuario existingUsuario = existingUsuarioOpt.get();
        // Preservar la lista original de videojuegos antes de actualizar
        List<Videojuego> originalVideojuegos = existingUsuario.getVideojuegos();
        // Actualizar los atributos del usuario
        existingUsuario.setNombre(usuario.getNombre());
        existingUsuario.setEstatura(usuario.getEstatura());
        existingUsuario.setFechaNacimiento(usuario.getFechaNacimiento());
        existingUsuario.setEsPremium(usuario.isEsPremium());
        // Reestablecer la lista original de videojuegos
        existingUsuario.setVideojuegos(originalVideojuegos);
        return existingUsuario;
    }

    @Override
    public boolean deleteUsuario(int id) {
        return usuarios.removeIf(u -> u.getId() == id);
    }

    @Override
    public Optional<Usuario> buscarUsuario(Integer id, String nombre) {
        return usuarios.stream()
                .filter(usuario -> (id == null || usuario.getId() == id) &&
                        (nombre == null || usuario.getNombre().equalsIgnoreCase(nombre)))
                .findFirst();
    }

    @Override
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public List<Usuario> getUsuarios(Double estatura, Boolean esPremium) {
        List<Usuario> listaFiltrada = new ArrayList<>(usuarios);

        if (estatura != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(us -> us.getEstatura() == estatura)
                    .collect(Collectors.toList());
        }
        if (esPremium != null) {
            listaFiltrada = listaFiltrada.stream()
                    .filter(us -> us.isEsPremium() == esPremium)
                    .collect(Collectors.toList());
        }
        return listaFiltrada;
    }

    @Override
    public boolean existeUsuarioConId(int id) {
        return usuarios.stream().anyMatch(usuario -> usuario.getId() == id);
    }
}


