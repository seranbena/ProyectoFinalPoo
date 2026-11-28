package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.datos.Efecto;
import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.modelo.entidades.Jugador;
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
    private final EntradaJugador entradaP1 = new EntradaJugador();
    private final EntradaJugador entradaP2 = new EntradaJugador();

    private EstadoJuego estado;
    private HiloJuego hiloJuego;
    private HiloGenerador hiloGenerador;
    private Thread hebraJuego;
    private Thread hebraGenerador;

    private volatile String nombreJugador = "";
    private volatile boolean dosJugadores;
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
     * Prepara una partida nueva para un solo jugador.
     *
     * <p>Se conserva como sobrecarga de compatibilidad; el modo cooperativo usa
     * la version de dos argumentos.</p>
     *
     * @param nombre nombre del jugador que se mostrara en el HUD
     */
    public void iniciarPartida(String nombre) {
        iniciarPartida(nombre, false);
    }

    /**
     * Prepara una partida nueva y arranca sus hilos y temporizadores.
     *
     * @param nombre nombre individual o combinado del equipo
     * @param dosJugadores {@code true} para el modo cooperativo
     */
    public void iniciarPartida(String nombre, boolean dosJugadores) {
        detener();
        nombreJugador = nombre;
        this.dosJugadores = dosJugadores;
        estado = new EstadoJuego(nombre, dosJugadores);
        panel.setEstado(estado);
        panel.setPausado(false);
        entradaP1.reiniciar();
        entradaP2.reiniciar();
        partidaActiva = true;
        temporizadores.setEstado(estado);

        hiloJuego = new HiloJuego(this, panel, estado);
        hiloGenerador = new HiloGenerador(estado);
        hebraJuego = Thread.ofPlatform().name("HiloJuego").start(hiloJuego);
        hebraGenerador = Thread.ofPlatform().name("HiloGenerador").start(hiloGenerador);
        temporizadores.iniciar();
        panel.repaint();
        panel.pedirFoco();
    }

    /**
     * Ejecuta un paso del bucle: lee el teclado, actualiza el modelo y revisa
     * si la partida termino.
     *
     * @param esperado partida que el hilo cree estar ejecutando; si ya no es la
     *                 partida actual, el hilo antiguo debe terminar
     * @return {@code true} mientras la partida deba continuar
     */
    public boolean pasoJuego(EstadoJuego esperado) {
        EstadoJuego actual = estado;
        if (actual == null || actual != esperado || !partidaActiva) {
            return false;
        }
        if (dosJugadores) {
            actual.mover(actual.getJugadorAzul(), entradaP1.getDireccion());
            actual.mover(actual.getJugadorAmarillo(), entradaP2.getDireccion());
        } else {
            actual.mover(entradaP1.getDireccion());
        }
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
        actual.consumirEfectos().stream()
                .filter(efecto -> efecto != Efecto.GAME_OVER)
                .forEach(efecto -> GestorSonidos.reproducir(efecto.getClaveSonido()));
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
        if (pausado) {
            entradaP1.reiniciar();
            entradaP2.reiniciar();
        }
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
        entradaP1.reiniciar();
        entradaP2.reiniciar();
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
     * <p>En un jugador se usan flechas o A/D y Espacio. En dos jugadores, P1
     * usa A/D/W y P2 usa las flechas y la flecha arriba. Un mando configurado
     * con Steam o DS4Windows solo tiene que enviar estas teclas.</p>
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
        if (dosJugadores) {
            teclasDosJugadores(tecla);
        } else {
            teclasUnJugador(tecla);
        }
    }

    /**
     * Traduce las teclas del modo de un jugador.
     *
     * @param tecla codigo de la tecla pulsada
     */
    private void teclasUnJugador(int tecla) {
        switch (tecla) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> entradaP1.presionarIzquierda();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> entradaP1.presionarDerecha();
            case KeyEvent.VK_SPACE -> saltar();
            default -> {
                // Tecla sin accion en este modo.
            }
        }
    }

    /**
     * Ejecuta el salto conjunto del modo de un jugador.
     */
    private void saltar() {
        if (!entradaP1.intentarSalto()) {
            return;
        }
        EstadoJuego actual = estado;
        if (actual != null && actual.saltar()) {
            GestorSonidos.reproducir(GestorSonidos.SALTO);
        }
    }

    /**
     * Traduce las teclas del modo cooperativo.
     *
     * @param tecla codigo de la tecla pulsada
     */
    private void teclasDosJugadores(int tecla) {
        switch (tecla) {
            case KeyEvent.VK_A -> entradaP1.presionarIzquierda();
            case KeyEvent.VK_D -> entradaP1.presionarDerecha();
            case KeyEvent.VK_W -> {
                if (entradaP1.intentarSalto() && estado != null) {
                    saltarCon(estado.getJugadorAzul());
                }
            }
            case KeyEvent.VK_LEFT -> entradaP2.presionarIzquierda();
            case KeyEvent.VK_RIGHT -> entradaP2.presionarDerecha();
            case KeyEvent.VK_UP -> {
                if (entradaP2.intentarSalto() && estado != null) {
                    saltarCon(estado.getJugadorAmarillo());
                }
            }
            default -> {
                // Tecla sin accion en este modo.
            }
        }
    }

    /**
     * Ejecuta el salto de un gemelo concreto.
     *
     * @param jugador gemelo que debe saltar o rescatar
     */
    private void saltarCon(Jugador jugador) {
        EstadoJuego actual = estado;
        if (actual != null && actual.saltar(jugador)) {
            GestorSonidos.reproducir(GestorSonidos.SALTO);
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
        if (dosJugadores) {
            switch (tecla) {
                case KeyEvent.VK_A -> entradaP1.soltarIzquierda();
                case KeyEvent.VK_D -> entradaP1.soltarDerecha();
                case KeyEvent.VK_W -> entradaP1.soltarSalto();
                case KeyEvent.VK_LEFT -> entradaP2.soltarIzquierda();
                case KeyEvent.VK_RIGHT -> entradaP2.soltarDerecha();
                case KeyEvent.VK_UP -> entradaP2.soltarSalto();
                default -> {
                    // Tecla sin accion en este modo.
                }
            }
            return;
        }
        switch (tecla) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> entradaP1.soltarIzquierda();
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> entradaP1.soltarDerecha();
            case KeyEvent.VK_SPACE -> entradaP1.soltarSalto();
            default -> {
                // Tecla sin accion en este modo.
            }
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
