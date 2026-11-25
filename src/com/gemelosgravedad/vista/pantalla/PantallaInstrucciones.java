package com.gemelosgravedad.vista.pantalla;

import com.gemelosgravedad.modelo.TipoPowerUp;
import com.gemelosgravedad.vista.componente.BarraBrutal;
import com.gemelosgravedad.vista.componente.BotonEstilo;
import com.gemelosgravedad.vista.componente.EstiloBrutal;
import com.gemelosgravedad.vista.componente.PanelTarjeta;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Manual de operaciones: controles, objetivo, enemigos y power-ups.
 *
 * <p>Las dos columnas se construyen con las mismas piezas de estilo que el
 * resto de pantallas. Los datos variables de los power-ups se leen de
 * {@link TipoPowerUp} para no repetir duraciones en la interfaz.</p>
 */
public final class PantallaInstrucciones extends PantallaBase {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final int LADO_ICONO = 12;
    private static final int TAMANO_ICONO = 8;
    private static final int ANCHO_BOTON = 240;
    private static final int ALTO_BOTON = 50;

    /** Boton que vuelve al menu principal. */
    private final BotonEstilo botonVolver;

    /**
     * Crea el manual de operaciones y su boton de vuelta.
     */
    public PantallaInstrucciones() {
        super();
        botonVolver = new BotonEstilo("VOLVER", EstiloBrutal.AZUL,
                ANCHO_BOTON, ALTO_BOTON);
        montar("MANUAL DE OPERACIÓN", "CÓMO JUGAR", "GD—02 / 07 SECCIONES",
                crearCentro(), "MANUAL GD—02 // REVISIÓN A",
                "P PAUSA // ESC MENÚ");
    }

