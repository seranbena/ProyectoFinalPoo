package com.gemelosgravedad.modelo.interfaces;

/**
 * Contrato de los elementos que avanzan un paso de simulacion.
 *
 * <p>Separa la obligacion de actualizarse de la clase base {@code Entidad},
 * de modo que el bucle del juego pueda tratar cualquier elemento actualizable
 * sin conocer su jerarquia concreta.</p>
 */
public interface Actualizable {

    /**
     * Avanza el estado del elemento en un paso de simulacion.
     */
    void actualizar();
}
