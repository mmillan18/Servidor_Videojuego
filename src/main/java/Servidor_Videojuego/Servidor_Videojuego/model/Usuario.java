package Servidor_Videojuego.Servidor_Videojuego.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {

    private int id;
    private String nombre;
    private LocalDateTime fechaNacimiento;


}
