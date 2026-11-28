package com.gemelosgravedad.vista.dibujo;

import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.modelo.datos.Plataforma;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Dibuja el piso y el techo rotos, las plataformas y los huecos mortales.
 *
 * <p>Los huecos se calculan una sola vez por fotograma a partir de los tramos
 * ordenados. Asi el pozo, las franjas y las senales de aviso usan exactamente
 * las mismas coordenadas.</p>
 *
 * @see Plataforma
 * @see EstadoJuego
 */
public final class DibujoPlataformas {

    private static final Color CUERPO_SUELO = new Color(33, 33, 33);
    private static final Color CUERPO_TECHO = new Color(48, 45, 38);
    private static final Color CUERPO_PLATAFORMA = new Color(62, 59, 52);
    private static final Color HUELLA = new Color(46, 44, 40);
    private static final Color JUNTA = new Color(24, 24, 24);
    private static final Color POZO = new Color(5, 5, 5);
    private static final Color ROJO_OSCURO = new Color(92, 18, 14);
    private static final Color BISEL = new Color(70, 66, 56);
    private static final int ALTO_HUECO_TECHO = 18;
    private static final int ANCHO_BORDE_POZO = 4;
    private static final int ANCHO_AVISO = 15;
    private static final int ALTO_AVISO_PISO = 10;
    private static final int ALTO_AVISO_TECHO = 8;
    private static final int ANCHO_SENAL = 78;
    private static final int ALTO_SENAL = 20;
    private static final int RAYOS_POR_HUECO = 3;
    private static final BasicStroke TRAZO_GRUESO = new BasicStroke(3f);
    private static final BasicStroke TRAZO_MEDIO = new BasicStroke(2.5f);
    private static final BasicStroke TRAZO_FINO = new BasicStroke(2f);

    private DibujoPlataformas() {
    }

    /**
     * Dibuja todas las plataformas y los huecos del escenario.
     *
     * @param g contexto grafico
     * @param plataformas plataformas de la partida
     * @param tick contador de pasos para las animaciones
     */
    public static void dibujar(Graphics2D g, List<Plataforma> plataformas, int tick) {
        if (plataformas == null || plataformas.isEmpty()) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        List<Hueco> huecosPiso = buscarHuecos(plataformas, Plataforma.Tipo.SUELO);
        List<Hueco> huecosTecho = buscarHuecos(plataformas, Plataforma.Tipo.TECHO);

        for (Hueco hueco : huecosPiso) {
            dibujarHuecoPiso(g, hueco, tick);
        }
        for (Hueco hueco : huecosTecho) {
            dibujarHuecoTecho(g, hueco, tick);
        }

        for (Plataforma plataforma : plataformas) {
            switch (plataforma.tipo()) {
                case SUELO -> dibujarSuelo(g, plataforma);
                case TECHO -> dibujarTecho(g, plataforma);
                case FLOTANTE -> dibujarFlotante(g, plataforma);
            }
        }

        for (Hueco hueco : huecosPiso) {
            dibujarAvisoPiso(g, hueco, tick);
        }
        for (Hueco hueco : huecosTecho) {
            dibujarAvisoTecho(g, hueco, tick);
        }
    }

    /**
     * Ordena los tramos de un tipo y calcula los huecos entre ellos.
     *
     * @param plataformas plataformas de la partida
     * @param tipo tipo de tramo que se quiere analizar
     * @return huecos con ancho positivo
     */
    private static List<Hueco> buscarHuecos(List<Plataforma> plataformas,
                                            Plataforma.Tipo tipo) {
        List<Plataforma> tramos = plataformas.stream()
                .filter(plataforma -> plataforma.tipo() == tipo)
                .sorted(Comparator.comparingInt(Plataforma::x))
                .toList();

        List<Hueco> huecos = new ArrayList<>();
        for (int i = 0; i < tramos.size() - 1; i++) {
            Plataforma actual = tramos.get(i);
            Plataforma siguiente = tramos.get(i + 1);
            int inicio = actual.x() + actual.ancho();
            int ancho = siguiente.x() - inicio;
            if (ancho > 0) {
                huecos.add(new Hueco(inicio, ancho));
            }
        }
        return huecos;
    }

