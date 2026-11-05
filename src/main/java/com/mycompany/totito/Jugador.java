package com.mycompany.totito;

/**
 * Representa a un participante del juego.
 * Cada jugador tiene un nombre y un símbolo: X (jugador) u O (computadora).
 */
public class Jugador {

    private String nombre;
    private char simbolo;

    public Jugador(String nombre, char simbolo) {
        this.nombre = nombre;
        this.simbolo = simbolo;
    }

    public String getNombre() {
        return nombre;
    }

    public char getSimbolo() {
        return simbolo;
    }
}
