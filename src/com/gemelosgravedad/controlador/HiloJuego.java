package com.gemelosgravedad.controlador;

import com.gemelosgravedad.vista.PanelJuego;

/**
 * Bucle principal de la partida a unos sesenta fotogramas por segundo.
 *
 * <p>El hilo solo pide pasos al controlador y repinta el panel; las reglas del
 * juego permanecen en el modelo.</p>
 *
 * @see ControladorJuego
 */
public final class HiloJuego implements Runnable {

    private static final int FOTOGRAMAS_POR_SEGUNDO = 60;
    private static final long MILISEGUNDOS_POR_FOTOGRAMA = 1000L / FOTOGRAMAS_POR_SEGUNDO;

    private final ControladorJuego controlador;
    private final PanelJuego panel;
    private volatile boolean ejecutando = true;

    /**
     * Crea el hilo del bucle de juego.
     *
     * @param controlador controlador que aplica las reglas
     * @param panel panel que se repinta en cada paso
     */
    public HiloJuego(ControladorJuego controlador, PanelJuego panel) {
        this.controlador = controlador;
        this.panel = panel;
    }

    /**
     * Ejecuta el bucle hasta que el controlador detiene la partida o el hilo
     * recibe una interrupcion.
     */
    @Override
    public void run() {
        while (ejecutando) {
            long inicio = System.currentTimeMillis();
            if (!controlador.isPausado() && controlador.isPartidaActiva()) {
                boolean continuar = controlador.pasoJuego();
                panel.repaint();
                if (!continuar) {
                    break;
                }
            }

            long gastado = System.currentTimeMillis() - inicio;
            long espera = MILISEGUNDOS_POR_FOTOGRAMA - gastado;
            if (espera > 0) {
                try {
                    Thread.sleep(espera);
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
