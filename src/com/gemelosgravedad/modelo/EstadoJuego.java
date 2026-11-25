package com.gemelosgravedad.modelo;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Estado completo de una partida y reglas que se aplican en cada paso.
 *
 * <p>Esta clase no depende de Swing: la vista solo lee sus datos y el
 * controlador le envia las entradas del teclado. La lista de entidades
 * pendientes y la de efectos se comparten con el hilo generador, por lo que
 * solo esas estructuras requieren sincronizacion adicional.</p>
 *
 * @see Jugador
 * @see GestorColisiones
 * @see Nivel
 */
public class EstadoJuego {

    /** Ancho logico de la partida en pixeles. */
    public static final int ANCHO = 1000;

    /** Alto logico de la partida en pixeles. */
    public static final int ALTO = 640;

    /** Altura de la banda superior del escenario. */
    public static final int TECHO = 64;

    /** Coordenada vertical donde comienza el piso. */
    public static final int PISO = 580;

    private static final double VELOCIDAD_BASE_INICIAL = 4.0;
    private static final double INCREMENTO_VELOCIDAD_BASE = 0.6;
    private static final double INCREMENTO_VELOCIDAD_ENEMIGO = 0.35;
    private static final int PASO_NORMAL = 6;
    private static final int PASO_SOGA_TENSA = 1;
    private static final int MARGEN_CAIDA_PISO = 50;
    private static final int MARGEN_CAIDA_TECHO = 10;
    private static final int Y_COLGADO_PISO = PISO - 40;
    private static final int Y_COLGADO_TECHO = TECHO + 30;
    private static final int MARGEN_VACIO = 40;
    private static final int MARGEN_SALIDA_ENTIDAD = 20;
    private static final int X_INICIAL_JUGADORES = 150;

    private final String nombreJugador;
    private final Jugador jugadorAzul;
    private final Jugador jugadorAmarillo;
    private final List<Plataforma> plataformas;
    private final List<Entidad> entidades = new ArrayList<>();
    private final List<Entidad> pendientes =
            Collections.synchronizedList(new ArrayList<>());
    private final List<Efecto> efectos =
            Collections.synchronizedList(new ArrayList<>());
    private final Marcador marcador = new Marcador();
    private final PoderActivo poder = new PoderActivo();
    private final GestorColisiones colisiones;

    private volatile int tick;
    private volatile double velocidadBase = VELOCIDAD_BASE_INICIAL;

    /**
     * Crea una partida nueva para el jugador indicado.
     *
     * @param nombreJugador nombre que se mostrara en el HUD y el historial
     */
    public EstadoJuego(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        this.jugadorAzul = new Jugador(X_INICIAL_JUGADORES, PISO - Jugador.ALTO_NORMAL, 1,
                new Color(64, 170, 255), "sprites/jugador_azul");
        this.jugadorAmarillo = new Jugador(X_INICIAL_JUGADORES, TECHO, -1,
                new Color(255, 210, 60), "sprites/jugador_amarillo");
        this.plataformas = Nivel.crearPlataformas();
        this.colisiones = new GestorColisiones(jugadorAzul, jugadorAmarillo,
                entidades, efectos, marcador, poder);
    }

    /**
     * Avanza un paso de simulacion: recoge entidades pendientes, mueve a los
     * jugadores, revisa caidas, actualiza enemigos y resuelve colisiones.
     *
     * <p>Se sincroniza porque el temporizador de Swing y el bucle del juego
     * pueden tocar el estado. Las secciones son cortas y no afectan a los
     * sesenta fotogramas por segundo.</p>
     */
    public synchronized void actualizar() {
        if (marcador.isJuegoTerminado()) {
            return;
        }
        tick++;
        recogerPendientes();
        jugadorAzul.actualizar(plataformas);
        jugadorAmarillo.actualizar(plataformas);
        revisarCaidas();

        for (int i = entidades.size() - 1; i >= 0; i--) {
            Entidad entidad = entidades.get(i);
            entidad.actualizar();
            if (!entidad.isActiva()
                    || entidad.getX() + entidad.getAncho() < -MARGEN_SALIDA_ENTIDAD) {
                entidades.remove(i);
            }
        }
        colisiones.revisar();
    }

