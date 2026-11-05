package com.mycompany.totito;

import javax.swing.SwingUtilities;

/**
 * Punto de entrada del programa: abre la ventana del Totito.
 */
public class JuegoMain {

    public static void main(String[] args) {
        // Swing recomienda crear la ventana dentro de su propio hilo (invokeLater)
        SwingUtilities.invokeLater(() -> new VentanaTotito().setVisible(true));
    }
}
