package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.Cristal;
import com.gemelosgravedad.modelo.EnemigoCaminante;
import com.gemelosgravedad.modelo.EnemigoTecho;
import com.gemelosgravedad.modelo.EnemigoVolador;
import com.gemelosgravedad.modelo.EstadoJuego;
import com.gemelosgravedad.modelo.OrbeCambio;
import com.gemelosgravedad.modelo.PowerUp;
import com.gemelosgravedad.modelo.TipoPowerUp;
import java.util.Random;

/**
 * Hilo productor que crea enemigos, cristales, orbes y power-ups.
 *
 * <p>Los objetos se dejan en la lista sincronizada del {@link EstadoJuego}; el
 * hilo nunca modifica las listas que usa la vista. La frecuencia de aparicion
 * disminuye con el tiempo de partida para aumentar la dificultad.</p>
 *
 * @see EstadoJuego
 * @see TipoPowerUp
 */
public final class HiloGenerador implements Runnable {

    private static final int ESPERA_BASE_MS = 1450;
    private static final int REDUCCION_ESPERA_MS = 12;
    private static final int ESPERA_MINIMA_MS = 620;
    private static final int PROBABILIDAD_ENEMIGO = 55;
    private static final int PROBABILIDAD_CRISTAL = 83;
    private static final int PROBABILIDAD_POWER_UP = 91;
    private static final int MARGEN_ENTRADA = 40;
    private static final int MARGEN_ENTRADA_CRISTAL = 30;
    private static final int CRISTAL_DESDE_PISO = 62;
    private static final int CRISTAL_DESDE_TECHO = 6;
    private static final int POWER_UP_DESDE_PISO = 78;
    private static final int POWER_UP_DESDE_TECHO = 14;
    private static final int VOLADOR_DESDE_TECHO = 180;
    private static final int VOLADOR_RANGO_VERTICAL = 150;
    private static final int ORBE_Y_MINIMA = 250;
    private static final int ORBE_RANGO_VERTICAL = 170;
    private static final int CORTE_CAMINANTE = 38;
    private static final int CORTE_TECHO = 70;
    private static final double VARIACION_VELOCIDAD = 1.2;
    private static final double AJUSTE_VELOCIDAD_CRISTAL = 0.4;
    private static final double FACTOR_VELOCIDAD_POWER_UP = 0.8;
    private static final double FACTOR_VELOCIDAD_ORBE = 0.55;

    private final EstadoJuego estado;
    private final Random azar = new Random();
    private volatile boolean activo = true;
    private volatile boolean pausado;

    /**
     * Crea el generador de objetos de una partida.
     *
     * @param estado partida en la que se insertan los objetos
     */
    public HiloGenerador(EstadoJuego estado) {
        this.estado = estado;
    }

    /**
     * Genera objetos hasta que se detiene el hilo o la partida termina.
     */
    @Override
    public void run() {
        while (activo) {
            try {
                Thread.sleep(calcularEspera());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                activo = false;
                break;
            }
            if (!activo || pausado || estado.getMarcador().isJuegoTerminado()) {
                continue;
            }
            generar();
        }
    }

    /**
     * Calcula la espera entre dos objetos segun el tiempo de partida.
     *
     * @return milisegundos de espera
     */
    private int calcularEspera() {
        int espera = ESPERA_BASE_MS - estado.getMarcador().getSegundos() * REDUCCION_ESPERA_MS;
        return Math.max(espera, ESPERA_MINIMA_MS);
    }

    /**
     * Elige que tipo de objeto se genera en este ciclo.
     */
    private void generar() {
        int dado = azar.nextInt(100);
        if (dado < PROBABILIDAD_ENEMIGO) {
            generarEnemigo();
        } else if (dado < PROBABILIDAD_CRISTAL) {
            generarCristal();
        } else if (dado < PROBABILIDAD_POWER_UP) {
            generarPowerUp();
        } else {
            generarOrbe();
        }
    }

    /**
     * Genera un enemigo caminante, de techo o volador.
     */
    private void generarEnemigo() {
        int tipo = azar.nextInt(100);
        double velocidad = estado.getVelocidadBase() + azar.nextDouble() * VARIACION_VELOCIDAD;
        if (tipo < CORTE_CAMINANTE) {
            estado.agregarPendiente(new EnemigoCaminante(
                    EstadoJuego.ANCHO + MARGEN_ENTRADA,
                    EstadoJuego.PISO - 50, velocidad));
        } else if (tipo < CORTE_TECHO) {
            estado.agregarPendiente(new EnemigoTecho(
                    EstadoJuego.ANCHO + MARGEN_ENTRADA,
                    EstadoJuego.TECHO, velocidad));
        } else {
            int y = EstadoJuego.TECHO + VOLADOR_DESDE_TECHO
                    + azar.nextInt(VOLADOR_RANGO_VERTICAL);
            estado.agregarPendiente(new EnemigoVolador(
                    EstadoJuego.ANCHO + MARGEN_ENTRADA, y, velocidad + 1.0));
        }
    }

    /**
     * Genera un cristal en el carril del piso o del techo.
     */
    private void generarCristal() {
        int y = azar.nextBoolean()
                ? EstadoJuego.PISO - CRISTAL_DESDE_PISO
                : EstadoJuego.TECHO + CRISTAL_DESDE_TECHO;
        estado.agregarPendiente(new Cristal(
                EstadoJuego.ANCHO + MARGEN_ENTRADA_CRISTAL, y,
                estado.getVelocidadBase() - AJUSTE_VELOCIDAD_CRISTAL));
    }

    /**
     * Genera un power-up de escudo o de turbo.
     */
    private void generarPowerUp() {
        TipoPowerUp tipo = azar.nextBoolean() ? TipoPowerUp.ESCUDO : TipoPowerUp.TURBO;
        int y = azar.nextBoolean()
                ? EstadoJuego.PISO - POWER_UP_DESDE_PISO
                : EstadoJuego.TECHO + POWER_UP_DESDE_TECHO;
        estado.agregarPendiente(new PowerUp(
                EstadoJuego.ANCHO + MARGEN_ENTRADA, y, tipo,
                estado.getVelocidadBase() * FACTOR_VELOCIDAD_POWER_UP));
    }

    /**
     * Genera un orbe de cambio en la zona central del escenario.
     */
    private void generarOrbe() {
        int y = ORBE_Y_MINIMA + azar.nextInt(ORBE_RANGO_VERTICAL);
        estado.agregarPendiente(new OrbeCambio(
                EstadoJuego.ANCHO + MARGEN_ENTRADA, y,
                estado.getVelocidadBase() * FACTOR_VELOCIDAD_ORBE));
    }

    /**
     * Pausa o reanuda la generacion de objetos.
     *
     * @param pausado {@code true} para detener temporalmente la generacion
     */
    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    /**
     * Pide al hilo que termine.
     */
    public void detener() {
        activo = false;
    }
}
