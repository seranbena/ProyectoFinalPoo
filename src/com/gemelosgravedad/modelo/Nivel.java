package com.gemelosgravedad.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Diseno fijo del escenario: piso roto, techo con huecos y plataformas
 * flotantes.
 *
 * <p>Las alturas se definen una sola vez para que el dibujo y las colisiones
 * trabajen con las mismas dimensiones.</p>
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
     * @return lista ordenada de plataformas del nivel
     */
    public static List<Plataforma> crearPlataformas() {
        List<Plataforma> plataformas = new ArrayList<>();

        // Piso partido en cuatro tramos. Los huecos son caidas mortales.
        plataformas.add(new Plataforma(0, EstadoJuego.PISO, 220, ALTO_SUELO,
                Plataforma.SUELO));
        plataformas.add(new Plataforma(340, EstadoJuego.PISO, 160, ALTO_SUELO,
                Plataforma.SUELO));
        plataformas.add(new Plataforma(620, EstadoJuego.PISO, 140, ALTO_SUELO,
                Plataforma.SUELO));
        plataformas.add(new Plataforma(880, EstadoJuego.PISO, 120, ALTO_SUELO,
                Plataforma.SUELO));

        // Techo partido con huecos desfasados: cuando uno cae, el otro suele
        // quedar apoyado y la soga lo sostiene hasta el salto conjunto.
        int yTecho = EstadoJuego.TECHO - ALTO_TECHO;
        plataformas.add(new Plataforma(0, yTecho, 280, ALTO_TECHO,
                Plataforma.TECHO));
        plataformas.add(new Plataforma(400, yTecho, 140, ALTO_TECHO,
                Plataforma.TECHO));
        plataformas.add(new Plataforma(660, yTecho, 160, ALTO_TECHO,
                Plataforma.TECHO));
        plataformas.add(new Plataforma(940, yTecho, 60, ALTO_TECHO,
                Plataforma.TECHO));

        // Rutas flotantes: se pisan por arriba y se cuelgan por debajo.
        plataformas.add(new Plataforma(215, 470, 150, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));
        plataformas.add(new Plataforma(540, 405, 160, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));
        plataformas.add(new Plataforma(760, 470, 140, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));
        plataformas.add(new Plataforma(250, 330, 140, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));
        plataformas.add(new Plataforma(420, 245, 170, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));
        plataformas.add(new Plataforma(680, 300, 150, ALTO_FLOTANTE,
                Plataforma.FLOTANTE));

        return plataformas;
    }
}
