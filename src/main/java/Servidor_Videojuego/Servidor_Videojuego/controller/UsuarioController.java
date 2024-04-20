package Servidor_Videojuego.Servidor_Videojuego.controller;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.services.ErrorMessage;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioUsuario;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private IServicioUsuario servicioUsuario;

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return "Service status fine!";
    }

    @PostMapping
    public ResponseEntity<?> addUsuario(@Validated @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatMessage(result));
        }

        try {
            Usuario nuevoUsuario = servicioUsuario.addUsuario(usuario);
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{usuarioId}/videojuegos")
    public ResponseEntity<?> addVideojuegoToUsuario(@PathVariable int usuarioId, @RequestBody Videojuego videojuego) {
        try {
            Usuario usuario = servicioUsuario.addVideojuegoToUsuario(usuarioId, videojuego);
            return new ResponseEntity<>(usuario, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(@Validated @RequestBody Usuario usuario, @PathVariable int id, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(formatMessage(result));
        }

        if (usuario.getId() != id) {
            return ResponseEntity.badRequest().body("El ID del usuario no coincide con el ID en la ruta.");
        }

        if (!servicioUsuario.existeUsuarioConId(id)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Usuario updateUsuario = servicioUsuario.updateUsuario(usuario, id);
            return ResponseEntity.ok(updateUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable int id) {
        if (!servicioUsuario.deleteUsuario(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<?> getUsuarios(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "estatura", required = false) Double estatura,
            @RequestParam(value = "esPremium", required = false) Boolean esPremium) {

        if (id != null || nombre != null) {
            Optional<Usuario> resultado = servicioUsuario.buscarUsuario(id, nombre);
            return resultado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else if (estatura != null || esPremium != null) {
            List<Usuario> usuarios = servicioUsuario.getUsuarios(estatura, esPremium);
            return ResponseEntity.ok(usuarios);
        } else {
            List<Usuario> usuarios = servicioUsuario.getUsuarios();
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        }
    }


    private String formatMessage(BindingResult result){
        List<Map<String, String>> errores = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());

        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .mensajes(errores)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;
    }
}
