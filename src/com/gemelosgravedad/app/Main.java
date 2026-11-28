package com.gemelosgravedad.app;

import com.gemelosgravedad.controlador.ControladorNavegacion;
import com.gemelosgravedad.recursos.GestorSonidos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Punto de entrada de la aplicacion.
 *
 * <p>Carga los sonidos, intenta usar el aspecto del sistema y arranca la
 * navegacion en el hilo de eventos de Swing.</p>
 *
 * @see ControladorNavegacion
 * @see GestorSonidos
 */
public final class Main {

    private static final Logger REGISTRO = Logger.getLogger(Main.class.getName());

    private Main  () {
    }

    /**
     * Arranca la aplicacion.
     *
     * @param args argumentos de linea de comandos; no se usan
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
            REGISTRO.log(Level.FINE,
                    "No se pudo usar el aspecto del sistema; se usa el de Swing", e);
        }

        GestorSonidos.cargarTodos();
        SwingUtilities.invokeLater(ControladorNavegacion::new);
    }
}