    /**
     * Traslada las entidades generadas por el otro hilo a la lista principal.
     */
    private void recogerPendientes() {
        List<Entidad> copia;
        synchronized (pendientes) {
            copia = new ArrayList<>(pendientes);
            pendientes.clear();
        }
        entidades.addAll(copia);
    }

    /**
     * La soga atrapa al gemelo que cae si el otro sigue apoyado. Solo se
     * pierde una vida cuando los dos quedan en el vacio: ahi caen juntos.
     */
    private void revisarCaidas() {
        if (enVacio(jugadorAzul) && enVacio(jugadorAmarillo)) {
            perderVidaPorCaida();
            return;
        }
        revisarCaida(jugadorAzul, jugadorAmarillo);
        revisarCaida(jugadorAmarillo, jugadorAzul);
    }

    /**
     * Cuelga al jugador si paso el borde y su companero sigue apoyado.
     *
     * @param jugador jugador que puede haber caido
     * @param companero jugador que puede servir de ancla
     */
    private void revisarCaida(Jugador jugador, Jugador companero) {
        if (jugador.isColgando()) {
            return;
        }
        boolean pasoBorde = jugador.getSentidoGravedad() == 1
                ? jugador.getY() > PISO - MARGEN_CAIDA_PISO
                : jugador.getY() < TECHO - MARGEN_CAIDA_TECHO;
        if (!pasoBorde) {
            return;
        }
        if (companero.isEnSuelo() && !companero.isColgando()) {
            int yColgado = jugador.getSentidoGravedad() == 1
                    ? Y_COLGADO_PISO
                    : Y_COLGADO_TECHO;
            jugador.colgar(yColgado);
        }
    }

    /**
     * Indica si un jugador ya salio completamente de la zona jugable.
     *
     * @param jugador jugador que se quiere comprobar
     * @return {@code true} si esta en el vacio
     */
    private boolean enVacio(Jugador jugador) {
        if (jugador.isColgando()) {
            return true;
        }
        if (jugador.getSentidoGravedad() == 1) {
            return jugador.getY() > ALTO + MARGEN_VACIO;
        }
        return jugador.getY() + jugador.getAlto() < -MARGEN_VACIO;
    }

    /**
     * Resta una vida y devuelve a ambos gemelos a su ultimo punto seguro.
     */
    private void perderVidaPorCaida() {
        marcador.perderVida();
        efectos.add(marcador.isJuegoTerminado() ? Efecto.GAME_OVER : Efecto.GOLPE);
        jugadorAzul.reaparecer();
        jugadorAmarillo.reaparecer();
    }

    /**
     * Mueve a los dos gemelos en la direccion indicada.
     *
     * @param direccion {@code -1} a la izquierda, {@code 1} a la derecha
     */
    public synchronized void mover(int direccion) {
        if (direccion == 0 || marcador.isJuegoTerminado()) {
            return;
        }
        int paso = getJugadorColgadoInterno() == null ? PASO_NORMAL : PASO_SOGA_TENSA;
        jugadorAzul.mover(direccion * paso, ANCHO);
        jugadorAmarillo.mover(direccion * paso, ANCHO);
    }

    /**
     * Intenta saltar con ambos gemelos o rescatar al que esta colgado.
     *
     * @return {@code true} si al menos un gemelo pudo saltar
     */
    public synchronized boolean saltar() {
        if (marcador.isJuegoTerminado()) {
            return false;
        }
        Jugador colgado = getJugadorColgadoInterno();
        if (colgado != null) {
            Jugador ancla = colgado == jugadorAzul ? jugadorAmarillo : jugadorAzul;
            if (ancla.isEnSuelo() && !ancla.isColgando() && ancla.saltar()) {
                colgado.rescatar();
                ancla.destacar();
                return true;
            }
            return false;
        }
        boolean saltoAzul = jugadorAzul.saltar();
        boolean saltoAmarillo = jugadorAmarillo.saltar();
        return saltoAzul || saltoAmarillo;
    }

