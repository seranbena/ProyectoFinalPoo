package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.datos.Puntaje;
import com.gemelosgravedad.modelo.datos.RegistroPartida;
import com.gemelosgravedad.recursos.GestorSonidos;
import com.gemelosgravedad.vista.VentanaPrincipal;
import javax.swing.SwingUtilities;

/**
 * Controla el cambio de pantallas y conecta los botones con el juego.
 *
 * <p>La vista no decide que pantalla sigue: todas las transiciones pasan por
 * este controlador. Los eventos de Swing llegan ya en el hilo de interfaz, por
 * lo que solo el resultado final se publica con
 * {@link SwingUtilities#invokeLater}.</p>
 *
 * @see ControladorJuego
 * @see VentanaPrincipal
 */
public final class ControladorNavegacion {

    private final VentanaPrincipal ventana;
    private final ControladorPuntajes puntajes;
    private final ControladorJuego juego;
    private String nombreActual = "";
    private volatile boolean dosJugadores;

    /**
     * Crea la ventana principal y conecta todas las pantallas.
     */
    public ControladorNavegacion() {
        ventana = new VentanaPrincipal();
        puntajes = new ControladorPuntajes();
        juego = new ControladorJuego(ventana.getPanelJuego(), this);
        conectarEventos();
        ventana.setVisible(true);
        GestorSonidos.reproducirMusica();
    }

    /**
     * Conecta los botones de todas las pantallas.
     */
    private void conectarEventos() {
        conectarBienvenida();
        conectarModo();
        conectarInstrucciones();
        conectarNombre();
        conectarGameOver();
        conectarTop3();
    }

    /**
     * Conecta el menu de bienvenida.
     */
    private void conectarBienvenida() {
        ventana.getPantallaBienvenida().alJugar(e -> ventana.mostrarModo());
        ventana.getPantallaBienvenida().alInstrucciones(e -> ventana.mostrarInstrucciones());
        ventana.getPantallaBienvenida().alTop3(e -> abrirTop3());
        ventana.getPantallaBienvenida().alSalir(e -> {
            GestorSonidos.detenerMusica();
            ventana.dispose();
            System.exit(0);
        });
    }

    /**
     * Conecta la seleccion entre uno y dos jugadores.
     */
    private void conectarModo() {
        ventana.getPantallaModo().alUnJugador(e -> abrirNombre(false));
        ventana.getPantallaModo().alDosJugadores(e -> abrirNombre(true));
        ventana.getPantallaModo().alVolver(e -> ventana.mostrarBienvenida());
    }

    /**
     * Guarda el modo elegido y abre el formulario de nombres.
     *
     * @param dosJugadores {@code true} para el modo cooperativo
     */
    private void abrirNombre(boolean dosJugadores) {
        this.dosJugadores = dosJugadores;
        ventana.mostrarNombre(dosJugadores);
    }

    /**
     * Conecta la pantalla de instrucciones.
     */
    private void conectarInstrucciones() {
        ventana.getPantallaInstrucciones().alVolver(e -> ventana.mostrarBienvenida());
    }

    /**
     * Conecta la pantalla de nombre.
     */
    private void conectarNombre() {
        ventana.getPantallaNombre().alJugar(e -> intentarComenzar());
        ventana.getPantallaNombre().alVolver(e -> ventana.mostrarModo());
    }

    /**
     * Conecta la pantalla de fin de partida.
     */
    private void conectarGameOver() {
        ventana.getPantallaGameOver().alReiniciar(
                e -> empezarPartida(nombreActual, dosJugadores));
        ventana.getPantallaGameOver().alMenu(e -> ventana.mostrarBienvenida());
    }

    /**
     * Conecta la pantalla de records.
     */
    private void conectarTop3() {
        ventana.getPantallaTop3().alVolver(e -> ventana.mostrarBienvenida());
    }

    /**
     * Valida los nombres introducidos y comienza la partida.
     */
    private void intentarComenzar() {
        String nombre1 = ventana.getPantallaNombre().getNombre().trim();
        if (nombre1.isEmpty()) {
            ventana.getPantallaNombre().mostrarError(
                    "Escribe el nombre del jugador 1.");
            return;
        }
        if (!dosJugadores) {
            if (nombre1.length() > Puntaje.NOMBRE_MAXIMO) {
                ventana.getPantallaNombre().mostrarError(
                        "El nombre admite maximo " + Puntaje.NOMBRE_MAXIMO
                                + " caracteres.");
                return;
            }
            nombreActual = nombre1;
            empezarPartida(nombreActual, false);
            return;
        }

        String nombre2 = ventana.getPantallaNombre().getNombre2().trim();
        if (nombre2.isEmpty()) {
            ventana.getPantallaNombre().mostrarError(
                    "Escribe el nombre del jugador 2.");
            return;
        }
        if (nombre1.length() > Puntaje.NOMBRE_MAXIMO_2P
                || nombre2.length() > Puntaje.NOMBRE_MAXIMO_2P) {
            ventana.getPantallaNombre().mostrarError(
                    "Cada nombre admite maximo " + Puntaje.NOMBRE_MAXIMO_2P
                            + " caracteres.");
            return;
        }
        String equipo = nombre1 + " + " + nombre2;
        if (equipo.length() > Puntaje.NOMBRE_MAXIMO) {
            ventana.getPantallaNombre().mostrarError(
                    "Los dos nombres juntos admiten maximo "
                            + Puntaje.NOMBRE_MAXIMO + " caracteres.");
            return;
        }
        nombreActual = equipo;
        empezarPartida(nombreActual, true);
    }

    /**
     * Muestra el panel de juego y arranca una partida.
     *
     * @param nombre nombre individual o combinado del equipo
     * @param dosJugadores {@code true} para el modo cooperativo
     */
    private void empezarPartida(String nombre, boolean dosJugadores) {
        this.dosJugadores = dosJugadores;
        ventana.mostrarJuego();
        juego.iniciarPartida(nombre, dosJugadores);
    }

    /**
     * Carga el historial en segundo plano y muestra el Top 3 en el EDT.
     *
     * <p>La lectura del archivo es bloqueante, por lo que se ejecuta en un hilo
     * virtual y solo la actualizacion de Swing vuelve al hilo de eventos.</p>
     */
    private void abrirTop3() {
        Thread.ofVirtual().name("cargar-top3").start(() -> {
            RegistroPartida registro = puntajes.cargar();
            SwingUtilities.invokeLater(() -> {
                ventana.getPantallaTop3().mostrarTop(registro.obtenerTop3());
                ventana.mostrarTop3();
            });
        });
    }

    /**
     * Guarda el resultado y muestra la pantalla de fin de partida.
     *
     * <p>El archivo se escribe en un hilo virtual para no bloquear el EDT; la
     * pantalla se actualiza en el hilo de eventos de Swing.</p>
     *
     * @param nombre nombre individual o combinado del equipo
     * @param puntos puntaje final
     * @param segundos tiempo sobrevivido
     */
    public void mostrarGameOver(String nombre, int puntos, int segundos) {
        boolean equipo = dosJugadores;
        Puntaje resultado = new Puntaje(nombre, puntos, segundos,
                puntajes.obtenerFechaActual());
        Thread.ofVirtual().name("guardar-puntaje")
                .start(() -> puntajes.guardar(resultado));
        SwingUtilities.invokeLater(() -> {
            ventana.getPantallaGameOver().setDatos(resultado, equipo);
            ventana.mostrarGameOver();
        });
    }

    /**
     * Vuelve a la pantalla de bienvenida.
     */
    public void volverAlMenu() {
        ventana.mostrarBienvenida();
    }
}
