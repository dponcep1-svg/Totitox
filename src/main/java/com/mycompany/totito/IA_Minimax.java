package com.mycompany.totito;

/**
 * Inteligencia artificial de la computadora usando el algoritmo Minimax.
 *
 * Idea general:
 * La IA prueba TODAS las jugadas que puede hacer. Para cada una, imagina cómo
 * seguiría la partida hasta el final, alternando turnos y suponiendo que el
 * jugador humano siempre elige su mejor respuesta. Al final escoge la jugada
 * que le deja el mejor resultado posible. Como el totito es pequeño, la IA
 * puede revisar el árbol completo de jugadas, por eso nunca pierde.
 *
 * Puntaje de una partida terminada (visto desde la IA):
 *   - Gana la IA      ->  10 - profundidad  (ganar en menos jugadas vale más)
 *   - Gana el humano  -> -10 + profundidad  (perder lo más tarde posible es "menos malo")
 *   - Empate          ->   0
 */
public class IA_Minimax {

    private char simboloIA;
    private char simboloHumano;

    public IA_Minimax(char simboloIA, char simboloHumano) {
        this.simboloIA = simboloIA;
        this.simboloHumano = simboloHumano;
    }

    /**
     * Calcula la mejor jugada para la IA en el tablero actual.
     * Devuelve un arreglo {fila, columna}.
     */
    public int[] calcularMejorMovimiento(Tablero tablero) {
        int mejorPuntaje = Integer.MIN_VALUE;
        int[] mejorMovimiento = {-1, -1};

        for (int fila = 0; fila < Tablero.TAMANO; fila++) {
            for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                if (tablero.esMovimientoValido(fila, columna)) {
                    // 1. Probar la jugada en el tablero
                    tablero.colocarFicha(fila, columna, simboloIA);

                    // 2. Evaluarla con Minimax. Después de la jugada de la IA
                    //    le toca al humano, por eso turnoDeLaIA = false.
                    int puntaje = minimax(tablero, 1, false);

                    // 3. Deshacer la jugada para dejar el tablero como estaba
                    tablero.quitarFicha(fila, columna);

                    // 4. Guardar la jugada si es la mejor encontrada hasta ahora
                    if (puntaje > mejorPuntaje) {
                        mejorPuntaje = puntaje;
                        mejorMovimiento[0] = fila;
                        mejorMovimiento[1] = columna;
                    }
                }
            }
        }
        return mejorMovimiento;
    }

    /**
     * Función recursiva del algoritmo Minimax.
     *
     * Hay dos tipos de turno:
     *   - MAX (turno de la IA): busca el puntaje más alto.
     *   - MIN (turno del humano): suponemos que el humano juega perfecto,
     *     así que busca el puntaje más bajo (el peor para la IA).
     *
     * Cada llamada prueba todas las casillas libres. Por cada una coloca la
     * ficha, se llama a sí misma con el turno contrario (esa llamada repite
     * el proceso un nivel más abajo en el árbol) y después quita la ficha.
     * La recursión se detiene en el caso base, cuando alguien gana o el tablero
     * se llena. Entonces los puntajes "suben" por el árbol hasta la primera llamada.
     *
     * @param tablero      estado actual, con las jugadas de prueba ya colocadas
     * @param profundidad  cuántas jugadas se han simulado desde el tablero real
     * @param turnoDeLaIA  true si en este estado le toca a la IA (MAX), false si al humano (MIN)
     * @return el puntaje de este estado si los dos jugadores juegan perfecto
     */
    private int minimax(Tablero tablero, int profundidad, boolean turnoDeLaIA) {
        // CASO BASE: la partida ya terminó en este estado, se devuelve su puntaje
        if (tablero.hayGanador(simboloIA)) {
            return 10 - profundidad;
        }
        if (tablero.hayGanador(simboloHumano)) {
            return profundidad - 10;
        }
        if (tablero.estaLleno()) {
            return 0;
        }

        // CASO RECURSIVO
        if (turnoDeLaIA) {
            // MAX: la IA se queda con el puntaje más alto de sus jugadas posibles
            int mejorPuntaje = Integer.MIN_VALUE;
            for (int fila = 0; fila < Tablero.TAMANO; fila++) {
                for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                    if (tablero.esMovimientoValido(fila, columna)) {
                        tablero.colocarFicha(fila, columna, simboloIA);
                        int puntaje = minimax(tablero, profundidad + 1, false); // ahora le toca al humano
                        tablero.quitarFicha(fila, columna);
                        mejorPuntaje = Math.max(mejorPuntaje, puntaje);
                    }
                }
            }
            return mejorPuntaje;
        } else {
            // MIN: el humano se queda con el puntaje más bajo (lo peor para la IA)
            int peorPuntaje = Integer.MAX_VALUE;
            for (int fila = 0; fila < Tablero.TAMANO; fila++) {
                for (int columna = 0; columna < Tablero.TAMANO; columna++) {
                    if (tablero.esMovimientoValido(fila, columna)) {
                        tablero.colocarFicha(fila, columna, simboloHumano);
                        int puntaje = minimax(tablero, profundidad + 1, true); // ahora le toca a la IA
                        tablero.quitarFicha(fila, columna);
                        peorPuntaje = Math.min(peorPuntaje, puntaje);
                    }
                }
            }
            return peorPuntaje;
        }
    }
}
