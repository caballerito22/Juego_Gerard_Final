import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Esperando jugadores... Un momento por favor.");

        // Conexión de los jugadores
        Socket socket1 = serverSocket.accept();
        PrintWriter writer1 = new PrintWriter(socket1.getOutputStream(), true);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

        Socket socket2 = serverSocket.accept();
        PrintWriter writer2 = new PrintWriter(socket2.getOutputStream(), true);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

        // Mensaje de bienvenida y nombre de los jugadores
        writer1.println("Bienvenido al Juego del Nim!, estamos buscando otro jugador para empezar...");
        writer2.println("Bienvenido al Juego del Nim! ");

        String player1Name = reader1.readLine();
        String player2Name = reader2.readLine();

        writer1.println("Hola, " + player1Name + ". VAMOS A EMPEZAR !!!");
        writer2.println("Hola, " + player2Name + ". El juego comienza ahora.");
        writer1.println(player1Name + " te ha tocado jugar contra " + player2Name + " ¿crees que puedes ganarle? Demuéstralo");
        writer2.println(player2Name + ", vas a jugar contra " + player1Name + " es hora de demostrar lo que sabes.");
        writer1.println("El juego es al mejor de 3 partidas, es decir, el que gane dos partidas, será el ganador.");
        writer2.println("El juego es al mejor de 3 partidas, es decir, el que gane dos partidas, será el ganador.");

        // Variables para el conteo de victorias
        int winsP1 = 0, winsP2 = 0;

        // Juego principal
        while (winsP1 < 2 && winsP2 < 2) {
            // Configuración del juego
            int[][] tablero = {
                    {1, 1, 1, 1, 1}, // Montón 1 con 5 fichas (5 palos)
                    {1, 1, 1, 1},     // Montón 2 con 4 fichas (4 palos)
                    {1, 1, 1}         // Montón 3 con 3 fichas (3 palos)
            };

            int currentPlayer = 1;

            // Mostrar el tablero al principio de cada partida
            showBoard(writer1, writer2, tablero);

            // Juego de la partida
            while (true) {
                if (currentPlayer == 1) {
                    writer1.println(player1Name + ", es tu turno.");
                    writer2.println("Un momento, es turno de " + player1Name + ".");
                    int pile = Integer.parseInt(reader1.readLine()) - 1;  // Convertir de 1-3 a 0-2
                    int tokens = Integer.parseInt(reader1.readLine());

                    if (tablero[pile].length >= tokens) {
                        // Quitar las fichas seleccionadas
                        tablero[pile] = Arrays.copyOf(tablero[pile], tablero[pile].length - tokens);
                        currentPlayer = 2;
                    } else {
                        writer1.println("No puedes quitar más fichas de las que hay en el montón.");
                    }
                } else {
                    writer2.println(player2Name + ", es tu turno.");
                    int pile = Integer.parseInt(reader2.readLine()) - 1;  // Convertir de 1-3 a 0-2
                    int tokens = Integer.parseInt(reader2.readLine());

                    if (tablero[pile].length >= tokens) {
                        // Quitar las fichas seleccionadas
                        tablero[pile] = Arrays.copyOf(tablero[pile], tablero[pile].length - tokens);
                        currentPlayer = 1;
                    } else {
                        writer2.println("No puedes quitar más fichas de las que hay en el montón.");
                    }
                }

                // Mostrar el tablero después de cada movimiento
                showBoard(writer1, writer2, tablero);

                // Comprobar si alguno de los jugadores ha ganado esta partida
                boolean gameWon = true;
                for (int i = 0; i < tablero.length; i++) {
                    if (tablero[i].length > 0) {
                        gameWon = false;
                        break;
                    }
                }

                if (gameWon) {
                    if (currentPlayer == 1) {
                        winsP2++;
                        writer1.println("Lo siento "+player1Name+ " has perdido :/");
                        writer2.println("Muy bien "+player2Name+ "has ganado!!");
                    } else {
                        winsP1++;
                        writer1.println("Muy bien "+player1Name+ "has ganado!!");
                        writer2.println("Lo siento "+player2Name+ " has perdido :/");
                    }
                    break;
                }
            }

            // Mostrar el marcador
            writer1.println("Marcador: " + player1Name + " " + winsP1 + " - " + winsP2 + " " + player2Name);
            writer2.println("Marcador: " + player2Name + " " + winsP2 + " - " + winsP1 + " " + player1Name);
        }

        // Final del juego
        if (winsP1 == 2) {
            writer1.println("Enhorabuena " + player1Name + " como ya has ganado dos partidas, HAS GANADO EL JUEGO !!!");
            writer2.println("Lo siento " +player2Name +" otra vez será, "+player1Name+ " te ha ganado");
        } else {
            writer1.println("¡" + player1Name + " ha perdido! " + player2Name + " ha ganado el juego.");
            writer2.println("¡Felicidades, " + player2Name + "! Has ganado el juego.");
        }

        socket1.close();
        socket2.close();
        serverSocket.close();
    }

    // Mostrar tablero
    private static void showBoard(PrintWriter writer1, PrintWriter writer2, int[][] tablero) {
        StringBuilder board = new StringBuilder();

        // Mostrar las fichas de cada montón, con espacio entre los palos
        for (int i = 0; i < tablero.length; i++) {
            board.append("Fila " + (i + 1) + ": "); // Muestra Fila 1, Fila 2, Fila 3

            // Dibujar los palos
            for (int j = 0; j < tablero[i].length; j++) {
                board.append("|   ");  // Añadido espacio entre los palos
            }

            // Salto de línea para la siguiente fila
            board.append("\n");
        }

        // Enviar el tablero a ambos jugadores
        writer1.println(board.toString());
        writer2.println(board.toString());
    }
}
