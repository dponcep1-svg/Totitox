package com.mycompany.totito;

/**
 * Controla una partida: de quién es el turno, los movimientos y el resultado.
 * Une al Tablero, a los dos Jugadores y a la IA.
 * No usa Swing, así la lógica del juego queda separada de la ventana.
 */
public class Juego {

    private Tablero tablero;
    private Jugador humano;
    private Jugador computadora;
    private IA_Minimax ia;

    private Jugador turnoActual;
    private boolean terminado;
    private Jugador ganador; // queda en null si hay empate o si la partida sigue

    public Juego() {
        tablero = new Tablero();
        humano = new Jugador("Jugador", 'X');
        computadora = new Jugador("Computadora", 'O');
        ia = new IA_Minimax(computadora.getSimbolo(), humano.getSimbolo());
        reiniciar();
    }

    /** Empieza una partida nueva. Siempre comienza el jugador (X). */
    public void reiniciar() {
        tablero.limpiar();
        turnoActual = humano;
        terminado = false;
        ganador = null;
    }

    /**
     * Intenta hacer la jugada del humano.
     * Devuelve false (y no hace nada) si la partida terminó, si no es su turno
     * o si la casilla no es válida.
     */
    public boolean jugarHumano(int fila, int columna) {
        if (terminado || turnoActual != humano) {
            return false;
        }
        if (!tablero.colocarFicha(fila, columna, humano.getSimbolo())) {
            return false;
        }
        terminarTurno(humano);
        return true;
    }

    /**
     * La IA elige su jugada con Minimax y la coloca.
     * Devuelve la casilla elegida {fila, columna}, o null si no es su turno.
     */
    public int[] jugarComputadora() {
        if (terminado || turnoActual != computadora) {
            return null;
        }
        int[] movimiento = ia.calcularMejorMovimiento(tablero);
        tablero.colocarFicha(movimiento[0], movimiento[1], computadora.getSimbolo());
        terminarTurno(computadora);
        return movimiento;
    }

    /** Revisa si la partida terminó. Si no, le pasa el turno al otro jugador. */
    private void terminarTurno(Jugador quienJugo) {
        if (tablero.hayGanador(quienJugo.getSimbolo())) {
            terminado = true;
            ganador = quienJugo;
        } else if (tablero.estaLleno()) {
            terminado = true; // empate: ganador se queda en null
        } else if (quienJugo == humano) {
            turnoActual = computadora;
        } else {
            turnoActual = humano;
        }
    }

    public boolean isTerminado() {
        return terminado;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public Jugador getTurnoActual() {
        return turnoActual;
    }

    public Jugador getHumano() {
        return humano;
    }

    public Jugador getComputadora() {
        return computadora;
    }
}