    /**
     * Dibuja un hueco del piso con su pozo y sus rayos.
     *
     * @param g contexto grafico
     * @param hueco posicion y ancho del hueco
     * @param tick contador de pasos
     */
    private static void dibujarHuecoPiso(Graphics2D g, Hueco hueco, int tick) {
        int inicio = hueco.inicio();
        int ancho = hueco.ancho();
        int alto = EstadoJuego.ALTO - EstadoJuego.PISO;
        int centro = hueco.centro();

        dibujarPozo(g, inicio, ancho, EstadoJuego.PISO, alto);
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(inicio + ANCHO_BORDE_POZO, EstadoJuego.PISO, 2, alto);
        g.fillRect(inicio + ancho - ANCHO_BORDE_POZO - 2, EstadoJuego.PISO, 2, alto);

        EstiloBrutal.franjas(g, inicio, EstadoJuego.PISO, ancho, ALTO_AVISO_PISO,
                EstiloBrutal.ROJO);
        EstiloBrutal.franjas(g, inicio, EstadoJuego.ALTO - 9, ancho, 9,
                EstiloBrutal.AMARILLO);

        Shape recorte = g.getClip();
        try {
            g.clipRect(inicio + 6, EstadoJuego.PISO + 11, ancho - 12, alto - 21);
            g.setStroke(TRAZO_GRUESO);
            for (int k = 0; k < RAYOS_POR_HUECO; k++) {
                int y = EstadoJuego.PISO + 14 + ((tick * 2 + k * 13) % 28);
                g.setColor(k % 2 == 0 ? EstiloBrutal.ROJO : EstiloBrutal.AMARILLO);
                g.drawLine(centro - 13, y, centro, y + 11);
                g.drawLine(centro + 13, y, centro, y + 11);
            }
        } finally {
            g.setClip(recorte);
        }
    }

    /**
     * Dibuja un hueco del techo con su pozo y sus rayos.
     *
     * @param g contexto grafico
     * @param hueco posicion y ancho del hueco
     * @param tick contador de pasos
     */
    private static void dibujarHuecoTecho(Graphics2D g, Hueco hueco, int tick) {
        int inicio = hueco.inicio();
        int ancho = hueco.ancho();
        int arriba = EstadoJuego.TECHO - ALTO_HUECO_TECHO;
        int centro = hueco.centro();

        dibujarPozo(g, inicio, ancho, arriba, ALTO_HUECO_TECHO);
        EstiloBrutal.franjas(g, inicio, EstadoJuego.TECHO - 7, ancho, 7,
                EstiloBrutal.ROJO);

        Shape recorte = g.getClip();
        try {
            g.clipRect(inicio + 5, arriba + 2, ancho - 10, ALTO_HUECO_TECHO - 4);
            g.setStroke(TRAZO_MEDIO);
            for (int k = 0; k < RAYOS_POR_HUECO; k++) {
                int y = arriba + 3 + ((tick * 2 + k * 7) % 17);
                g.setColor(k % 2 == 0 ? EstiloBrutal.ROJO : EstiloBrutal.AMARILLO);
                g.drawLine(centro - 10, y + 6, centro, y);
                g.drawLine(centro + 10, y + 6, centro, y);
            }
        } finally {
            g.setClip(recorte);
        }
    }

    /**
     * Rellena el pozo y sus bordes rojos.
     *
     * @param g contexto grafico
     * @param inicio coordenada horizontal del hueco
     * @param ancho ancho del hueco
     * @param y coordenada vertical superior
     * @param alto alto del pozo
     */
    private static void dibujarPozo(Graphics2D g, int inicio, int ancho,
                                    int y, int alto) {
        g.setColor(POZO);
        g.fillRect(inicio, y, ancho, alto);
        g.setColor(ROJO_OSCURO);
        g.fillRect(inicio, y, ANCHO_BORDE_POZO, alto);
        g.fillRect(inicio + ancho - ANCHO_BORDE_POZO, y, ANCHO_BORDE_POZO, alto);
    }

    /**
     * Dibuja las franjas y la senal de aviso de un hueco del piso.
     *
     * @param g contexto grafico
     * @param hueco posicion y ancho del hueco
     * @param tick contador de pasos
     */
    private static void dibujarAvisoPiso(Graphics2D g, Hueco hueco, int tick) {
        int inicio = hueco.inicio();
        int ancho = hueco.ancho();
        EstiloBrutal.franjas(g, inicio - ANCHO_AVISO, EstadoJuego.PISO - 4,
                ANCHO_AVISO, ALTO_AVISO_PISO, EstiloBrutal.ROJO);
        EstiloBrutal.franjas(g, inicio + ancho, EstadoJuego.PISO - 4,
                ANCHO_AVISO, ALTO_AVISO_PISO, EstiloBrutal.ROJO);
        dibujarSenal(g, hueco.centro(), EstadoJuego.PISO - 32, tick, true);
    }

