package com.gemelosgravedad.modelo.juego;

import com.gemelosgravedad.modelo.datos.Efecto;
import com.gemelosgravedad.modelo.datos.Plataforma;
import com.gemelosgravedad.modelo.entidades.Enemigo;
import com.gemelosgravedad.modelo.entidades.Entidad;
import com.gemelosgravedad.modelo.entidades.Jugador;
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

    /**
     * Separacion horizontal maxima entre los centros de los gemelos antes de
     * que la soga frene al que se aleja.
     */
    public static final int DISTANCIA_MAXIMA_SOGA = 420;

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
    private final boolean dosJugadores;
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
     * Crea una partida nueva para un solo jugador.
     *
     * @param nombreJugador nombre que se mostrara en el HUD y el historial
     */
    public EstadoJuego(String nombreJugador) {
        this(nombreJugador, false);
    }

    /**
     * Crea una partida nueva en el modo indicado.
     *
     * <p>En el modo de dos jugadores cada uno controla un gemelo, pero las
     * vidas, el puntaje y los power-ups siguen siendo compartidos.</p>
     *
     * @param nombreJugador nombre individual o combinado del equipo
     * @param dosJugadores {@code true} para el modo cooperativo
     */
    public EstadoJuego(String nombreJugador, boolean dosJugadores) {
        this.nombreJugador = nombreJugador;
        this.dosJugadores = dosJugadores;
        this.jugadorAzul = new Jugador(X_INICIAL_JUGADORES, PISO - Jugador.ALTO_NORMAL, 1,
                new Color(64, 170, 255), "sprites/jugador_azul");
        this.jugadorAmarillo = new Jugador(X_INICIAL_JUGADORES, TECHO, -1,
                new Color(255, 210, 60), "sprites/jugador_amarillo");
        this.plataformas = List.copyOf(Nivel.crearPlataformas());
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

        for (Entidad entidad : entidades) {
            entidad.actualizar();
        }
        entidades.removeIf(entidad -> !entidad.isActiva()
                || entidad.getX() + entidad.getAncho() < -MARGEN_SALIDA_ENTIDAD);
        colisiones.revisar();
    }

    /**
     * Traslada las entidades generadas por el otro hilo a la lista principal.
     */
    private void recogerPendientes() {
        synchronized (pendientes) {
            entidades.addAll(pendientes);
            pendientes.clear();
        }
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
     * <p>Se conserva para el modo de un jugador, donde las dos mitades del
     * equipo comparten el control.</p>
     *
     * @param direccion {@code -1} a la izquierda, {@code 1} a la derecha
     */
    public synchronized void mover(int direccion) {
        if (direccion == 0 || marcador.isJuegoTerminado()) {
            return;
        }
        int paso = pasoMovimiento();
        jugadorAzul.mover(direccion * paso, ANCHO);
        jugadorAmarillo.mover(direccion * paso, ANCHO);
    }

    /**
     * Mueve solo al gemelo indicado y aplica el limite de la soga.
     *
     * <p>Es la entrada usada por el modo de dos jugadores: cada jugador mueve
     * su propio gemelo sin arrastrar al companero. Si la soga queda tensa, el
     * que se aleja se detiene para no separar al equipo.</p>
     *
     * @param jugador gemelo que recibe la orden
     * @param direccion {@code -1} a la izquierda, {@code 1} a la derecha
     */
    public synchronized void mover(Jugador jugador, int direccion) {
        if (jugador == null || direccion == 0 || marcador.isJuegoTerminado()) {
            return;
        }
        int paso = pasoMovimiento();
        Jugador companero = jugador == jugadorAzul ? jugadorAmarillo : jugadorAzul;
        int distanciaAntes = distanciaHorizontal(jugador, companero);
        jugador.mover(direccion * paso, ANCHO);
        limitarSoga(jugador, companero, distanciaAntes, direccion);
    }

    /**
     * Frena al gemelo que intenta aumentar la separacion mas alla de la soga.
     *
     * <p>Si la separacion ya supera el limite (por ejemplo, porque el otro
     * gemelo quedo colgado), el movimiento que la reduce se permite. Solo se
     * bloquea el movimiento que la aumenta.</p>
     *
     * @param jugador gemelo que acaba de moverse
     * @param companero otro gemelo de la partida
     * @param distanciaAntes separacion horizontal antes del movimiento
     * @param direccion direccion solicitada
     */
    private void limitarSoga(Jugador jugador, Jugador companero,
                             int distanciaAntes, int direccion) {
        int distanciaDespues = distanciaHorizontal(jugador, companero);
        if (distanciaDespues <= DISTANCIA_MAXIMA_SOGA
                || distanciaDespues <= distanciaAntes) {
            return;
        }
        int limite = Math.max(DISTANCIA_MAXIMA_SOGA, distanciaAntes);
        int exceso = distanciaDespues - limite;
        jugador.mover(direccion > 0 ? -exceso : exceso, ANCHO);
    }

    /**
     * Calcula la separacion horizontal entre los centros de dos gemelos.
     *
     * @param uno primer gemelo
     * @param otro segundo gemelo
     * @return distancia en pixeles entre sus centros
     */
    private int distanciaHorizontal(Jugador uno, Jugador otro) {
        int centroUno = uno.getX() + uno.getAncho() / 2;
        int centroOtro = otro.getX() + otro.getAncho() / 2;
        return Math.abs(centroUno - centroOtro);
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
     * Intenta saltar con un solo gemelo o rescatar al companero colgado.
     *
     * <p>Si el jugador indicado es el que esta colgado, no puede saltar: debe
     * esperar a que su companero lo rescate.</p>
     *
     * @param jugador gemelo que recibe la orden de salto
     * @return {@code true} si el salto o el rescate se realizaron
     */
    public synchronized boolean saltar(Jugador jugador) {
        if (jugador == null || marcador.isJuegoTerminado() || jugador.isColgando()) {
            return false;
        }
        Jugador colgado = getJugadorColgadoInterno();
        if (colgado != null) {
            if (jugador.isEnSuelo() && jugador.saltar()) {
                colgado.rescatar();
                jugador.destacar();
                return true;
            }
            return false;
        }
        return jugador.saltar();
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
                enemigo.acelerar(INCREMENTO_VELOCIDAD_ENEMIGO);
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
        synchronized (efectos) {
            List<Efecto> copia = List.copyOf(efectos);
            efectos.clear();
            return copia;
        }
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
     * Devuelve el nombre del jugador o del equipo.
     *
     * @return nombre introducido al comenzar la partida
     */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /**
     * Indica si la partida es cooperativa para dos jugadores.
     *
     * @return {@code true} si cada jugador controla un gemelo
     */
    public boolean isDosJugadores() {
        return dosJugadores;
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
        return jugadorAmarillo.isColgando() ? jugadorAmarillo : null;
    }

    /**
     * Devuelve el paso horizontal segun el estado de la soga.
     *
     * @return paso normal o reducido cuando un gemelo esta colgado
     */
    private int pasoMovimiento() {
        return getJugadorColgadoInterno() == null ? PASO_NORMAL : PASO_SOGA_TENSA;
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
