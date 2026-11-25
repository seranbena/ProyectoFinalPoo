package com.gemelosgravedad.vista.dibujo;

import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.modelo.Marcador;
import com.gemelosgravedad.modelo.PoderActivo;
import com.gemelosgravedad.modelo.TipoPowerUp;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Locale;

/**
 * Dibuja el HUD superior: vidas, tiempo, puntos, power-up, nivel y jugador.
 *
 * <p>Todos los datos se leen del modelo. El HUD no guarda estado propio; el
 * parpadeo de invulnerabilidad se controla desde el panel.</p>
 *
 * @see EstadoJuego
 * @see Marcador
 */
public final class DibujoHud {

    private static final Color SEPARADOR = new Color(62, 62, 62);
    private static final Color PODER_INACTIVO = new Color(105, 105, 105);
    private static final Color VIDA_PERDIDA = new Color(80, 80, 80);
    private static final int X_VIDAS = 24;
    private static final int Y_VIDAS = 28;
    private static final int PASO_VIDAS = 26;
    private static final int TAMANO_VIDA = 18;
    private static final int MARGEN_DERECHO = 28;
    private static final int X_NOMBRE = EstadoJuego.ANCHO - MARGEN_DERECHO;
    private static final int Y_DATO = 47;
    private static final int Y_TEXTO = 45;

    private DibujoHud() {
    }

    /**
     * Pinta la barra superior de datos.
     *
     * @param g contexto grafico
     * @param estado partida de la que se leen los datos
     */
    public static void dibujar(Graphics2D g, EstadoJuego estado) {
        Marcador marcador = estado.getMarcador();
        PoderActivo poder = estado.getPoder();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);

        bloqueVidas(g, marcador.getVidas());
        separador(g, 142);

        rotulo(g, "TIEMPO", 162, EstiloBrutal.AMARILLO);
        g.setFont(EstiloBrutal.titulo(22));
        g.setColor(EstiloBrutal.AMARILLO);
        g.drawString(marcador.getTiempoTexto(), 162, Y_DATO);
        separador(g, 302);

        rotulo(g, "PUNTOS", 322, EstiloBrutal.PAPEL);
        g.setFont(EstiloBrutal.titulo(22));
        g.setColor(EstiloBrutal.PAPEL);
        g.drawString(String.valueOf(marcador.getPuntaje()), 322, Y_DATO);
        separador(g, 470);

        rotulo(g, "POWER-UP", 490, EstiloBrutal.CIAN);
        dibujarPoder(g, poder, 490);
        separador(g, 704);

        rotulo(g, "NIVEL", 724, EstiloBrutal.AZUL);
        g.setFont(EstiloBrutal.titulo(22));
        g.setColor(EstiloBrutal.BLANCO);
        g.drawString(String.format(Locale.ROOT, "%02d", marcador.getNivel()), 724, Y_DATO);
        separador(g, 806);

        String nombre = estado.getNombreJugador();
        if (nombre == null || nombre.trim().isEmpty()) {
            nombre = "JUGADOR";
        }
        rotulo(g, "JUGADOR", 826, EstiloBrutal.BLANCO);
        g.setFont(EstiloBrutal.mono(12));
        g.setColor(EstiloBrutal.GRIS_CLARO);
        int ancho = g.getFontMetrics().stringWidth(nombre);
        g.drawString(nombre, X_NOMBRE - ancho, Y_TEXTO);
    }

    /**
     * Dibuja los cuadros de vida restantes.
     *
     * @param g contexto grafico
     * @param vidas vidas actuales
     */
    private static void bloqueVidas(Graphics2D g, int vidas) {
        rotulo(g, "VIDAS", X_VIDAS, EstiloBrutal.ROJO);
        for (int i = 0; i < Marcador.VIDAS_INICIALES; i++) {
            int x = X_VIDAS + i * PASO_VIDAS;
            if (i < vidas) {
                g.setColor(EstiloBrutal.ROJO);
                g.fillRect(x, Y_VIDAS, TAMANO_VIDA, TAMANO_VIDA);
                g.setColor(EstiloBrutal.PAPEL);
            } else {
                g.setColor(EstiloBrutal.CARBON_CLARO);
                g.fillRect(x, Y_VIDAS, TAMANO_VIDA, TAMANO_VIDA);
                g.setColor(VIDA_PERDIDA);
            }
            g.setStroke(new BasicStroke(2f));
            g.drawRect(x, Y_VIDAS, TAMANO_VIDA, TAMANO_VIDA);
        }
    }

    /**
     * Dibuja el power-up activo y sus segundos restantes.
     *
     * @param g contexto grafico
     * @param poder control del power-up
     * @param x coordenada horizontal del bloque
     */
    private static void dibujarPoder(Graphics2D g, PoderActivo poder, int x) {
        TipoPowerUp tipo = poder.getTipo();
        if (tipo == null) {
            g.setFont(EstiloBrutal.mono(13));
            g.setColor(PODER_INACTIVO);
            g.drawString("SIN PODER", x, Y_TEXTO);
            return;
        }
        g.setFont(EstiloBrutal.mono(14));
        g.setColor(tipo.getColor());
        g.drawString(tipo.name() + " " + poder.getSegundos() + " S", x, Y_TEXTO);
    }

    /**
     * Dibuja el rotulo pequeno de un bloque del HUD.
     *
     * @param g contexto grafico
     * @param texto texto del rotulo
     * @param x coordenada horizontal
     * @param color color de la marca superior
     */
    private static void rotulo(Graphics2D g, String texto, int x, Color color) {
        g.setColor(color);
        g.fillRect(x, 9, 24, 3);
        g.setColor(EstiloBrutal.GRIS_CLARO);
        g.setFont(EstiloBrutal.mono(9));
        g.drawString(texto, x, 21);
    }

    /**
     * Dibuja una linea vertical de separacion.
     *
     * @param g contexto grafico
     * @param x coordenada horizontal
     */
    private static void separador(Graphics2D g, int x) {
        g.setColor(SEPARADOR);
        g.fillRect(x, 10, 2, 42);
    }
}
