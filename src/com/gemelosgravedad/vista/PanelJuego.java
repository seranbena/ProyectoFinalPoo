package com.gemelosgravedad.vista;

import com.gemelosgravedad.modelo.Entidad;
import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.modelo.Jugador;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.dibujo.DibujoCuerda;
import com.gemelosgravedad.vista.dibujo.DibujoFondo;
import com.gemelosgravedad.vista.dibujo.DibujoHud;
import com.gemelosgravedad.vista.dibujo.DibujoPlataformas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Panel donde se dibuja la partida.
 *
 * <p>Solo pinta el estado que recibe del controlador: no aplica reglas ni
 * modifica el modelo. El parpadeo de invulnerabilidad se controla desde los
 * temporizadores del juego.</p>
 *
 * @see EstadoJuego
 * @see DibujoHud
 */
public final class PanelJuego extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Color VELO_PAUSA = new Color(5, 5, 5, 220);
    private static final int MARGEN_PIE = 24;
    private static final int ALTO_PIE = 17;
    private static final int ANCHO_PAUSA = 560;
    private static final int ALTO_PAUSA = 310;

    private transient EstadoJuego estado;

    /** Indica si la partida esta pausada. */
    private boolean pausado;

    /** Controla la visibilidad de los gemelos durante la invulnerabilidad. */
    private boolean parpadeoVisible = true;

    /**
     * Crea el panel de juego y su listener de foco.
     */
    public PanelJuego() {
        setPreferredSize(new Dimension(EstadoJuego.ANCHO, EstadoJuego.ALTO));
        setBackground(EstiloBrutal.CARBON);
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            /**
             * Devuelve el foco al panel cuando se hace clic sobre el.
             *
             * @param e evento del raton
             */
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    /**
     * Asocia la partida que se va a dibujar.
     *
     * @param estado partida activa o {@code null} antes de comenzar
     */
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
        repaint();
    }

    /**
     * Indica si se debe dibujar el velo de pausa.
     *
     * @param pausado {@code true} para mostrar la pausa
     */
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    /**
     * Controla la visibilidad del parpadeo de invulnerabilidad.
     *
     * @param parpadeoVisible {@code true} si los gemelos deben verse
     */
    public void setParpadeoVisible(boolean parpadeoVisible) {
        this.parpadeoVisible = parpadeoVisible;
    }

    /**
     * Pide el foco del teclado en el siguiente ciclo de Swing.
     */
    public void pedirFoco() {
        SwingUtilities.invokeLater(this::requestFocusInWindow);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int tick = estado == null ? 0 : estado.getTick();
            DibujoFondo.dibujar(g2, tick);

            if (estado == null) {
                dibujarMensaje(g2, "PREPARANDO LA PARTIDA...");
                return;
            }

            DibujoPlataformas.dibujar(g2, estado.getPlataformas(), tick);
            DibujoCuerda.dibujar(g2, estado.getJugadorAzul(),
                    estado.getJugadorAmarillo(), tick);

            List<Entidad> copia = estado.obtenerEntidadesCopia();
            for (Entidad entidad : copia) {
                entidad.dibujar(g2);
            }

            dibujarJugadores(g2);
            dibujarRescate(g2, tick);
            DibujoHud.dibujar(g2, estado);
            dibujarPie(g2, tick);
            if (pausado) {
                dibujarPausa(g2);
            }
        } finally {
            g2.dispose();
        }
    }

    /**
     * Dibuja a los gemelos, respetando el parpadeo de invulnerabilidad.
     *
     * @param g contexto grafico
     */
    private void dibujarJugadores(Graphics2D g) {
        boolean visible = !estado.getMarcador().isInvulnerable() || parpadeoVisible;
        if (!visible) {
            return;
        }
        Jugador azul = estado.getJugadorAzul();
        Jugador amarillo = estado.getJugadorAmarillo();
        azul.dibujar(g);
        amarillo.dibujar(g);
    }

    /**
     * Dibuja la leyenda inferior con los controles y el aviso de rescate.
     *
     * @param g contexto grafico
     * @param tick contador de pasos
     */
    private void dibujarPie(Graphics2D g, int tick) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(EstiloBrutal.mono(11));

        String izquierda = "P: PAUSA   //   ESC: MENÚ";
        EstiloBrutal.textoDuro(g, izquierda, MARGEN_PIE,
                EstadoJuego.ALTO - ALTO_PIE, EstiloBrutal.PAPEL,
                EstiloBrutal.NEGRO, 2);

        boolean rescate = estado.getJugadorColgado() != null;
        String derecha = rescate
                ? "¡SOGA TENSA!  ESPACIO = SALTO CONJUNTO"
                : "SALTA ENTRE PLATAFORMAS · EVITA EL VACÍO";
        Color color = rescate
                ? ((tick / 12) % 2 == 0 ? EstiloBrutal.ROJO : EstiloBrutal.AMARILLO)
                : EstiloBrutal.GRIS_CLARO;
        int x = EstadoJuego.ANCHO - MARGEN_PIE
                - g.getFontMetrics().stringWidth(derecha);
        EstiloBrutal.textoDuro(g, derecha, x, EstadoJuego.ALTO - ALTO_PIE,
                color, EstiloBrutal.NEGRO, 2);
    }

    /**
     * Dibuja la etiqueta que explica como rescatar al gemelo colgado.
     *
     * @param g contexto grafico
     * @param tick contador de pasos
     */
    private void dibujarRescate(Graphics2D g, int tick) {
        Jugador colgado = estado.getJugadorColgado();
        if (colgado == null) {
            return;
        }
        boolean colgadoAbajo = colgado.getSentidoGravedad() == 1;
        int cx = colgado.getX() + colgado.getAncho() / 2;
        String texto = "SALTO CONJUNTO: ESPACIO";
        g.setFont(EstiloBrutal.mono(10));
        int ancho = g.getFontMetrics().stringWidth(texto) + 18;
        int x = Math.max(14, Math.min(cx - ancho / 2,
                EstadoJuego.ANCHO - ancho - 14));
        int y = colgadoAbajo
                ? colgado.getY() - 38
                : colgado.getY() + colgado.getAlto() + 16;
        y = Math.max(70, Math.min(y, EstadoJuego.ALTO - 66));

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 4, y + 4, ancho, 22);
        g.setColor((tick / 12) % 2 == 0
                ? EstiloBrutal.AMARILLO : EstiloBrutal.ROJO);
        g.fillRect(x, y, ancho, 22);
        g.setColor(EstiloBrutal.NEGRO);
        g.setStroke(new BasicStroke(2f));
        g.drawRect(x, y, ancho, 22);
        g.drawString(texto, x + 9, y + 15);

        int fy = colgadoAbajo ? y + 22 : y - 9;
        Polygon flecha = new Polygon();
        flecha.addPoint(cx - 9, fy);
        flecha.addPoint(cx + 9, fy);
        flecha.addPoint(cx, fy + (colgadoAbajo ? 9 : -9));
        g.setColor(EstiloBrutal.ROJO);
        g.fillPolygon(flecha);
        g.setColor(EstiloBrutal.NEGRO);
        g.setStroke(new BasicStroke(2f));
        g.drawPolygon(flecha);
    }

    /**
     * Dibuja el cartel de pausa sobre la partida congelada.
     *
     * @param g contexto grafico
     */
    private void dibujarPausa(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(VELO_PAUSA);
        g.fillRect(0, 0, EstadoJuego.ANCHO, EstadoJuego.ALTO);

        int x = (EstadoJuego.ANCHO - ANCHO_PAUSA) / 2;
        int y = (EstadoJuego.ALTO - ALTO_PAUSA) / 2;

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 12, y + 12, ANCHO_PAUSA, ALTO_PAUSA);
        g.setColor(EstiloBrutal.PAPEL);
        g.fillRect(x, y, ANCHO_PAUSA, ALTO_PAUSA);

        g.setColor(EstiloBrutal.NEGRO);
        g.setStroke(new BasicStroke(8f));
        g.drawRect(x + 4, y + 4, ANCHO_PAUSA - 8, ALTO_PAUSA - 8);

        g.setColor(EstiloBrutal.ROJO);
        g.fillRect(x + 9, y + 9, 14, ALTO_PAUSA - 18);

        g.setColor(EstiloBrutal.NEGRO);
        g.fillRect(x + 23, y + 9, ANCHO_PAUSA - 32, 80);

        g.setFont(EstiloBrutal.titulo(54));
        g.setColor(EstiloBrutal.AMARILLO);
        g.drawString("PAUSA", x + 46, y + 70);

        for (int i = 0; i < 3; i++) {
            g.setColor(i == 1 ? EstiloBrutal.CIAN : EstiloBrutal.AMARILLO);
            g.fillRect(x + ANCHO_PAUSA - 104 + i * 26, y + 34, 16, 16);
        }

        EstiloBrutal.franjas(g, x + 23, y + 89, ANCHO_PAUSA - 32, 9,
                EstiloBrutal.AMARILLO);

        g.setFont(EstiloBrutal.mono(11));
        g.setColor(EstiloBrutal.GRIS);
        g.drawString("SISTEMA EN ESPERA // GD—02", x + 46, y + 128);

        g.setFont(EstiloBrutal.titulo(24));
        g.setColor(EstiloBrutal.NEGRO);
        g.drawString("P  //  CONTINUAR", x + 46, y + 178);

        g.setFont(EstiloBrutal.mono(14));
        g.setColor(EstiloBrutal.NEGRO);
        g.drawString("ESC  //  VOLVER AL MENÚ", x + 46, y + 216);

        g.setFont(EstiloBrutal.monoNormal(11));
        g.setColor(EstiloBrutal.GRIS);
        g.drawString("LA PARTIDA ESTÁ CONGELADA.", x + 46, y + 256);

        EstiloBrutal.franjas(g, x, y + ALTO_PAUSA - 10, ANCHO_PAUSA, 10,
                EstiloBrutal.AZUL);
    }

    /**
     * Dibuja un mensaje central mientras no hay partida.
     *
     * @param g contexto grafico
     * @param texto texto del mensaje
     */
    private void dibujarMensaje(Graphics2D g, String texto) {
        g.setFont(EstiloBrutal.titulo(22));
        FontMetrics medidas = g.getFontMetrics();
        int ancho = medidas.stringWidth(texto) + 64;
        int alto = 72;
        int x = (EstadoJuego.ANCHO - ancho) / 2;
        int y = (EstadoJuego.ALTO - alto) / 2;

        EstiloBrutal.panel(g, x, y, ancho, alto, EstiloBrutal.PAPEL, 10);
        g.setColor(EstiloBrutal.NEGRO);
        g.drawString(texto, x + 32, y + 45);
    }
}
