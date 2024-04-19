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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/videojuego")
@CrossOrigin(origins = "http://localhost:4200")

public class VideojuegoController {

    @Autowired
    private IServicioVideojuego servicioVideojuego;

    //Verificación estado  --- OK

    @RequestMapping(value = "/healthcheck")

    public String healthCheck(){
        return "Service status fine!";
    }

    // Insertar nuevo videojuego  --- OK

    @PostMapping
    public ResponseEntity<?> addVideojuego(@RequestBody Videojuego videojuego) {
        if (videojuego.getId() == 0 || videojuego.getNombre() == null || videojuego.getNombre().isEmpty()
                || videojuego.getPrecio() == 0.0 || videojuego.getFechaLanzamiento() == null) {
            String errorMessage = "Todos los campos son obligatorios.";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (servicioVideojuego.existeVideojuegoConId(videojuego.getId())) {
            String errorMessage = "Ya existe un videojuego con el mismo ID.";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        Videojuego createdVideojuego = servicioVideojuego.addVideojuego(videojuego);
        return ResponseEntity.ok(createdVideojuego);
    }





    //Actualizar videojuego --- OK

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVideojuego(@RequestBody Videojuego videojuego, @PathVariable int id) {
        if (videojuego.getNombre() == null || videojuego.getNombre().isEmpty() || videojuego.getPrecio() == 0.0 || videojuego.getFechaLanzamiento() == null) {
            String errorMessage = "Por favor ingresar todos los campos para actualizar";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (videojuego.getId() != id) {
            String errorMessage = "El ID del videojuego no coincide con el ID de la ruta";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        if (!servicioVideojuego.existeVideojuegoConId(id)) {
            String errorMessage = "No existe un videojuego con este ID para actualizar";
            return ResponseEntity.notFound().build();
        }

        Videojuego updatedVideojuego = servicioVideojuego.updateVideojuego(videojuego, id);
        return ResponseEntity.ok(updatedVideojuego);
    }


    //Eliminar videojuego

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteVideojuego(@PathVariable int id) {
        if (!servicioVideojuego.existeVideojuegoConId(id)) {
            String errorMessage = "No existe un videojuego con este ID";
            return ResponseEntity.badRequest().body(errorMessage);
        }

        boolean isDeleted = servicioVideojuego.deleteVideojuego(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }


    //Consultar

    @GetMapping
    public ResponseEntity<?> getVideojuego(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "multijugador", required = false) Boolean multijugador) {

        if (id != null || nombre != null) {
            Optional<Videojuego> resultado = servicioVideojuego.buscarVideojuegos(id, nombre, precio);
            return resultado
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else if (precio != null || multijugador != null) {
            List<Videojuego> videojuegos = servicioVideojuego.getVideojuego(precio, multijugador);
            return ResponseEntity.ok(videojuegos);
        } else {
            List<Videojuego> videojuegos = servicioVideojuego.getVideojuego();
            return new ResponseEntity<>(videojuegos, HttpStatus.OK);
        }
    }


    /*
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

    //Listar todos los videojuegos

    @GetMapping("/listar")
    public ResponseEntity<List<Videojuego>> getVideojuego() {

        var videojuegos = servicioVideojuego.getVideojuego();
        return new ResponseEntity<>(videojuegos, HttpStatus.OK);
    }


    //Listar por dos parametros

    @GetMapping("/listarfiltro")
    public ResponseEntity<List<Videojuego>> getVideojuego(
            @RequestParam(value = "precio", required = false) Double precio,
            @RequestParam(value = "multijugador", required = false) Boolean multijugador) {
        List<Videojuego> videojuegos = servicioVideojuego.getVideojuego(precio, multijugador);
        return ResponseEntity.ok(videojuegos);
    }

     */



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

