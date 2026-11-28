package com.gemelosgravedad.modelo.entidades;

import com.gemelosgravedad.modelo.datos.Plataforma;
import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.recursos.GestorRecursos;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

/**
 * Uno de los dos gemelos controlados por el jugador.
 *
 * <p>{@code sentidoGravedad = 1} indica que camina por el piso y
 * {@code sentidoGravedad = -1} que cuelga del techo. Las plataformas permiten
 * apoyarse arriba con gravedad normal o colgarse debajo con gravedad
 * invertida.</p>
 *
 * @see Entidad
 * @see EstadoJuego
 */
public final class Jugador extends Entidad {

    /** Ancho del gemelo en estado normal. */
    public static final int ANCHO_NORMAL = 54;

    /** Alto del gemelo en estado normal. */
    public static final int ALTO_NORMAL = 60;

    /** Ancho del gemelo durante el turbo. */
    public static final int ANCHO_TURBO = 76;

    /** Alto del gemelo durante el turbo. */
    public static final int ALTO_TURBO = 84;

    private static final double GRAVEDAD = 0.65;
    private static final double IMPULSO = 16.0;
    private static final double IMPULSO_RESCATE = 0.6;
    private static final int MARGEN_HORIZONTAL = 24;
    private static final int MARGEN_APOYO = 2;
    private static final int DESTELLO_INTERCAMBIO = 22;
    private static final int DESTELLO_RESCATE = 18;

    private static final Color NEGRO = new Color(18, 18, 18);
    private static final Color AMARILLO = new Color(255, 211, 0);
    private static final Color CIAN_ESCUDO = new Color(80, 245, 255);
    private static final Color CIAN_ESCUDO_CLARO = new Color(140, 255, 255);
    private static final Color CIAN_RESPALDO = new Color(110, 248, 255);
    private static final Color NARANJA_TURBO = new Color(255, 145, 0);
    private static final Color NARANJA_RESPALDO = new Color(255, 150, 40);
    private static final Color SOMBRA = new Color(0, 0, 0, 140);

    private static final BasicStroke TRAZO_ESCUDO = new BasicStroke(3f);
    private static final BasicStroke TRAZO_DESTELLO = new BasicStroke(3f);
    private static final BasicStroke TRAZO_COLGANDO = new BasicStroke(3f,
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f,
            new float[]{6f, 4f}, 0f);
    private static final BasicStroke TRAZO_AURA = new BasicStroke(4f);
    private static final BasicStroke TRAZO_AURA_BORDE = new BasicStroke(2f);
    private static final BasicStroke TRAZO_MARCO = new BasicStroke(7f);
    private static final BasicStroke TRAZO_MARCO_DESTELLO = new BasicStroke(8f);

    private final Color color;
    private final Image[] spritesTurbo;
    private final Image[] spritesEscudo;

    private int sentidoGravedad;
    private double velocidadY;
    private boolean enSuelo;
    private boolean colgando;
    private boolean escudo;
    private boolean turbo;

    private int ultimoX;
    private int ultimoY;
    private int destello;

