package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.vista.PanelJuego;
import javax.swing.Timer;

/**
 * Agrupa los temporizadores de la partida: el cronometro y el parpadeo de
 * invulnerabilidad.
 *
 * <p>Ambos se ejecutan en el hilo de Swing, por lo que sus campos solo se
 * consultan desde el EDT.</p>
 *
 * @see ControladorJuego
 */
public final class TemporizadoresJuego {

    private static final int INTERVALO_SEGUNDO_MS = 1000;
    private static final int INTERVALO_PARPADEO_MS = 140;

    private final PanelJuego panel;
    private final Timer timerSegundos;
    private final Timer timerParpadeo;
    private EstadoJuego estado;
    private volatile boolean pausado;
    private boolean parpadeo = true;

    /**
     * Crea los temporizadores asociados a un panel de juego.
     *
     * @param panel panel que se repinta durante la invulnerabilidad
     */
    public TemporizadoresJuego(PanelJuego panel) {
        this.panel = panel;
        timerSegundos = new Timer(INTERVALO_SEGUNDO_MS, e -> {
            EstadoJuego actual = estado;
            if (actual != null && !pausado) {
                actual.actualizarSegundo();
            }
        });
        timerParpadeo = new Timer(INTERVALO_PARPADEO_MS, e -> {
            parpadeo = !parpadeo;
            panel.setParpadeoVisible(parpadeo);
        });
    }

    /**
     * Asocia la partida que consume los temporizadores.
     *
     * @param estado partida que se va a cronometrar
     */
    public void setEstado(EstadoJuego estado) {
        this.estado = estado;
        parpadeo = true;
        panel.setParpadeoVisible(true);
    }

    /**
     * Inicia el cronometro y el parpadeo si la partida sigue activa.
     */
    public void iniciar() {
        pausado = false;
        EstadoJuego actual = estado;
        if (actual != null && !actual.getMarcador().isJuegoTerminado()) {
            timerSegundos.start();
            timerParpadeo.start();
        }
    }

    /**
     * Alterna entre pausa y marcha.
     */
    public void alternarPausa() {
        EstadoJuego actual = estado;
        if (actual == null || actual.getMarcador().isJuegoTerminado()) {
            return;
        }
        pausado = !pausado;
        if (pausado) {
            timerSegundos.stop();
            timerParpadeo.stop();
        } else {
            timerSegundos.start();
            timerParpadeo.start();
        }
    }

    /**
     * Detiene ambos temporizadores y sale del estado de pausa.
     */
    public void detener() {
        pausado = false;
        timerSegundos.stop();
        timerParpadeo.stop();
    }

    /**
     * Indica si la partida esta pausada.
     *
     * @return {@code true} si los temporizadores estan detenidos por pausa
     */
    public boolean isPausado() {
        return pausado;
    }
}