    /**
     * Dibuja las franjas y la senal de aviso de un hueco del techo.
     *
     * @param g contexto grafico
     * @param hueco posicion y ancho del hueco
     * @param tick contador de pasos
     */
    private static void dibujarAvisoTecho(Graphics2D g, Hueco hueco, int tick) {
        int inicio = hueco.inicio();
        int ancho = hueco.ancho();
        EstiloBrutal.franjas(g, inicio - ANCHO_AVISO, EstadoJuego.TECHO - 6,
                ANCHO_AVISO, ALTO_AVISO_TECHO, EstiloBrutal.AMARILLO);
        EstiloBrutal.franjas(g, inicio + ancho, EstadoJuego.TECHO - 6,
                ANCHO_AVISO, ALTO_AVISO_TECHO, EstiloBrutal.AMARILLO);
        dibujarSenal(g, hueco.centro(), EstadoJuego.TECHO + 9, tick, false);
    }

    /**
     * Dibuja una senal de peligro con forma de cartel y flecha.
     *
     * @param g contexto grafico
     * @param centroX centro horizontal de la senal
     * @param y coordenada vertical del cartel
     * @param tick contador de pasos
     * @param apuntaAbajo {@code true} si la flecha senala hacia abajo
     */
    private static void dibujarSenal(Graphics2D g, int centroX, int y, int tick,
                                     boolean apuntaAbajo) {
        int x = centroX - ANCHO_SENAL / 2;

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 4, y + 4, ANCHO_SENAL, ALTO_SENAL);
        g.setColor(EstiloBrutal.AMARILLO);
        g.fillRect(x, y, ANCHO_SENAL, ALTO_SENAL);
        g.setColor(EstiloBrutal.NEGRO);
        g.setStroke(TRAZO_FINO);
        g.drawRect(x, y, ANCHO_SENAL, ALTO_SENAL);

        boolean pulso = (tick / 14) % 2 == 0;
        g.setColor(pulso ? EstiloBrutal.ROJO : EstiloBrutal.NEGRO);
        g.fillRect(x + 4, y + 4, 13, ALTO_SENAL - 8);
        g.setFont(EstiloBrutal.mono(11));
        g.setColor(EstiloBrutal.PAPEL);
        g.drawString("!", x + 9, y + ALTO_SENAL - 6);

        g.setFont(EstiloBrutal.mono(9));
        g.setColor(EstiloBrutal.NEGRO);
        g.drawString("VACIO", x + 22, y + ALTO_SENAL - 6);