    /**
     * Actualiza el cronometro, la invulnerabilidad, los power-ups y la
     * dificultad. Lo llama el temporizador de Swing cada segundo.
     */
    public synchronized void actualizarSegundo() {
        if (marcador.isJuegoTerminado()) {
            return;
        }
        marcador.sumarSegundo();
        marcador.actualizarSegundo();
        poder.actualizarSegundo(jugadorAzul, jugadorAmarillo);
        if (marcador.getSegundos() % Marcador.SEGUNDOS_POR_NIVEL == 0) {
            subirDificultad();
        }
    }

    /**
     * Aumenta la velocidad base y la de todos los enemigos activos.
     */
    private void subirDificultad() {
        velocidadBase += INCREMENTO_VELOCIDAD_BASE;
        for (Entidad entidad : entidades) {
            if (entidad instanceof Enemigo enemigo) {
                enemigo.aumentarVelocidad(INCREMENTO_VELOCIDAD_ENEMIGO);
            }
        }
    }

    /**
     * Agrega una entidad generada por el hilo productor. La lista es segura
     * para acceso concurrente.
     *
     * @param entidad entidad que se incorporara en el siguiente paso
     */
    public void agregarPendiente(Entidad entidad) {
        pendientes.add(entidad);
    }

    /**
     * Devuelve y vacia los efectos pendientes de reproducir.
     *
     * @return copia de los efectos acumulados
     */
    public List<Efecto> consumirEfectos() {
        List<Efecto> copia;
        synchronized (efectos) {
            copia = new ArrayList<>(efectos);
            efectos.clear();
        }
        return copia;
    }

    /**
     * Devuelve una copia de las entidades para que la vista pueda dibujarlas
     * sin riesgo de modificacion concurrente.
     *
     * @return copia de la lista de entidades
     */
    public synchronized List<Entidad> obtenerEntidadesCopia() {
        return new ArrayList<>(entidades);
    }

    /**
     * Devuelve el nombre del jugador.
     *
     * @return nombre introducido al comenzar la partida
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Devuelve el gemelo azul.
     *
     * @return jugador azul
     */
    public Jugador getJugadorAzul() {
        return jugadorAzul;
    }

    /**
     * Devuelve el gemelo amarillo.
     *
     * @return jugador amarillo
     */
    public Jugador getJugadorAmarillo() {
        return jugadorAmarillo;
    }

    /**
     * Devuelve el gemelo que la soga dejo colgando en un hueco.
     *
     * @return jugador colgado o {@code null} si los dos estan apoyados
     */
    public synchronized Jugador getJugadorColgado() {
        return getJugadorColgadoInterno();
    }

    /**
     * Busca al jugador colgado sin sincronizar; se usa desde metodos que ya
     * tienen el bloqueo.
     *
     * @return jugador colgado o {@code null}
     */
    private Jugador getJugadorColgadoInterno() {
        if (jugadorAzul.isColgando()) {
            return jugadorAzul;
        }
        if (jugadorAmarillo.isColgando()) {
            return jugadorAmarillo;
        }
        return null;
    }

    /**
     * Devuelve las plataformas del escenario.
     *
     * @return lista de plataformas
     */
    public List<Plataforma> getPlataformas() {
        return plataformas;
    }

    /**
     * Devuelve el marcador de la partida.
     *
     * @return marcador con vidas, puntos y tiempo
     */
    public Marcador getMarcador() {
        return marcador;
    }

    /**
     * Devuelve el control del power-up activo.
     *
     * @return estado del power-up
     */
    public PoderActivo getPoder() {
        return poder;
    }

    /**
     * Devuelve el contador de pasos de simulacion.
     *
     * @return numero de ticks transcurridos
     */
    public int getTick() {
        return tick;
    }

    /**
     * Devuelve la velocidad base usada para generar objetos.
     *
     * @return velocidad base actual
     */
    public double getVelocidadBase() {
        return velocidadBase;
    }
}
