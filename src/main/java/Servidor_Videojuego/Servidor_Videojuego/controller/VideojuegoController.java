package Servidor_Videojuego.Servidor_Videojuego.controller;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import Servidor_Videojuego.Servidor_Videojuego.services.ErrorMessage;
import Servidor_Videojuego.Servidor_Videojuego.services.IServicioVideojuego;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/videojuego")

public class VideojuegoController {

    private static int valCodigo = 0;

    @Autowired
    private IServicioVideojuego servicioVideojuego;


    @RequestMapping(value = "/healthcheck")
    public String healthCheck(){
        return "Service status fine!";
    }

    // Crear

    @PostMapping
    public ResponseEntity<Videojuego> setVideojuego(@RequestBody Videojuego videojuego) {
        Videojuego createdVideojuego = servicioVideojuego.setVideojuego(videojuego);
        return ResponseEntity.ok(createdVideojuego);
    }


    // Leer
    public ResponseEntity<Videojuego> getVideojuego(@PathVariable int id) {
    Videojuego videojuego = servicioVideojuego.getVideojuego(id);
    if (videojuego != null) {
        return ResponseEntity.ok(videojuego);
    }
    return ResponseEntity.notFound().build();
}


    @PutMapping("/{id}")
    public ResponseEntity<Videojuego> updateVideojuego(@RequestBody Videojuego videojuego, @PathVariable int id) {
        Videojuego updatedVideojuego = servicioVideojuego.updateVideojuego(videojuego, id);
        if (updatedVideojuego != null) {
            return ResponseEntity.ok(updatedVideojuego);
        }
        return ResponseEntity.notFound().build();
    }

    //////ELIMINAR

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteVideojuego(@PathVariable int id) {
        boolean isDeleted = servicioVideojuego.deleteVideojuego(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //Buscar por 3 Variantes

    @GetMapping("/buscar")
    public ResponseEntity<Videojuego> buscarVideojuego(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) Double precio) {

        Optional<Videojuego> resultado = servicioVideojuego.buscarVideojuegos(id, nombre, precio);

        return resultado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

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

