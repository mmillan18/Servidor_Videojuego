package Servidor_Videojuego.Servidor_Videojuego.controller;

import Servidor_Videojuego.Servidor_Videojuego.model.Usuario;
import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.services.ErrorMessage;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioUsuario;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioVideojuego;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/videojuegos")
@CrossOrigin(origins = "http://localhost:4200")

public class VideojuegoController {

    @Autowired
    private IServicioVideojuego servicioVideojuego;
    private IServicioUsuario servicioUsuario;

    //Verificación estado  --- OK

    @RequestMapping(value = "/healthcheck")

    public String healthCheck(){
        return "Service status fine!";
    }

    //  crear y asociar un videojuego a un usuario
    @PostMapping("/{usuarioId}")
    public ResponseEntity<Videojuego> crearYAsociarVideojuego(
            @PathVariable int usuarioId,
            @RequestBody Videojuego videojuego) {
        try {
            Videojuego nuevoVideojuego = servicioVideojuego.addUserToVideojuego(usuarioId, videojuego);
            return new ResponseEntity<>(nuevoVideojuego, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    //Actualizar
    @PutMapping("/{usuarioId}/{id}")
    public ResponseEntity<Videojuego> updateVideojuego(@PathVariable int usuarioId, @PathVariable int id, @RequestBody Videojuego videojuego) {
        Videojuego updatedVideojuego = servicioVideojuego.updateVideojuego(videojuego, usuarioId, id);
        return ResponseEntity.ok(updatedVideojuego);
    }


    //Eliminar videojuego

    @DeleteMapping("/{usuarioId}/{id}")
    public ResponseEntity<?> deleteVideojuego(@PathVariable int usuarioId, @PathVariable int id) {
        boolean isDeleted = servicioVideojuego.deleteVideojuego(usuarioId, id);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    //Consultar

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> getVideojuegosDeUsuario(
            @PathVariable Integer usuarioId,
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "multijugador", required = false) Boolean multijugador) {

        // Filtrar los videojuegos del usuario por ID o nombre
        List<Videojuego> videojuegos = servicioVideojuego.getVideojuegosDeUsuario(usuarioId).stream()
                .filter(v -> (id == null || v.getId() == id) && // Usar == para comparar int
                        (nombre == null || v.getNombre().equalsIgnoreCase(nombre)))
                .collect(Collectors.toList());

        if (!videojuegos.isEmpty() && (precio != null || multijugador != null)) {
            // Filtrar además por precio y si son multijugador
            videojuegos = videojuegos.stream()
                    .filter(v -> (precio == null || Double.compare(v.getPrecio(), precio) == 0) && // Usar Double.compare para comparar double
                            (multijugador == null || v.isMultijugador() == multijugador)) // Usar == para comparar boolean
                    .collect(Collectors.toList());
        }

        if (videojuegos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(videojuegos);
    }


    @GetMapping("/{usuarioId}")
    public ResponseEntity<?> getVideojuegosDeUsuario(@PathVariable int usuarioId) {
        try {
            List<Videojuego> videojuegos = servicioVideojuego.getVideojuegosDeUsuario(usuarioId);
            return ResponseEntity.ok(videojuegos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Videojuego>> getAllVideojuegos() {
        List<Videojuego> videojuegos = servicioVideojuego.getVideojuego();  // Asume que este método devuelve todos los videojuegos
        if (videojuegos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(videojuegos);
    }



    //Formato mensaje error

    private String formatMessage(BindingResult result){
        List<Map<String,String>> errores = result.getFieldErrors().stream()
                .map(err -> {
                    Map<String,String> error = new HashMap<>();
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

