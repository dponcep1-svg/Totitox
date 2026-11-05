package com.mycompany.totito;

/**
 * Guarda el estado del tablero de 3x3 y revisa las reglas básicas:
 * si una casilla está libre, si alguien hizo 3 en línea y si el tablero está lleno.
 * Esta clase no usa Swing: solo maneja datos.
 */
public class Tablero {

    public static final int TAMANO = 3;
    public static final char VACIA = ' ';

    private char[][] casillas;

    public Tablero() {
        casillas = new char[TAMANO][TAMANO];
        limpiar();
    }

    /** Deja todas las casillas vacías (para empezar una partida nueva). */
    public void limpiar() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                casillas[fila][columna] = VACIA;
            }
        }
    }

    /** Un movimiento es válido si está dentro del tablero y la casilla está vacía. */
    public boolean esMovimientoValido(int fila, int columna) {
        boolean dentroDelTablero = fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
        return dentroDelTablero && casillas[fila][columna] == VACIA;
    }

    /**
     * Coloca una ficha en la casilla indicada.
     * Devuelve true si se pudo colocar, o false si el movimiento no es válido.
     */
    public boolean colocarFicha(int fila, int columna, char simbolo) {
        if (!esMovimientoValido(fila, columna)) {
            return false;
        }
        casillas[fila][columna] = simbolo;
        return true;
    }

    /** Vacía una casilla. La IA lo usa para "deshacer" las jugadas que prueba. */
    public void quitarFicha(int fila, int columna) {
        casillas[fila][columna] = VACIA;
    }

    public char getCasilla(int fila, int columna) {
        return casillas[fila][columna];
    }

    /** Revisa si el símbolo tiene 3 fichas alineadas: en una fila, una columna o una diagonal. */
    public boolean hayGanador(char simbolo) {
        for (int i = 0; i < TAMANO; i++) {
            // Fila i completa
            if (casillas[i][0] == simbolo && casillas[i][1] == simbolo && casillas[i][2] == simbolo) {
                return true;
            }
            // Columna i completa
            if (casillas[0][i] == simbolo && casillas[1][i] == simbolo && casillas[2][i] == simbolo) {
                return true;
            }
        }
        // Diagonal principal (de arriba-izquierda a abajo-derecha)
        if (casillas[0][0] == simbolo && casillas[1][1] == simbolo && casillas[2][2] == simbolo) {
            return true;
        }
        // Diagonal secundaria (de arriba-derecha a abajo-izquierda)
        return casillas[0][2] == simbolo && casillas[1][1] == simbolo && casillas[2][0] == simbolo;
    }

    /** El tablero está lleno cuando ya no queda ninguna casilla vacía. */
    public boolean estaLleno() {
        for (int fila = 0; fila < TAMANO; fila++) {
            for (int columna = 0; columna < TAMANO; columna++) {
                if (casillas[fila][columna] == VACIA) {
                    return false;
                }
            }
        }
        return true;
    }
}