    /**
     * Crea un gemelo con su gravedad, color y animacion.
     *
     * @param x coordenada horizontal inicial
     * @param y coordenada vertical inicial
     * @param sentidoGravedad {@code 1} para el piso, {@code -1} para el techo
     * @param color color base del gemelo
     * @param carpeta carpeta de sprites dentro de {@code resources}
     */
    public Jugador(int x, int y, int sentidoGravedad, Color color, String carpeta) {
        super(x, y, ANCHO_NORMAL, ALTO_NORMAL);
        this.sentidoGravedad = sentidoGravedad;
        this.color = color;
        this.enSuelo = true;
        this.ultimoX = x;
        this.ultimoY = y;
        this.sprites = GestorRecursos.cargarCuadros(carpeta, 3);
        this.spritesTurbo = GestorRecursos.tintarCuadros(sprites,
                new Color(255, 140, 20), 0.58f);
        this.spritesEscudo = GestorRecursos.tintarCuadros(sprites,
                new Color(80, 245, 255), 0.48f);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Version sin plataformas; se conserva para cumplir el contrato de la
     * clase base y para pruebas aisladas.</p>
     */
    @Override
    public void actualizar() {
        actualizar(List.of());
    }

    /**
     * Aplica gravedad y resuelve el apoyo contra las plataformas.
     *
     * <p>El gemelo normal aterriza sobre el borde superior de una plataforma;
     * el invertido se cuelga de su borde inferior. El piso y el techo tambien
     * tienen huecos, por lo que caer al vacio es posible.</p>
     *
     * @param plataformas plataformas solidas de la partida
     */
    public void actualizar(List<Plataforma> plataformas) {
        contadorAnimacion++;
        if (destello > 0) {
            destello--;
        }
        if (colgando) {
            return;
        }

        int anteriorY = getY();
        velocidadY += GRAVEDAD * sentidoGravedad;
        int nuevoY = getY() + (int) Math.round(velocidadY);
        setY(nuevoY);

        boolean apoyo = false;
        if (sentidoGravedad == 1) {
            if (velocidadY >= 0) {
                int borde = buscarApoyoSuperior(plataformas, anteriorY, nuevoY);
                if (borde != Integer.MAX_VALUE) {
                    setY(borde - getAlto());
                    velocidadY = 0;
                    apoyo = true;
                }
            }
        } else if (velocidadY <= 0) {
            int borde = buscarApoyoInferior(plataformas, anteriorY, nuevoY);
            if (borde != Integer.MIN_VALUE) {
                setY(borde);
                velocidadY = 0;
                apoyo = true;
            }
        }

        enSuelo = apoyo;
        if (enSuelo) {
            ultimoX = getX();
            ultimoY = getY();
        }
    }

    /**
     * Busca la plataforma mas cercana sobre la que puede aterrizar un gemelo
     * con gravedad normal.
     *
     * @param plataformas plataformas de la partida
     * @param anteriorY posicion vertical antes de aplicar la gravedad
     * @param nuevoY posicion vertical despues de aplicar la gravedad
     * @return borde superior de apoyo o {@link Integer#MAX_VALUE} si no hay
     */
    private int buscarApoyoSuperior(List<Plataforma> plataformas, int anteriorY, int nuevoY) {
        return plataformas.stream()
                .filter(plataforma -> plataforma.apoyaA(1)
                        && plataforma.cubreHorizontalmente(getX(), getAncho()))
                .filter(plataforma -> anteriorY + getAlto() <= plataforma.y() + MARGEN_APOYO
                        && nuevoY + getAlto() >= plataforma.y())
                .mapToInt(Plataforma::y)
                .min()
                .orElse(Integer.MAX_VALUE);
    }

    /**
     * Busca la plataforma mas cercana de la que puede colgarse un gemelo con
     * gravedad invertida.
     *
     * @param plataformas plataformas de la partida
     * @param anteriorY posicion vertical antes de aplicar la gravedad
     * @param nuevoY posicion vertical despues de aplicar la gravedad
     * @return borde inferior de apoyo o {@link Integer#MIN_VALUE} si no hay
     */
    private int buscarApoyoInferior(List<Plataforma> plataformas, int anteriorY, int nuevoY) {
        return plataformas.stream()
                .filter(plataforma -> plataforma.apoyaA(-1)
                        && plataforma.cubreHorizontalmente(getX(), getAncho()))
                .filter(plataforma -> anteriorY >= plataforma.bordeInferior() - MARGEN_APOYO
                        && nuevoY <= plataforma.bordeInferior())
                .mapToInt(Plataforma::bordeInferior)
                .max()
                .orElse(Integer.MIN_VALUE);
    }

    /**
     * Mueve el gemelo horizontalmente dentro de los limites de la pantalla.
     *
     * @param direccion desplazamiento horizontal solicitado
     * @param anchoPantalla ancho visible de la partida
     */
    public void mover(int direccion, int anchoPantalla) {
        if (colgando) {
            return;
        }
        int nuevaX = getX() + direccion;
        int maximo = anchoPantalla - getAncho() - MARGEN_HORIZONTAL;
        if (nuevaX < MARGEN_HORIZONTAL) {
            nuevaX = MARGEN_HORIZONTAL;
        }
        if (nuevaX > maximo) {
            nuevaX = maximo;
        }
        setX(nuevaX);
    }

    /**
     * Salto en espejo: el gemelo del piso sube y el del techo baja.
     *
     * @return {@code true} si el salto se pudo realizar
     */
    public boolean saltar() {
        if (colgando || !enSuelo) {
            return false;
        }
        velocidadY = -IMPULSO * sentidoGravedad;
        enSuelo = false;
        return true;
    }

    /**
     * Cambia el tamano del gemelo por el power-up turbo, manteniendo el apoyo.
     *
     * @param nuevoAncho nuevo ancho en pixeles
     * @param nuevoAlto nuevo alto en pixeles
     */
    public void cambiarTamano(int nuevoAncho, int nuevoAlto) {
        if (colgando) {
            return;
        }
        int altoAnterior = getAlto();
        setAncho(nuevoAncho);
        setAlto(nuevoAlto);
        if (sentidoGravedad == 1) {
            setY(getY() + altoAnterior - nuevoAlto);
        }
    }

    /**
     * Intercambia posicion, gravedad y apoyo con el otro gemelo.
     *
     * <p>La soga no se rompe: solo cambia quien va arriba y quien va abajo. La
     * operacion se realiza en dos pasos para no perder el estado intermedio.</p>
     *
     * @param otro el otro gemelo de la partida
     */
    public void intercambiarCon(Jugador otro) {
        Instantanea propio = instantanea();
        Instantanea ajeno = otro.instantanea();
        restaurar(ajeno);
        otro.restaurar(propio);
        destello = DESTELLO_INTERCAMBIO;
        otro.destello = DESTELLO_INTERCAMBIO;
    }

    /**
     * Captura el estado que se intercambia entre gemelos.
     *
     * @return instantanea del estado actual
     */
    private Instantanea instantanea() {
        return new Instantanea(getX(), getY(), sentidoGravedad, velocidadY,
                enSuelo, colgando, ultimoX, ultimoY);
    }

    /**
     * Restaura un estado capturado previamente.
     *
     * @param estado estado que se quiere aplicar
     */
    private void restaurar(Instantanea estado) {
        setX(estado.x());
        setY(estado.y());
        sentidoGravedad = estado.sentidoGravedad();
        velocidadY = estado.velocidadY();
        enSuelo = estado.enSuelo();
        colgando = estado.colgando();
        ultimoX = estado.ultimoX();
        ultimoY = estado.ultimoY();
    }

    /**
     * Devuelve el gemelo al ultimo punto seguro tras caer al vacio.
     */
    public void reaparecer() {
        volverAlPuntoSeguro();
        velocidadY = 0;
        enSuelo = true;
    }

    /**
     * Devuelve al gemelo a su ultimo punto seguro y suelta la soga.
     */
    private void volverAlPuntoSeguro() {
        setX(ultimoX);
        setY(ultimoY);
        colgando = false;
    }

    /**
     * Deja al gemelo colgando de la soga en un hueco.
     *
     * @param y coordenada vertical en la que queda suspendido
     */
    public void colgar(int y) {
        colgando = true;
        setY(y);
        velocidadY = 0;
        enSuelo = false;
    }

    /**
     * Rescata al gemelo colgado y lo devuelve a su ultimo punto seguro con un
     * impulso.
     */
    public void rescatar() {
        volverAlPuntoSeguro();
        velocidadY = -IMPULSO * IMPULSO_RESCATE * sentidoGravedad;
        enSuelo = false;
        destello = DESTELLO_RESCATE;
    }

    /**
     * Activa el destello visual de rescate o intercambio.
     */
    public void destacar() {
        destello = DESTELLO_RESCATE;
    }

    /**
     * Indica si el gemelo esta colgando de la soga.
     *
     * @return {@code true} si esta suspendido en un hueco
     */
    public boolean isColgando() {
        return colgando;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dibujar(Graphics2D g) {
        if (turbo) {
            dibujarAura(g, NARANJA_TURBO);
        }
        if (escudo) {
            dibujarAura(g, CIAN_ESCUDO);
        }

        Image[] cuadrosActuales = sprites;
        if (turbo && spritesTurbo != null) {
            cuadrosActuales = spritesTurbo;
        } else if (escudo && spritesEscudo != null) {
            cuadrosActuales = spritesEscudo;
        }
        boolean conImagen = dibujarSprite(g, cuadrosActuales, sentidoGravedad < 0);
        if (!conImagen) {
            Color colorBase = color;
            if (turbo) {
                colorBase = NARANJA_RESPALDO;
            } else if (escudo) {
                colorBase = CIAN_RESPALDO;
            }
            dibujarRespaldo(g, colorBase);
        }

        if (escudo) {
            dibujarMarco(g, 6, TRAZO_MARCO, CIAN_ESCUDO_CLARO, TRAZO_ESCUDO);
        }
        if (destello > 0) {
            dibujarMarco(g, 5, TRAZO_MARCO_DESTELLO, AMARILLO, TRAZO_DESTELLO);
        }
        if (colgando) {
            dibujarMarco(g, 5, TRAZO_MARCO, AMARILLO, TRAZO_COLGANDO);
        }
    }

    /**
     * Dibuja un marco doble alrededor del gemelo.
     *
     * @param g contexto grafico
     * @param margen separacion respecto al rectangulo del gemelo
     * @param grosorExterno grosor del borde negro exterior
     * @param colorInterno color del borde interior
     * @param trazoInterno trazo del borde interior
     */
    private void dibujarMarco(Graphics2D g, int margen, BasicStroke trazoExterno,
                              Color colorInterno, BasicStroke trazoInterno) {
        int x = getX() - margen;
        int y = getY() - margen;
        int ancho = getAncho() + margen * 2;
        int alto = getAlto() + margen * 2;
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(NEGRO);
        g2.setStroke(trazoExterno);
        g2.drawRect(x, y, ancho, alto);
        g2.setColor(colorInterno);
        g2.setStroke(trazoInterno);
        g2.drawRect(x, y, ancho, alto);
        g2.dispose();
    }

    /**
     * Dibuja el aura de un power-up activo.
     *
     * @param g contexto grafico
     * @param colorAura color del aura
     */
    private void dibujarAura(Graphics2D g, Color colorAura) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(colorAura);
        g2.setStroke(TRAZO_AURA);
        g2.drawRect(getX() - 8, getY() - 8, getAncho() + 16, getAlto() + 16);
        g2.setColor(NEGRO);
        g2.setStroke(TRAZO_AURA_BORDE);
        g2.drawRect(getX() - 12, getY() - 12, getAncho() + 24, getAlto() + 24);
        g2.dispose();
    }

    /**
     * Dibuja una figura geometrica de respaldo cuando no hay sprites.
     *
     * @param g contexto grafico
     * @param colorBase color principal del gemelo
     */
    private void dibujarRespaldo(Graphics2D g, Color colorBase) {
        int x = getX();
        int y = getY();
        int ancho = getAncho();
        int alto = getAlto();
        boolean invertido = sentidoGravedad < 0;

        g.setColor(SOMBRA);
        g.fillRect(x + 4, y + 4, ancho, alto);
        g.setColor(NEGRO);
        g.fillRect(x, y, ancho, alto);
        g.setColor(colorBase.darker());
        g.fillRect(x + 4, y + 4, ancho - 8, alto - 8);
        g.setColor(colorBase);
        g.fillRect(x + 8, y + 8, ancho - 16, alto - 20);

        int ojosY = invertido ? y + alto - 26 : y + 15;
        g.setColor(Color.WHITE);
        g.fillRect(x + 12, ojosY, 12, 12);
        g.fillRect(x + ancho - 24, ojosY, 12, 12);
        g.setColor(NEGRO);
        g.fillRect(x + 16, ojosY + 4, 5, 5);
        g.fillRect(x + ancho - 20, ojosY + 4, 5, 5);

        g.setColor(NEGRO);
        g.fillRect(x + ancho / 2 - 2, y + 10, 4, alto - 20);
        g.setColor(colorBase.brighter());
        g.fillRect(x + ancho / 2 - 5, invertido ? y + alto - 9 : y + 2, 10, 7);
        g.setColor(NEGRO);
        g.fillRect(x + ancho / 2 - 2, invertido ? y + alto - 6 : y + 6, 4, 4);
    }

    /**
     * Indica si el escudo esta activo.
     *
     * @return {@code true} si el gemelo tiene escudo
     */
    public boolean isEscudo() {
        return escudo;
    }

    /**
     * Activa o desactiva el escudo.
     *
     * @param escudo nuevo estado del escudo
     */
    public void setEscudo(boolean escudo) {
        this.escudo = escudo;
    }

    /**
     * Indica si el turbo esta activo.
     *
     * @return {@code true} si el gemelo tiene turbo
     */
    public boolean isTurbo() {
        return turbo;
    }

    /**
     * Activa o desactiva el turbo.
     *
     * @param turbo nuevo estado del turbo
     */
    public void setTurbo(boolean turbo) {
        this.turbo = turbo;
    }

    /**
     * Devuelve el sentido de la gravedad actual.
     *
     * @return {@code 1} para el piso, {@code -1} para el techo
     */
    public int getSentidoGravedad() {
        return sentidoGravedad;
    }

    /**
     * Indica si el gemelo esta apoyado en una plataforma.
     *
     * @return {@code true} si esta en el suelo o colgado de una plataforma
     */
    public boolean isEnSuelo() {
        return enSuelo;
    }

    /**
     * Instantanea inmutable del estado que se intercambia entre gemelos.
     *
     * @param x coordenada horizontal
     * @param y coordenada vertical
     * @param sentidoGravedad sentido de la gravedad
     * @param velocidadY velocidad vertical
     * @param enSuelo estado de apoyo
     * @param colgando estado de la soga
     * @param ultimoX ultimo punto seguro horizontal
     * @param ultimoY ultimo punto seguro vertical
     */
    private record Instantanea(int x, int y, int sentidoGravedad, double velocidadY,
                               boolean enSuelo, boolean colgando, int ultimoX, int ultimoY) {
    }
}
