package com.gemelosgravedad.util;

import java.util.Locale;

/**
 * Utilidades de formato de tiempo compartidas por el modelo y la vista.
 *
 * <p>Se centraliza aqui el formato {@code mm:ss} para que el cronometro del
 * HUD, el resultado final y el historial de partidas muestren siempre el mismo
 * texto.</p>
 */
public final class FormatoTiempo {

    private static final int SEGUNDOS_POR_MINUTO = 60;

    private FormatoTiempo() {
    }

    /**
     * Convierte una cantidad de segundos al formato {@code mm:ss}.
     *
     * @param segundos segundos transcurridos; los valores negativos se tratan
     *                 como cero
     * @return tiempo formateado con dos digitos para minutos y segundos
     */
    public static String formatear(int segundos) {
        int segundosSeguros = Math.max(0, segundos);
        return String.format(Locale.ROOT, "%02d:%02d",
                segundosSeguros / SEGUNDOS_POR_MINUTO,
                segundosSeguros % SEGUNDOS_POR_MINUTO);
    }
}
