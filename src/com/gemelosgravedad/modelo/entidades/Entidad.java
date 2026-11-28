package com.gemelosgravedad.modelo.entidades;

import com.gemelosgravedad.modelo.interfaces.Actualizable;
import com.gemelosgravedad.modelo.interfaces.Dibujable;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;

/**
 * Clase base de todo lo que se mueve o se dibuja durante una partida.
 *
 * <p>Concentra el estado comun de las entidades (posicion, tamano, visibilidad
 * y animacion) y obliga a las subclases a definir como se actualizan y como se
 * dibujan. Las colisiones se resuelven con rectangulos alineados a los ejes
 * (AABB), suficientes para este juego.</p>
 *
 * @see EntidadDesplazable
 * @see Jugador
 */
public abstract sealed class Entidad implements Actualizable, Dibujable
        permits Jugador, EntidadDesplazable {

    /** Fotogramas que se mantiene visible cada cuadro de una animacion. */
    private static final int FOTOGRAMAS_POR_CUADRO = 8;

    private int x;
    private int y;
    private int ancho;
    private int alto;
    private boolean activa;

    /**
     * Contador de fotogramas usado por las subclases para animar sus sprites.
     * Cada subclase decide cuando incrementarlo.
     */
    protected int contadorAnimacion;

    /**
     * Cuadros de animacion cargados desde {@code resources/sprites}. Puede ser
     * {@code null} o contener elementos {@code null}; en ese caso la subclase
     * dibuja una figura de respaldo.
     */
    protected Image[] sprites;

    /**
     * Crea una entidad con posicion y tamano iniciales.
     *
     * @param x coordenada horizontal del borde izquierdo
     * @param y coordenada vertical del borde superior
     * @param ancho ancho del rectangulo de colision
     * @param alto alto del rectangulo de colision
     */
    public Entidad(int x, int y, int ancho, int alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.activa = true;
        this.contadorAnimacion = 0;
    }

    /**
     * Actualiza el estado de la entidad en un paso de simulacion.
     */
    @Override
    public abstract void actualizar();

    /**
     * Dibuja la entidad sobre el contexto grafico indicado.
     *
     * @param g contexto grafico de la partida
     */
    @Override
    public abstract void dibujar(Graphics2D g);

    /**
     * Comprueba si esta entidad se solapa con otra y ambas estan activas.
     *
     * @param otra entidad con la que se quiere comprobar la colision
     * @return {@code true} si los rectangulos se solapan
     */
    public boolean colisionaCon(Entidad otra) {
        return activa && otra.activa
                && x < otra.x + otra.ancho
                && x + ancho > otra.x
                && y < otra.y + otra.alto
                && y + alto > otra.y;
    }

    /**
     * Dibuja el cuadro actual de la animacion principal.
     *
     * @param g contexto grafico
     * @param voltear {@code true} para dibujar el sprite invertido verticalmente
     * @return {@code false} si no hay sprite disponible para dibujar
     */
    protected boolean dibujarSprite(Graphics2D g, boolean voltear) {
        return dibujarSprite(g, sprites, voltear);
    }

    /**
     * Dibuja un cuadro de la animacion indicada.
     *
     * @param g contexto grafico
     * @param cuadros cuadros de animacion; puede ser {@code null}
     * @param voltear {@code true} para invertir el sprite verticalmente
     * @return {@code false} si no hay una imagen valida que dibujar
     */
    protected boolean dibujarSprite(Graphics2D g, Image[] cuadros, boolean voltear) {
        if (cuadros == null || cuadros.length == 0) {
            return false;
        }
        int indice = (contadorAnimacion / FOTOGRAMAS_POR_CUADRO) % cuadros.length;
        Image imagen = cuadros[indice];
        if (imagen == null) {
            return false;
        }
        if (voltear) {
            g.drawImage(imagen, x, y + alto, ancho, -alto, null);
        } else {
            g.drawImage(imagen, x, y, ancho, alto, null);
        }
        return true;
    }

    /**
     * Construye un rombo centrado en la entidad. Se usa para dibujar cristales
     * y orbes sin repetir la lista de vertices en cada subclase.
     *
     * @param margen separacion interior respecto a los bordes del rectangulo
     * @param desplazamientoX desplazamiento horizontal del rombo
     * @param desplazamientoY desplazamiento vertical del rombo
     * @return poligono de cuatro vertices con forma de rombo
     */
    protected Polygon crearDiamante(int margen, int desplazamientoX, int desplazamientoY) {
        int centroX = x + ancho / 2 + desplazamientoX;
        int centroY = y + alto / 2 + desplazamientoY;
        int izquierda = x + margen + desplazamientoX;
        int derecha = x + ancho - margen + desplazamientoX;
        int arriba = y + margen + desplazamientoY;
        int abajo = y + alto - margen + desplazamientoY;

        Polygon diamante = new Polygon();
        diamante.addPoint(centroX, arriba);
        diamante.addPoint(derecha, centroY);
        diamante.addPoint(centroX, abajo);
        diamante.addPoint(izquierda, centroY);
        return diamante;
    }

    /**
     * Devuelve la coordenada horizontal del borde izquierdo.
     *
     * @return coordenada X
     */
    public int getX() {
        return x;
    }

    /**
     * Establece la coordenada horizontal del borde izquierdo.
     *
     * @param x nueva coordenada X
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Devuelve la coordenada vertical del borde superior.
     *
     * @return coordenada Y
     */
    public int getY() {
        return y;
    }

    /**
     * Establece la coordenada vertical del borde superior.
     *
     * @param y nueva coordenada Y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Devuelve el ancho del rectangulo de colision.
     *
     * @return ancho en pixeles
     */
    public int getAncho() {
        return ancho;
    }

    /**
     * Establece el ancho del rectangulo de colision.
     *
     * @param ancho nuevo ancho en pixeles
     */
    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    /**
     * Devuelve el alto del rectangulo de colision.
     *
     * @return alto en pixeles
     */
    public int getAlto() {
        return alto;
    }

    /**
     * Establece el alto del rectangulo de colision.
     *
     * @param alto nuevo alto en pixeles
     */
    public void setAlto(int alto) {
        this.alto = alto;
    }

    /**
     * Indica si la entidad sigue participando en la partida.
     *
     * @return {@code true} si esta activa
     */
    public boolean isActiva() {
        return activa;
    }

    /**
     * Activa o desactiva la entidad. Las entidades inactivas se eliminan de la
     * lista de actualizacion.
     *
     * @param activa nuevo estado de actividad
     */
    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
