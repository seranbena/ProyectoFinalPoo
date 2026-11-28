/**
 * Modelo del juego: entidades, datos, reglas y estado de la partida.
 *
 * <p>El modelo no depende de Swing. La vista solo lee sus datos y el
 * controlador le envia las entradas del teclado.</p>
 *
 * <p>Organizacion del paquete:</p>
 * <ul>
 *   <li>{@code interfaces}: contratos de actualizacion y dibujo.</li>
 *   <li>{@code entidades}: jerarquia sellada de entidades (padres e hijas).</li>
 *   <li>{@code datos}: records y enums del dominio.</li>
 *   <li>{@code juego}: estado, reglas, nivel y marcador.</li>
 * </ul>
 *
 * @see com.gemelosgravedad.modelo.juego.EstadoJuego
 */
package com.gemelosgravedad.modelo;
