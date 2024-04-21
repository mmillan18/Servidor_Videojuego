package Servidor_Videojuego.Servidor_Videojuego.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

public class Usuario {

    private int id;
    private String nombre;
    private double estatura;
    private LocalDateTime fechaNacimiento;
    private boolean esPremium;
    private List<Videojuego> videojuegos;


}
