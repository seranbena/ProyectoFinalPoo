package com.gemelosgravedad.modelo.entidades;

import com.gemelosgravedad.modelo.juego.GestorColisiones;
import com.gemelosgravedad.recursos.GestorRecursos;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Cristal coleccionable que suma puntos al jugador.
 *
 * <p>Avanza de derecha a izquierda y desaparece al salir de la pantalla. Su
 * valor esta centralizado en {@link #VALOR} para que puntuacion y textos de la
 * interfaz usen la misma cifra.</p>
 *
 * @see EntidadDesplazable
 * @see GestorColisiones
 */
public final class Cristal extends EntidadDesplazable {

    /** Puntos que otorga cada cristal. */
    public static final int VALOR = 25;

    private static final Color BRILLO = new Color(0, 209, 209, 55);
    private static final Color SOMBRA = new Color(0, 0, 0, 150);
    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color CUERPO = new Color(0, 140, 165);
    private static final Color NUCLEO = new Color(0, 209, 209);
    private static final Color DESTELLO = new Color(190, 250, 250);
    private static final int FRECUENCIA_BRILLO = 24;
    private static final int DURACION_BRILLO = 12;

    /**
     * Crea un cristal en la posicion indicada.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param velocidad velocidad de avance horizontal
     */
    public Cristal(int x, int y, double velocidad) {
        super(x, y, 30, 30, velocidad, 20);
        this.sprites = GestorRecursos.cargarCuadros("sprites/cristal", 2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dibujar(Graphics2D g) {
        int x = getX();
        int y = getY();
        int ancho = getAncho();
        int alto = getAlto();

        if (contadorAnimacion % FRECUENCIA_BRILLO < DURACION_BRILLO) {
            g.setColor(BRILLO);
            g.fillRect(x - 5, y - 5, ancho + 10, alto + 10);
        }
        if (dibujarSprite(g, false)) {
            return;
        }

        g.setColor(SOMBRA);
        g.fillPolygon(crearDiamante(0, 3, 3));

        g.setColor(NEGRO);
        g.fillPolygon(crearDiamante(0, 0, 0));
        g.setColor(CUERPO);
        g.fillPolygon(crearDiamante(5, 0, 0));

        g.setColor(NUCLEO);
        g.fillPolygon(crearDiamante(10, 0, 0));

        int centroX = x + ancho / 2;
        int centroY = y + alto / 2;
        g.setColor(DESTELLO);
        g.fillRect(centroX - 2, y + 14, 4, alto - 28);
        g.setColor(NEGRO);
        g.fillRect(centroX - 4, centroY - 3, 8, 6);
    }

    /**
     * Devuelve los puntos que otorga el cristal.
     *
     * @return valor en puntos
     */
    public int getValor() {
        return VALOR;
    }
}
