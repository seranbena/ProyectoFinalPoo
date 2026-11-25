package com.gemelosgravedad.controlador;

import com.gemelosgravedad.modelo.Puntaje;
import com.gemelosgravedad.modelo.RegistroPartida;
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
        conectarInstrucciones();
        conectarNombre();
        conectarGameOver();
        conectarTop3();
    }

    /**
     * Conecta el menu de bienvenida.
     */
    private void conectarBienvenida() {
        ventana.getPantallaBienvenida().alJugar(e -> ventana.mostrarNombre());
        ventana.getPantallaBienvenida().alInstrucciones(e -> ventana.mostrarInstrucciones());
        ventana.getPantallaBienvenida().alTop3(e -> abrirTop3());
        ventana.getPantallaBienvenida().alSalir(e -> {
            GestorSonidos.detenerMusica();
            System.exit(0);
        });
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
        ventana.getPantallaNombre().alVolver(e -> ventana.mostrarBienvenida());
    }

    /**
     * Conecta la pantalla de fin de partida.
     */
    private void conectarGameOver() {
        ventana.getPantallaGameOver().alReiniciar(e -> empezarPartida(nombreActual));
        ventana.getPantallaGameOver().alMenu(e -> ventana.mostrarBienvenida());
    }

    /**
     * Conecta la pantalla de records.
     */
    private void conectarTop3() {
        ventana.getPantallaTop3().alVolver(e -> ventana.mostrarBienvenida());
    }

    /**
     * Valida el nombre introducido y comienza la partida.
     */
    private void intentarComenzar() {
        String nombre = ventana.getPantallaNombre().getNombre().trim();
        if (nombre.isEmpty()) {
            ventana.getPantallaNombre().mostrarError("Escribe un nombre para jugar.");
            return;
        }
        if (nombre.length() > Puntaje.NOMBRE_MAXIMO) {
            ventana.getPantallaNombre().mostrarError(
                    "El nombre admite maximo " + Puntaje.NOMBRE_MAXIMO + " caracteres.");
            return;
        }
        nombreActual = nombre;
        empezarPartida(nombre);
    }

    /**
     * Muestra el panel de juego y arranca una partida.
     *
     * @param nombre nombre del jugador
     */
    private void empezarPartida(String nombre) {
        ventana.mostrarJuego();
        juego.iniciarPartida(nombre);
    }

    /**
     * Carga el historial y muestra el Top 3.
     */
    private void abrirTop3() {
        RegistroPartida registro = puntajes.cargar();
        ventana.getPantallaTop3().mostrarTop(registro.obtenerTop3());
        ventana.mostrarTop3();
    }

    /**
     * Guarda el resultado y muestra la pantalla de fin de partida.
     *
     * @param nombre nombre del jugador
     * @param puntos puntaje final
     * @param segundos tiempo sobrevivido
     */
    public void mostrarGameOver(String nombre, int puntos, int segundos) {
        SwingUtilities.invokeLater(() -> {
            Puntaje resultado = new Puntaje(nombre, puntos, segundos,
                    puntajes.obtenerFechaActual());
            puntajes.guardar(resultado);
            ventana.getPantallaGameOver().setDatos(resultado);
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