        Polygon flecha = new Polygon();
        if (apuntaAbajo) {
            flecha.addPoint(centroX - 8, y + ALTO_SENAL + 1);
            flecha.addPoint(centroX + 8, y + ALTO_SENAL + 1);
            flecha.addPoint(centroX, y + ALTO_SENAL + 8);
        } else {
            flecha.addPoint(centroX - 8, y - 1);
            flecha.addPoint(centroX + 8, y - 1);
            flecha.addPoint(centroX, y - 8);
        }
        g.setColor(EstiloBrutal.NEGRO);
        g.fillPolygon(flecha);
        g.setColor(EstiloBrutal.ROJO);
        flecha.translate(0, apuntaAbajo ? -2 : 2);
        g.fillPolygon(flecha);
    }

    /**
     * Dibuja un tramo de piso.
     *
     * @param g contexto grafico
     * @param plataforma tramo de piso
     */
    private static void dibujarSuelo(Graphics2D g, Plataforma plataforma) {
        int x = plataforma.x();
        int y = plataforma.y();
        int ancho = plataforma.ancho();
        int alto = EstadoJuego.ALTO - y;

        g.setColor(CUERPO_SUELO);
        g.fillRect(x, y, ancho, alto);

        g.setColor(JUNTA);
        g.setStroke(TRAZO_FINO);
        for (int linea = x + 26; linea < x + ancho; linea += 30) {
            g.drawLine(linea, y + 10, linea, EstadoJuego.ALTO);
        }

        for (int bloque = 0; bloque < ancho; bloque += 24) {
            g.setColor((bloque / 24) % 2 == 0 ? EstiloBrutal.AZUL : EstiloBrutal.NEGRO);
            g.fillRect(x + bloque, y, Math.min(24, ancho - bloque), 6);
        }
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x, y - 3, ancho, 3);
        g.fillRect(x, y + 6, ancho, 2);
        g.fillRect(x, y, 5, alto);
        g.fillRect(x + ancho - 5, y, 5, alto);

        g.setColor(EstiloBrutal.AMARILLO);
        g.fillRect(x + 12, y + 20, 12, 12);
        g.fillRect(x + ancho - 24, y + 20, 12, 12);
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 16, y + 24, 4, 4);
        g.fillRect(x + ancho - 20, y + 24, 4, 4);
    }

    /**
     * Dibuja un tramo de techo.
     *
     * @param g contexto grafico
     * @param plataforma tramo de techo
     */
    private static void dibujarTecho(Graphics2D g, Plataforma plataforma) {
        int x = plataforma.x();
        int y = plataforma.y();
        int ancho = plataforma.ancho();
        int alto = plataforma.alto();

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 5, y + 5, ancho, alto);
        g.setColor(CUERPO_TECHO);
        g.fillRect(x, y, ancho, alto);
        g.setColor(BISEL);
        g.fillRect(x, y, ancho, 3);
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x, y + alto - 3, ancho, 3);
        g.fillRect(x, y, 4, alto);
        g.fillRect(x + ancho - 4, y, 4, alto);

        for (int remache = x + 18; remache < x + ancho - 12; remache += 42) {
            g.setColor(EstiloBrutal.NEGRO);
            g.fillRect(remache, y + 7, 6, 6);
            g.setColor(EstiloBrutal.PAPEL);
            g.fillRect(remache + 2, y + 9, 2, 2);
        }

        EstiloBrutal.franjas(g, x, y + alto - 7, ancho, 7, EstiloBrutal.AMARILLO);
    }

    /**
     * Dibuja una plataforma flotante con su huella diagonal.
     *
     * @param g contexto grafico
     * @param plataforma plataforma flotante
     */
    private static void dibujarFlotante(Graphics2D g, Plataforma plataforma) {
        int x = plataforma.x();
        int y = plataforma.y();
        int ancho = plataforma.ancho();
        int alto = plataforma.alto();

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 7, y + 7, ancho, alto);
        g.setColor(CUERPO_PLATAFORMA);
        g.fillRect(x, y, ancho, alto);

        Shape recorte = g.getClip();
        try {
            g.clipRect(x + 4, y + 4, ancho - 8, alto - 8);
            g.setColor(HUELLA);
            g.setStroke(TRAZO_FINO);
            for (int linea = x - alto; linea < x + ancho; linea += 18) {
                g.drawLine(linea, y + alto, linea + alto, y);
            }
        } finally {
            g.setClip(recorte);
        }

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x, y, ancho, 4);
        g.fillRect(x, y + alto - 4, ancho, 4);
        g.fillRect(x, y, 4, alto);
        g.fillRect(x + ancho - 4, y, 4, alto);

        g.setColor(EstiloBrutal.AZUL);
        g.fillRect(x + 4, y + 4, ancho - 8, 4);
        g.setColor(EstiloBrutal.AMARILLO);
        g.fillRect(x + 4, y + alto - 8, ancho - 8, 4);
        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 4, y + 8, ancho - 8, 2);
        g.fillRect(x + 4, y + alto - 10, ancho - 8, 2);

        g.fillRect(x + 8, y + 12, 6, 6);
        g.fillRect(x + ancho - 14, y + 12, 6, 6);
        g.fillRect(x + 8, y + alto - 18, 6, 6);
        g.fillRect(x + ancho - 14, y + alto - 18, 6, 6);
        g.setColor(EstiloBrutal.PAPEL);
        g.fillRect(x + 10, y + 14, 2, 2);
        g.fillRect(x + ancho - 12, y + 14, 2, 2);
        g.fillRect(x + 10, y + alto - 16, 2, 2);
        g.fillRect(x + ancho - 12, y + alto - 16, 2, 2);
    }

    /**
     * Hueco entre dos tramos consecutivos de un mismo tipo.
     *
     * @param inicio coordenada horizontal donde empieza el hueco
     * @param ancho ancho del hueco
     */
    private record Hueco(int inicio, int ancho) {

        /**
         * Devuelve el centro horizontal del hueco.
         *
         * @return coordenada central
         */
        private int centro() {
            return inicio + ancho / 2;
        }
    }
}
