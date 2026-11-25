package com.gemelosgravedad.recursos;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Localiza archivos de {@code resources} en el classpath o en el sistema de
 * archivos.
 *
 * <p>Primero se prueba la ruta indicada, despues la misma ruta precedida por
 * {@code resources/}. Si el recurso no esta empaquetado, se busca como archivo
 * suelto. Asi el juego funciona tanto desde el IDE como desde una carpeta de
 * distribucion.</p>
 */
public final class LocalizadorRecursos {

    private static final String PREFIJO_CLASSPATH = "/";
    private static final String CARPETA_RECURSOS = "resources/";

    private LocalizadorRecursos() {
    }

    /**
     * Busca un recurso por su ruta relativa.
     *
     * @param ruta ruta relativa, por ejemplo {@code sprites/jugador_azul_1.png}
     * @return URL del recurso o {@code null} si no existe
     */
    public static URL localizar(String ruta) {
        URL url = LocalizadorRecursos.class.getResource(PREFIJO_CLASSPATH + ruta);
        if (url == null) {
            url = LocalizadorRecursos.class.getResource(
                    PREFIJO_CLASSPATH + CARPETA_RECURSOS + ruta);
        }
        if (url != null) {
            return url;
        }
        File archivo = new File(ruta);
        if (!archivo.exists()) {
            archivo = new File(CARPETA_RECURSOS + ruta);
        }
        if (!archivo.exists()) {
            return null;
        }
        try {
            return archivo.toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }
}
