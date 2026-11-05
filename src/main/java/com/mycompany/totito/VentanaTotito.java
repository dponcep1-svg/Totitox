package com.mycompany.totito;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;

/**
 * Ventana del juego (interfaz gráfica con Swing).
 * Solo muestra el tablero y recibe los clics del jugador;
 * las reglas del juego las decide la clase Juego.
 */
public class VentanaTotito extends JFrame {

    private static final Color COLOR_X = new Color(190, 215, 255); // azul claro
    private static final Color COLOR_O = new Color(255, 200, 200); // rojo claro

    private Juego juego;
    private JButton[][] casillas;
    private JLabel etiquetaEstado;

    public VentanaTotito() {
        super("Totito - Jugador vs IA");
        juego = new Juego();

        // Por defecto Swing pinta en gris claro el texto de un botón deshabilitado
        // y las X / O casi no se ven. Esto lo cambia a gris oscuro.
        UIManager.put("Button.disabledText", Color.DARK_GRAY);

        // Parte de arriba: estado del juego (turno, ganador o empate)
        etiquetaEstado = new JLabel("", SwingConstants.CENTER);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaEstado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Centro: tablero de 3x3 casillas
        JPanel panelTablero = new JPanel(new GridLayout(Tablero.TAMANO, Tablero.TAMANO, 5, 5));
        panelTablero.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        casillas = new JButton[Tablero.TAMANO][Tablero.TAMANO];
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                JButton casilla = new JButton();
                casilla.setFont(new Font("Arial", Font.BOLD, 60));
                casilla.setPreferredSize(new Dimension(120, 120));
                casilla.setFocusPainted(false);

                int fila = f;
                int columna = c;
                casilla.addActionListener(e -> clicEnCasilla(fila, columna));

                casillas[f][c] = casilla;
                panelTablero.add(casilla);
            }
        }

        // Parte de abajo: botón para empezar otra partida
        JButton botonReiniciar = new JButton("Nueva Partida / Reiniciar");
        botonReiniciar.setFont(new Font("Arial", Font.PLAIN, 16));
        botonReiniciar.addActionListener(e -> nuevaPartida());
        JPanel panelInferior = new JPanel();
        panelInferior.add(botonReiniciar);

        setLayout(new BorderLayout());
        add(etiquetaEstado, BorderLayout.NORTH);
        add(panelTablero, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // centrar la ventana en la pantalla

        nuevaPartida();
    }

    /** Se ejecuta cuando el jugador hace clic en una casilla. */
    private void clicEnCasilla(int fila, int columna) {
        // Juego revisa que sea el turno del jugador y que la casilla esté libre
        if (!juego.jugarHumano(fila, columna)) {
            return;
        }
        marcarCasilla(fila, columna, juego.getHumano().getSimbolo());

        if (juego.isTerminado()) {
            mostrarResultado();
        } else {
            etiquetaEstado.setText("Turno de la computadora (O)...");
            // Pequeña pausa antes de que juegue la IA para que se note el cambio de turno
            Timer pausa = new Timer(500, e -> turnoComputadora());
            pausa.setRepeats(false);
            pausa.start();
        }
    }

    /** La computadora hace su jugada. */
    private void turnoComputadora() {
        int[] movimiento = juego.jugarComputadora();
        if (movimiento == null) {
            return; // ya no era su turno (por ejemplo, se reinició durante la pausa)
        }
        marcarCasilla(movimiento[0], movimiento[1], juego.getComputadora().getSimbolo());

        if (juego.isTerminado()) {
            mostrarResultado();
        } else {
            etiquetaEstado.setText("Tu turno (X)");
        }
    }

    /** Muestra la ficha en la casilla y la deshabilita para evitar tiros duplicados. */
    private void marcarCasilla(int fila, int columna, char simbolo) {
        JButton casilla = casillas[fila][columna];
        casilla.setText(String.valueOf(simbolo));
        casilla.setBackground(simbolo == 'X' ? COLOR_X : COLOR_O);
        casilla.setEnabled(false);
    }

    /** Anuncia el resultado y bloquea las casillas que quedaron libres. */
    private void mostrarResultado() {
        Jugador ganador = juego.getGanador();
        if (ganador == null) {
            etiquetaEstado.setText("¡Empate!");
        } else if (ganador == juego.getComputadora()) {
            etiquetaEstado.setText("¡Ganó la computadora (O)!");
        } else {
            etiquetaEstado.setText("¡Ganaste! (X)");
        }

        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                casillas[f][c].setEnabled(false);
            }
        }
    }

    /** Limpia el tablero y la ventana para empezar otra partida. */
    private void nuevaPartida() {
        juego.reiniciar();
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                casillas[f][c].setText("");
                casillas[f][c].setBackground(null); // vuelve al color normal del botón
                casillas[f][c].setEnabled(true);
            }
        }
        etiquetaEstado.setText("Tu turno (X)");
    }
}
