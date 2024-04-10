package Servidor_Videojuego.Servidor_Videojuego.services;

import Servidor_Videojuego.Servidor_Videojuego.model.Videojuego;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class ServicioVideojuego implements IServicioVideojuego{
    private Videojuego videojuego;

    @Override
    public Videojuego getVideojuego() {
        return videojuego;
    }

    @Override
    public Videojuego setVideojuego(Videojuego videojuego) {
        this.videojuego = videojuego;
        return this.videojuego;
    }

    @Override
    public Videojuego updateVideojuego(Videojuego videojuego, int id) {

        if (id == this.videojuego.getId()) {
            this.videojuego = videojuego;
        }
        return videojuego;
    }
}