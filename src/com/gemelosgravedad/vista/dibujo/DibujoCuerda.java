package com.gemelosgravedad.vista.dibujo;

import com.gemelosgravedad.modelo.entidades.Jugador;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;

/**
 * Dibuja la soga que une a los dos gemelos.
 *
 * <p>Cuelga con holgura mientras caminan y se tensa en rojo cuando uno queda
 * atrapado en un hueco. La curva se calcula a partir de los centros de ambos
 * jugadores para que el dibujo siga funcionando tras un intercambio.</p>
 *
 * @see Jugador
 */
public final class DibujoCuerda {

    private static final Color CUERDA = new Color(12, 12, 12);
    private static final Color TENSA = new Color(96, 16, 12);
    private static final double LONGITUD_HOLGURA = 300.0;
    private static final double FACTOR_HOLGURA = 0.42;
    private static final double AMPLITUD_ONDA = 3.0;
    private static final double VELOCIDAD_ONDA = 0.06;
    private static final double VIBRACION = 1.8;
    private static final double VELOCIDAD_VIBRACION = 0.55;
    private static final double POSICION_ABRAZADERA_IZQUIERDA = 0.11;
    private static final double POSICION_ABRAZADERA_DERECHA = 0.89;
    private static final BasicStroke TRAZO_SOGA = new BasicStroke(8f,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke TRAZO_GUIA = new BasicStroke(2.5f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f,
            new float[]{7f, 7f}, 0f);
    private static final BasicStroke TRAZO_TENSA_FONDO = new BasicStroke(15f,
            BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke TRAZO_TENSA_ROJO = new BasicStroke(5f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

    private DibujoCuerda() {
    }

    /**
     * Dibuja la soga entre los dos gemelos.
     *
     * @param g contexto grafico
     * @param azul gemelo azul
     * @param amarillo gemelo amarillo
     * @param tick contador de pasos para la animacion
     */
    public static void dibujar(Graphics2D g, Jugador azul, Jugador amarillo, int tick) {
        if (azul == null || amarillo == null) {
            return;
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double x1 = azul.getX() + azul.getAncho() / 2.0;
        double y1 = azul.getY() + azul.getAlto() / 2.0;
        double x2 = amarillo.getX() + amarillo.getAncho() / 2.0;
        double y2 = amarillo.getY() + amarillo.getAlto() / 2.0;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double distancia = Math.max(1.0, Math.hypot(dx, dy));
        double perpX = -dy / distancia;
        double perpY = dx / distancia;

        if (azul.isColgando() || amarillo.isColgando()) {
            dibujarTensa(g, x1, y1, x2, y2, perpX, perpY, tick);
            return;
        }

        double holgura = Math.max(0.0, LONGITUD_HOLGURA - distancia);
        double medioX = (x1 + x2) / 2.0;
        double medioY = (y1 + y2) / 2.0;
        double lado = holgura * FACTOR_HOLGURA + Math.sin(tick * VELOCIDAD_ONDA) * AMPLITUD_ONDA;

        QuadCurve2D curva = new QuadCurve2D.Double(x1, y1,
                medioX + perpX * lado, medioY + perpY * lado, x2, y2);

        g.setColor(CUERDA);
        g.setStroke(TRAZO_SOGA);
        g.draw(curva);

        g.setColor(EstiloBrutal.AMARILLO);
        g.setStroke(TRAZO_GUIA);
        g.draw(curva);

        dibujarAbrazadera(g, punto(curva, POSICION_ABRAZADERA_IZQUIERDA));
        dibujarAbrazadera(g, punto(curva, POSICION_ABRAZADERA_DERECHA));

        Punto centro = punto(curva, 0.5);
        g.setColor(CUERDA);
        g.fillRect((int) centro.x() - 7, (int) centro.y() - 7, 14, 14);
        g.setColor(EstiloBrutal.AMARILLO);
        g.fillRect((int) centro.x() - 2, (int) centro.y() - 2, 4, 4);
    }

    /**
     * Dibuja la soga tensa con vibracion y un rombo de aviso.
     *
     * @param g contexto grafico
     * @param x1 coordenada horizontal del primer gemelo
     * @param y1 coordenada vertical del primer gemelo
     * @param x2 coordenada horizontal del segundo gemelo
     * @param y2 coordenada vertical del segundo gemelo
     * @param perpX componente horizontal de la perpendicular
     * @param perpY componente vertical de la perpendicular
     * @param tick contador de pasos
     */
    private static void dibujarTensa(Graphics2D g, double x1, double y1,
                                     double x2, double y2, double perpX,
                                     double perpY, int tick) {
        double vibracion = Math.sin(tick * VELOCIDAD_VIBRACION) * VIBRACION;
        double ax = x1 + perpX * vibracion;
        double ay = y1 + perpY * vibracion;
        double bx = x2 + perpX * vibracion;
        double by = y2 + perpY * vibracion;

        g.setColor(TENSA);
        g.setStroke(TRAZO_TENSA_FONDO);
        g.drawLine((int) ax, (int) ay, (int) bx, (int) by);

        g.setColor(CUERDA);
        g.setStroke(TRAZO_SOGA);
        g.drawLine((int) ax, (int) ay, (int) bx, (int) by);

        g.setColor(EstiloBrutal.ROJO);
        g.setStroke(TRAZO_TENSA_ROJO);
        g.drawLine((int) ax, (int) ay, (int) bx, (int) by);

        g.setColor(EstiloBrutal.AMARILLO);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                1f, new float[]{8f, 6f}, (float) (tick * 0.6)));
        g.drawLine((int) ax, (int) ay, (int) bx, (int) by);

        dibujarAbrazadera(g, new Punto(
                x1 + (x2 - x1) * POSICION_ABRAZADERA_IZQUIERDA,
                y1 + (y2 - y1) * POSICION_ABRAZADERA_IZQUIERDA));
        dibujarAbrazadera(g, new Punto(
                x1 + (x2 - x1) * POSICION_ABRAZADERA_DERECHA,
                y1 + (y2 - y1) * POSICION_ABRAZADERA_DERECHA));

        int mx = (int) Math.round((ax + bx) / 2.0);
        int my = (int) Math.round((ay + by) / 2.0);
        int pulso = 14 + (int) (Math.sin(tick * 0.2) * 3);
        Polygon diamante = new Polygon();
        diamante.addPoint(mx, my - pulso);
        diamante.addPoint(mx + pulso, my);
        diamante.addPoint(mx, my + pulso);
        diamante.addPoint(mx - pulso, my);
        g.setColor(EstiloBrutal.NEGRO);
        g.fillPolygon(diamante);

        int interior = pulso - 5;
        Polygon centro = new Polygon();
        centro.addPoint(mx, my - interior);
        centro.addPoint(mx + interior, my);
        centro.addPoint(mx, my + interior);
        centro.addPoint(mx - interior, my);
        g.setColor((tick / 10) % 2 == 0 ? EstiloBrutal.AMARILLO : EstiloBrutal.ROJO);
        g.fillPolygon(centro);

        g.setFont(EstiloBrutal.mono(13));
        g.setColor(EstiloBrutal.NEGRO);
        g.drawString("!", mx - 3, my + 5);
    }

    /**
     * Calcula un punto de la curva cuadratica.
     *
     * @param curva curva de la soga
     * @param t parametro entre cero y uno
     * @return punto interpolado
     */
    private static Punto punto(QuadCurve2D curva, double t) {
        double u = 1.0 - t;
        double x = u * u * curva.getX1() + 2 * u * t * curva.getCtrlX()
                + t * t * curva.getX2();
        double y = u * u * curva.getY1() + 2 * u * t * curva.getCtrlY()
                + t * t * curva.getY2();
        return new Punto(x, y);
    }

    /**
     * Dibuja una abrazadera metalica sobre la soga.
     *
     * @param g contexto grafico
     * @param punto posicion de la abrazadera
     */
    private static void dibujarAbrazadera(Graphics2D g, Punto punto) {
        int x = (int) Math.round(punto.x());
        int y = (int) Math.round(punto.y());
        g.setColor(CUERDA);
        g.fillRect(x - 8, y - 6, 16, 12);
        g.setColor(EstiloBrutal.AMARILLO);
        g.fillRect(x - 5, y - 3, 10, 6);
        g.setColor(CUERDA);
        g.fillRect(x - 2, y - 1, 4, 2);
    }

    /**
     * Punto bidimensional usado por la curva de la soga.
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     */
    private record Punto(double x, double y) {
    }
}