    /**
     * Crea el contenido central con las columnas y las acciones.
     *
     * @return contenedor central
     */
    private JPanel crearCentro() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setOpaque(false);
        principal.setBorder(BorderFactory.createEmptyBorder(20, 42, 8, 42));
        principal.add(crearColumnas(), BorderLayout.CENTER);
        principal.add(BarraBrutal.acciones(
                "TODAS LAS SECCIONES CABEN EN UNA PARTIDA.", botonVolver),
                BorderLayout.SOUTH);
        return principal;
    }

    /**
     * Crea las dos columnas del manual.
     *
     * @return panel con las columnas
     */
    private JPanel crearColumnas() {
        JPanel columnas = new JPanel(new GridLayout(1, 2, 26, 0));
        columnas.setOpaque(false);

        PanelTarjeta izquierda = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.ROJO);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        seccion(izquierda, "01 // CONTROLES", EstiloBrutal.AMARILLO);
        linea(izquierda, "← / →    MOVER A AMBOS GEMELOS", EstiloBrutal.NEGRO, false);
        linea(izquierda, "ESPACIO   SALTO EN ESPEJO / RESCATE", EstiloBrutal.NEGRO, false);
        linea(izquierda, "P   PAUSA        ESC   MENÚ", EstiloBrutal.NEGRO, false);

        seccion(izquierda, "02 // OBJETIVO", EstiloBrutal.AMARILLO);
        linea(izquierda, "SOBREVIVE EL MAYOR TIEMPO POSIBLE.", EstiloBrutal.NEGRO, false);
        linea(izquierda, "EL TIEMPO SUMA 2 PUNTOS POR SEGUNDO.", EstiloBrutal.NEGRO, false);
        linea(izquierda, "CADA 10 SEGUNDOS SUBE LA DIFICULTAD.", EstiloBrutal.NEGRO, false);

        seccion(izquierda, "03 // CRISTALES", EstiloBrutal.AMARILLO);
        linea(izquierda, "CADA CRISTAL VALE 25 PUNTOS.", EstiloBrutal.NEGRO, false);
        linea(izquierda, "APARECEN EN EL PISO O EN EL TECHO.", EstiloBrutal.NEGRO, false);
        linea(izquierda, "LLÉVALO AL GEMELO CORRECTO.", EstiloBrutal.NEGRO, false);

        seccion(izquierda, "04 // HUECOS, PLATAFORMAS Y SOGA",
                EstiloBrutal.AMARILLO);
        linea(izquierda, "PISO Y TECHO ROTOS: SALTA ENTRE PLATAFORMAS.",
                EstiloBrutal.NEGRO, false);
        linea(izquierda, "SI CAE UNO, LA SOGA LO DEJA COLGANDO.", EstiloBrutal.ROJO, true);
        linea(izquierda, "ESPACIO LO RESCATA. SOLO CAES SI CAEN LOS DOS.",
                EstiloBrutal.ROJO, true);
        izquierda.add(Box.createVerticalGlue());
        columnas.add(izquierda);

        PanelTarjeta derecha = new PanelTarjeta(EstiloBrutal.BLANCO,
                EstiloBrutal.AZUL);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        derecha.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));

        seccion(derecha, "05 // ENEMIGOS", EstiloBrutal.AMARILLO);
        filaEnemigo(derecha, EstiloBrutal.ROJO, "CAMINANTE", "AVANZA POR EL PISO.");
        filaEnemigo(derecha, EstiloBrutal.MORADO, "MURCIÉLAGO", "AVANZA POR EL TECHO.");
        filaEnemigo(derecha, EstiloBrutal.VERDE, "DRON", "VUELA POR EL CENTRO.");
        linea(derecha, "SI TE TOCAN, PIERDES UNA VIDA.", EstiloBrutal.ROJO, true);

        seccion(derecha, "06 // POWER-UPS", EstiloBrutal.AMARILLO);
        filaEnemigo(derecha, TipoPowerUp.ESCUDO.getColor(), "ESCUDO [E]",
                TipoPowerUp.ESCUDO.getDuracionSegundos() + " S. RESISTE UN GOLPE.");
        filaEnemigo(derecha, TipoPowerUp.TURBO.getColor(), "TURBO [T]",
                TipoPowerUp.TURBO.getDuracionSegundos() + " S. DESTRUYE ENEMIGOS.");
        filaEnemigo(derecha, EstiloBrutal.MORADO, "ORBE", "INTERCAMBIA LOS GEMELOS.");
        linea(derecha, "EL TURBO TAMBIÉN TE HACE MÁS GRANDE.",
                EstiloBrutal.NEGRO, false);

        seccion(derecha, "07 // VIDAS Y EXTRA", EstiloBrutal.AMARILLO);
        linea(derecha, "EMPIEZAS CON 3 VIDAS.", EstiloBrutal.NEGRO, false);
        linea(derecha, "TRAS UN GOLPE: 2 S INVULNERABLE.", EstiloBrutal.NEGRO, false);
        linea(derecha, "EL PARPADEO TE DELATA.", EstiloBrutal.NEGRO, false);
        linea(derecha, "ENEMIGO DESTRUIDO CON TURBO: +50.", EstiloBrutal.NEGRO, false);
        derecha.add(Box.createVerticalGlue());
        columnas.add(derecha);
        return columnas;
    }

    /**
     * Agrega una cabecera de seccion a una tarjeta.
     *
     * @param tarjeta tarjeta que recibe la seccion
     * @param titulo texto de la seccion
     * @param acento color del texto
     */
    private static void seccion(JPanel tarjeta, String titulo, Color acento) {
        if (tarjeta.getComponentCount() > 0) {
            tarjeta.add(Box.createVerticalStrut(9));
        }
        JLabel cabecera = new JLabel(titulo);
        cabecera.setOpaque(true);
        cabecera.setBackground(EstiloBrutal.NEGRO);
        cabecera.setForeground(acento);
        cabecera.setFont(EstiloBrutal.titulo(12));
        cabecera.setBorder(BorderFactory.createEmptyBorder(4, 9, 4, 9));
        cabecera.setAlignmentX(Component.LEFT_ALIGNMENT);
        cabecera.setMaximumSize(new Dimension(Integer.MAX_VALUE, 26));
        tarjeta.add(cabecera);
        tarjeta.add(Box.createVerticalStrut(5));
    }

    /**
     * Agrega una linea de texto a una tarjeta.
     *
     * @param tarjeta tarjeta que recibe la linea
     * @param texto contenido
     * @param color color del texto
     * @param fuerte {@code true} para usar negrita monoespaciada
     */
    private static void linea(JPanel tarjeta, String texto, Color color,
                              boolean fuerte) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(fuerte ? EstiloBrutal.mono(12)
                : EstiloBrutal.monoNormal(12));
        etiqueta.setForeground(color);
        etiqueta.setBorder(BorderFactory.createEmptyBorder(1, 8, 1, 0));
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        etiqueta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 21));
        tarjeta.add(etiqueta);
    }

    /**
     * Agrega una fila con icono de color a una tarjeta.
     *
     * @param tarjeta tarjeta que recibe la fila
     * @param color color del icono
     * @param nombre nombre del elemento
     * @param detalle descripcion breve
     */
    private static void filaEnemigo(JPanel tarjeta, Color color, String nombre,
                                    String detalle) {
        JLabel fila = new JLabel(nombre + "  /  " + detalle);
        fila.setIcon(crearIcono(color));
        fila.setIconTextGap(8);
        fila.setFont(EstiloBrutal.monoNormal(12));
        fila.setForeground(EstiloBrutal.NEGRO);
        fila.setAlignmentX(Component.LEFT_ALIGNMENT);
        fila.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        tarjeta.add(fila);
    }

    /**
     * Crea un cuadrado de color con marco negro.
     *
     * @param color color del cuadrado
     * @return icono generado
     */
    private static ImageIcon crearIcono(Color color) {
        BufferedImage imagen = new BufferedImage(LADO_ICONO, LADO_ICONO,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        try {
            g.setColor(EstiloBrutal.NEGRO);
            g.fillRect(0, 0, LADO_ICONO, LADO_ICONO);
            g.setColor(color);
            g.fillRect(2, 2, TAMANO_ICONO, TAMANO_ICONO);
        } finally {
            g.dispose();
        }
        return new ImageIcon(imagen);
    }

    /**
     * Registra el oyente del boton Volver.
     *
     * @param oyente accion que se ejecuta al pulsar
     */
    public void alVolver(ActionListener oyente) {
        botonVolver.addActionListener(oyente);
    }
}
