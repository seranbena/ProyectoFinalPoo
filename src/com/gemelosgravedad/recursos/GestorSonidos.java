package com.gemelosgravedad.recursos;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Carga los archivos {@code .wav} al iniciar y los reproduce con
 * {@code javax.sound.sampled}.
 *
 * <p>Si un archivo falta o el sistema de audio no esta disponible, el sonido se
 * ignora y el juego continua. Las claves publicas se comparten con
 * {@link com.gemelosgravedad.modelo.Efecto} para no duplicar literales.</p>
 *
 * @see LocalizadorRecursos
 */
public final class GestorSonidos {

    /** Clave del sonido que se reproduce al saltar. */
    public static final String SALTO = "salto";

    /** Clave del sonido que se reproduce al recoger un cristal. */
    public static final String CRISTAL = "cristal";

    /** Clave del sonido que se reproduce al recibir un golpe. */
    public static final String GOLPE = "golpe";

    /** Clave del sonido que se reproduce al terminar la partida. */
    public static final String GAME_OVER = "gameover";

    /** Clave del sonido que se reproduce al recoger un power-up. */
    public static final String POWER_UP = "powerup";

    private static final Logger REGISTRO = Logger.getLogger(GestorSonidos.class.getName());
    private static final String CARPETA_SONIDOS = "sonidos/";
    private static final String EXTENSION = ".wav";
    private static final String MUSICA = "musica";
    private static final float VOLUMEN_MUSICA = 0.30f;
    private static final Map<String, Clip> SONIDOS = new HashMap<>();
    private static Clip musica;

    private GestorSonidos() {
    }

    /**
     * Carga todos los efectos y la musica de fondo.
     */
    public static void cargarTodos() {
        cargar(SALTO);
        cargar(CRISTAL);
        cargar(GOLPE);
        cargar(GAME_OVER);
        cargar(POWER_UP);
        musica = cargarClip(CARPETA_SONIDOS + MUSICA + EXTENSION);
        if (musica != null) {
            ajustarVolumen(musica, VOLUMEN_MUSICA);
        }
    }

    /**
     * Carga un efecto y lo registra con su clave.
     *
     * @param clave clave con la que se reproducira el sonido
     */
    private static void cargar(String clave) {
        Clip clip = cargarClip(CARPETA_SONIDOS + clave + EXTENSION);
        if (clip != null) {
            SONIDOS.put(clave, clip);
        }
    }

    /**
     * Abre un archivo de audio como clip reproducible.
     *
     * @param ruta ruta relativa dentro de {@code resources}
     * @return clip abierto o {@code null} si no se pudo cargar
     */
    private static Clip cargarClip(String ruta) {
        URL url = LocalizadorRecursos.localizar(ruta);
        if (url == null) {
            return null;
        }
        Clip clip = null;
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(url)) {
            clip = AudioSystem.getClip();
            clip.open(audio);
            return clip;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            if (clip != null) {
                clip.close();
            }
            REGISTRO.log(Level.FINE, "Sonido no disponible: " + ruta, e);
            return null;
        }
    }

    /**
     * Reproduce un efecto desde el principio.
     *
     * @param clave clave del sonido que se quiere reproducir
     */
    public static void reproducir(String clave) {
        Clip clip = SONIDOS.get(clave);
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        clip.start();
    }

    /**
     * Inicia la musica de fondo en bucle si aun no esta sonando.
     */
    public static void reproducirMusica() {
        if (musica == null || musica.isRunning()) {
            return;
        }
        musica.setFramePosition(0);
        musica.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Detiene la musica de fondo.
     */
    public static void detenerMusica() {
        if (musica != null && musica.isRunning()) {
            musica.stop();
        }
    }

    /**
     * Ajusta el volumen de un clip si el sistema lo permite.
     *
     * @param clip clip que se quiere ajustar
     * @param volumen volumen lineal entre cero y uno
     */
    private static void ajustarVolumen(Clip clip, float volumen) {
        try {
            if (clip.getControl(FloatControl.Type.MASTER_GAIN)
                    instanceof FloatControl ganancia) {
                float decibelios = (float) (20.0 * Math.log10(volumen));
                ganancia.setValue(decibelios);
            }
        } catch (IllegalArgumentException e) {
            // Algunos sistemas no permiten cambiar el volumen; se deja igual.
        }
    }
}
