package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.Efecto;
import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.recursos.GestorSonidos;
import com.gemelosgravedad.vista.PanelJuego;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Une el modelo con la vista: teclado, hilos y transiciones de fin de partida.
 *
 * <p>El controlador no aplica reglas del juego. Solo traduce las teclas a
 * llamadas del {@link EstadoJuego}, arranca y detiene los hilos y reproduce los
 * efectos sonoros que publica el modelo.</p>
 *
 * @see EstadoJuego
 * @see ControladorNavegacion
 */
public final class ControladorJuego implements KeyListener {

    private final PanelJuego panel;
    private final ControladorNavegacion navegacion;
    private final TemporizadoresJuego temporizadores;

    private EstadoJuego estado;
    private HiloJuego hiloJuego;
    private HiloGenerador hiloGenerador;
    private Thread hebraJuego;
    private Thread hebraGenerador;

    private String nombreJugador = "";
    private volatile boolean izquierda;
    private volatile boolean derecha;
    private volatile boolean saltoMantenido;
    private volatile boolean partidaActiva;

    /**
     * Crea el controlador de una partida.
     *
     * @param panel panel donde se dibuja el juego
     * @param navegacion controlador que cambia entre pantallas
     */
    public ControladorJuego(PanelJuego panel, ControladorNavegacion navegacion) {
        this.panel = panel;
        this.navegacion = navegacion;
        this.temporizadores = new TemporizadoresJuego(panel);
        panel.setFocusable(true);
        panel.addKeyListener(this);
    }

    /**
     * Prepara una partida nueva y arranca sus hilos y temporizadores.
     *
     * @param nombre nombre del jugador que se mostrara en el HUD
     */
    public void iniciarPartida(String nombre) {
        detener();
        nombreJugador = nombre;
        estado = new EstadoJuego(nombre);
        panel.setEstado(estado);
        panel.setPausado(false);
        izquierda = false;
        derecha = false;
        saltoMantenido = false;
        partidaActiva = true;
        temporizadores.setEstado(estado);

        hiloJuego = new HiloJuego(this, panel);
        hiloGenerador = new HiloGenerador(estado);
        hebraJuego = new Thread(hiloJuego, "HiloJuego");
        hebraGenerador = new Thread(hiloGenerador, "HiloGenerador");
        hebraJuego.start();
        hebraGenerador.start();
        temporizadores.iniciar();
        panel.repaint();
        panel.pedirFoco();
    }

    /**
     * Ejecuta un paso del bucle: lee el teclado, actualiza el modelo y revisa
     * si la partida termino.
     *
     * @return {@code true} mientras la partida deba continuar
     */
    public boolean pasoJuego() {
        EstadoJuego actual = estado;
        if (actual == null || !partidaActiva) {
            return false;
        }
        int direccion = 0;
        if (izquierda) {
            direccion--;
        }
        if (derecha) {
            direccion++;
        }
        actual.mover(direccion);
        actual.actualizar();
        reproducirEfectos(actual);

        if (actual.getMarcador().isJuegoTerminado()) {
            finalizarPartida(actual);
            return false;
        }
        return true;
    }

    /**
     * Reproduce los sonidos de los efectos acumulados por el modelo.
     *
     * <p>El sonido de fin de partida se deja para {@link #finalizarPartida}
     * para que suene una sola vez.</p>
     *
     * @param actual partida de la que se consumen los efectos
     */
    private void reproducirEfectos(EstadoJuego actual) {
        for (Efecto efecto : actual.consumirEfectos()) {
            if (efecto != Efecto.GAME_OVER) {
                GestorSonidos.reproducir(efecto.getClaveSonido());
            }
        }
    }

    /**
     * Cierra la partida y muestra la pantalla de resultado.
     *
     * @param actual partida que acaba de terminar
     */
    private void finalizarPartida(EstadoJuego actual) {
        if (!partidaActiva) {
            return;
        }
        partidaActiva = false;
        temporizadores.detener();
        detenerHilos(false);
        GestorSonidos.reproducir(Efecto.GAME_OVER.getClaveSonido());
        navegacion.mostrarGameOver(nombreJugador,
                actual.getMarcador().getPuntaje(),
                actual.getMarcador().getSegundos());
    }

    /**
     * Pausa o reanuda la partida.
     */
    private void alternarPausa() {
        temporizadores.alternarPausa();
        boolean pausado = temporizadores.isPausado();
        panel.setPausado(pausado);
        if (hiloGenerador != null) {
            hiloGenerador.setPausado(pausado);
        }
        panel.repaint();
        panel.pedirFoco();
    }

    /**
     * Detiene la partida y vuelve al menu principal.
     */
    private void abandonarPartida() {
        detener();
        panel.setPausado(false);
        navegacion.volverAlMenu();
    }

    /**
     * Detiene la partida actual y todos sus hilos.
     */
    public void detener() {
        partidaActiva = false;
        temporizadores.detener();
        detenerHilos(true);
    }

    /**
     * Detiene los hilos de juego y generacion.
     *
     * @param interrumpirJuego {@code true} si tambien se debe interrumpir el
     *                         hilo que esta ejecutando el paso actual
     */
    private void detenerHilos(boolean interrumpirJuego) {
        if (hiloJuego != null) {
            hiloJuego.detener();
        }
        if (hiloGenerador != null) {
            hiloGenerador.detener();
        }
        if (interrumpirJuego && hebraJuego != null) {
            hebraJuego.interrupt();
        }
        if (hebraGenerador != null) {
            hebraGenerador.interrupt();
        }
    }

    /**
     * Procesa las teclas de control, pausa y abandono.
     *
     * @param e evento de teclado
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!partidaActiva) {
            return;
        }
        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_P) {
            alternarPausa();
            return;
        }
        if (tecla == KeyEvent.VK_ESCAPE) {
            abandonarPartida();
            return;
        }
        if (temporizadores.isPausado()) {
            return;
        }
        if (tecla == KeyEvent.VK_LEFT || tecla == KeyEvent.VK_A) {
            izquierda = true;
        } else if (tecla == KeyEvent.VK_RIGHT || tecla == KeyEvent.VK_D) {
            derecha = true;
        } else if (tecla == KeyEvent.VK_SPACE && !saltoMantenido) {
            EstadoJuego actual = estado;
            if (actual != null && actual.saltar()) {
                GestorSonidos.reproducir(GestorSonidos.SALTO);
            }
            saltoMantenido = true;
        }
    }

    /**
     * Libera las teclas de movimiento y salto.
     *
     * @param e evento de teclado
     */
    @Override
    public void keyReleased(KeyEvent e) {
        int tecla = e.getKeyCode();
        if (tecla == KeyEvent.VK_LEFT || tecla == KeyEvent.VK_A) {
            izquierda = false;
        } else if (tecla == KeyEvent.VK_RIGHT || tecla == KeyEvent.VK_D) {
            derecha = false;
        } else if (tecla == KeyEvent.VK_SPACE) {
            saltoMantenido = false;
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>El juego no usa caracteres tipados.</p>
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // Sin accion: solo se reacciona a pulsaciones y liberaciones.
    }

    /**
     * Indica si la partida esta pausada.
     *
     * @return {@code true} si el cronometro esta detenido por pausa
     */
    public boolean isPausado() {
        return temporizadores.isPausado();
    }

    /**
     * Indica si hay una partida en curso.
     *
     * @return {@code true} mientras la partida siga activa
     */
    public boolean isPartidaActiva() {
        return partidaActiva;
    }
}
