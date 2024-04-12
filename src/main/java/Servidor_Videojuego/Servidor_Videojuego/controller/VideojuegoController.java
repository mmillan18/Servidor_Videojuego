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


    @GetMapping
    public ResponseEntity<Videojuego> getVideojuego(){

        Videojuego videojuego;
        videojuego = servicioVideojuego.getVideojuego();

        if (videojuego == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(videojuego);
    }

    @PostMapping
    public ResponseEntity<Videojuego> setVideojuego(@RequestBody Videojuego videojuego, BindingResult result) throws Exception {
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

        if(videojuego.getId() == 111) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error 111");

        }
        servicioVideojuego.setVideojuego(videojuego);
        return ResponseEntity.status(HttpStatus.CREATED).body(videojuego);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Videojuego> updateVideojuego(@RequestBody Videojuego videojuego,
                                                       @PathVariable("id") int id, BindingResult result) throws Exception {
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }
        Videojuego resVideojuego= servicioVideojuego.updateVideojuego(videojuego, id);

        return ResponseEntity.status(HttpStatus.FOUND).body(videojuego);
    }

    //////ELIMINAR

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteVideojuego(@PathVariable("id") int id) {
        boolean deleted = servicioVideojuego.deleteVideojuego(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
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

