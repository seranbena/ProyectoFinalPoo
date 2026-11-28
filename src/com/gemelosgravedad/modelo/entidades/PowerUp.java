package com.gemelosgravedad.modelo.entidades;

import com.gemelosgravedad.modelo.datos.TipoPowerUp;
import com.gemelosgravedad.modelo.juego.PoderActivo;
import com.gemelosgravedad.recursos.GestorRecursos;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;

/**
 * Power-up que se recoge y activa un efecto temporal en los dos gemelos.
 *
 * <p>Flota mientras avanza. El tipo determina la duracion, el sprite, la letra
 * de respaldo y el color, de modo que no haya cadenas sueltas en el resto del
 * codigo.</p>
 *
 * @see TipoPowerUp
 * @see PoderActivo
 */
public final class PowerUp extends ObjetoFlotante {

    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color SOMBRA = new Color(0, 0, 0, 90);
    private static final double AMPLITUD = 8.0;
    private static final double VELOCIDAD_ANGULAR = 0.08;
    private static final int FRECUENCIA_BRILLO = 6;
    private static final int TAMANO_LETRA = 20;

    private final TipoPowerUp tipo;
    private final Image imagen;

    /**
     * Crea un power-up del tipo indicado.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical central de la oscilacion
     * @param tipo tipo de power-up
     * @param velocidad velocidad de avance horizontal
     */
    public PowerUp(int x, int y, TipoPowerUp tipo, double velocidad) {
        super(x, y, 40, 40, velocidad, AMPLITUD, VELOCIDAD_ANGULAR);
        this.tipo = tipo;
        this.imagen = GestorRecursos.cargarImagen(tipo.getRutaSprite());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actualizar() {
        super.actualizar();
        actualizarFlotacion();
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
        Color color = tipo.getColor();

        if ((contadorAnimacion / FRECUENCIA_BRILLO) % 2 == 0) {
            g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 70));
            g.fillRect(x - 7, y - 7, ancho + 14, alto + 14);
        }

        if (imagen != null) {
            g.drawImage(imagen, x, y, ancho, alto, null);
            return;
        }

        g.setColor(SOMBRA);
        g.fillRect(x + 4, y + 4, ancho, alto);
        g.setColor(NEGRO);
        g.fillRect(x, y, ancho, alto);
        g.setColor(color);
        g.fillRect(x + 4, y + 4, ancho - 8, alto - 8);
        g.setColor(color.brighter());
        g.fillRect(x + 4, y + 4, ancho - 8, 5);
        g.setColor(NEGRO);
        g.fillRect(x + 4, y + alto - 9, ancho - 8, 5);
        g.fillRect(x + 10, y + 10, ancho - 20, alto - 20);

        g.setColor(color);
        g.setFont(new Font("Arial Black", Font.BOLD, TAMANO_LETRA));
        String letra = tipo.getLetra();
        int letraX = x + ancho / 2 - g.getFontMetrics().stringWidth(letra) / 2;
        g.drawString(letra, letraX, y + 29);

        g.setColor(NEGRO);
        g.fillRect(x + 6, y + 6, 4, 4);
        g.fillRect(x + ancho - 10, y + 6, 4, 4);
        g.fillRect(x + 6, y + alto - 10, 4, 4);
        g.fillRect(x + ancho - 10, y + alto - 10, 4, 4);
    }

    /**
     * Devuelve el tipo de power-up.
     *
     * @return tipo del power-up
     */
    public TipoPowerUp getTipo() {
        return tipo;
    }
}
