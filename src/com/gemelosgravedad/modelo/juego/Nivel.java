package com.gemelosgravedad.modelo.juego;

import com.gemelosgravedad.modelo.datos.Plataforma;
import java.util.List;

/**
 * Diseno fijo del escenario: piso roto, techo con huecos y plataformas
 * flotantes.
 *
 * <p>Las alturas se definen una sola vez para que el dibujo y las colisiones
 * trabajen con las mismas dimensiones. La lista devuelta es inmutable.</p>
 *
 * @see Plataforma
 * @see EstadoJuego
 */
public final class Nivel {

    private static final int ALTO_SUELO = 60;
    private static final int ALTO_TECHO = 12;
    private static final int ALTO_FLOTANTE = 22;

    private Nivel() {
    }

    /**
     * Crea las plataformas del unico escenario del juego.
     *
     * @return lista inmutable de plataformas del nivel
     */
    public static List<Plataforma> crearPlataformas() {
        int yTecho = EstadoJuego.TECHO - ALTO_TECHO;

        return List.of(
                // Piso partido en cuatro tramos. Los huecos son caidas mortales.
                suelo(0, 220),
                suelo(340, 160),
                suelo(620, 140),
                suelo(880, 120),

                // Techo partido con huecos desfasados: cuando uno cae, el otro
                // suele quedar apoyado y la soga lo sostiene hasta el salto.
                techo(0, 280, yTecho),
                techo(400, 140, yTecho),
                techo(660, 160, yTecho),
                techo(940, 60, yTecho),

                // Rutas flotantes: se pisan por arriba y se cuelgan por debajo.
                flotante(215, 470, 150),
                flotante(540, 405, 160),
                flotante(760, 470, 140),
                flotante(250, 330, 140),
                flotante(420, 245, 170),
                flotante(680, 300, 150));
    }

    private static Plataforma suelo(int x, int ancho) {
        return new Plataforma(x, EstadoJuego.PISO, ancho, ALTO_SUELO,
                Plataforma.Tipo.SUELO);
    }

    private static Plataforma techo(int x, int ancho, int y) {
        return new Plataforma(x, y, ancho, ALTO_TECHO, Plataforma.Tipo.TECHO);
    }

    private static Plataforma flotante(int x, int y, int ancho) {
        return new Plataforma(x, y, ancho, ALTO_FLOTANTE,
                Plataforma.Tipo.FLOTANTE);
    }
}
