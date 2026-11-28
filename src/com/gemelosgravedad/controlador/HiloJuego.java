package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.juego.EstadoJuego;
import com.gemelosgravedad.vista.PanelJuego;

/**
 * Bucle principal de la partida a unos sesenta fotogramas por segundo.
 *
 * <p>El hilo solo pide pasos al controlador y repinta el panel; las reglas del
 * juego permanecen en el modelo. La partida se guarda para que un hilo antiguo
 * no pueda ejecutar pasos sobre una partida nueva.</p>
 *
 * @see ControladorJuego
 */
public final class HiloJuego implements Runnable {

    private static final int FOTOGRAMAS_POR_SEGUNDO = 60;
    private static final long NANOS_POR_FOTOGRAMA = 1_000_000_000L / FOTOGRAMAS_POR_SEGUNDO;

    private final ControladorJuego controlador;
    private final PanelJuego panel;
    private final EstadoJuego estado;
    private volatile boolean ejecutando = true;

    /**
     * Crea el hilo del bucle de juego.
     *
     * @param controlador controlador que aplica las reglas
     * @param panel panel que se repinta en cada paso
     * @param estado partida que este hilo tiene permitido avanzar
     */
    public HiloJuego(ControladorJuego controlador, PanelJuego panel, EstadoJuego estado) {
        this.controlador = controlador;
        this.panel = panel;
        this.estado = estado;
    }

    /**
     * Ejecuta el bucle hasta que el controlador detiene la partida o el hilo
     * recibe una interrupcion.
     */
    @Override
    public void run() {
        while (ejecutando) {
            long inicio = System.nanoTime();
            if (!controlador.isPausado() && controlador.isPartidaActiva()) {
                boolean continuar = controlador.pasoJuego(estado);
                panel.repaint();
                if (!continuar) {
                    break;
                }
            }

            long espera = NANOS_POR_FOTOGRAMA - (System.nanoTime() - inicio);
            if (espera > 0) {
                try {
                    Thread.sleep(espera / 1_000_000, (int) (espera % 1_000_000));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    ejecutando = false;
                }
            }
        }
    }

    /**
     * Pide al bucle que termine en la siguiente iteracion.
     */
    public void detener() {
        ejecutando = false;
    }
}
