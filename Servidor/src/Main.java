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
        writer1.println();
        writer2.println("Bienvenido al Juego del Nim! ");
        writer2.println();

        String player1Name = reader1.readLine();
        String player2Name = reader2.readLine();

        writer1.println("Hola, " + player1Name + ". VAMOS A EMPEZAR !!!");
        writer2.println("Hola, " + player2Name + ". El juego comienza ahora.");
        writer1.println(player1Name + " te ha tocado jugar contra " + player2Name + " ¿crees que puedes ganarle? Demuéstralo");
        writer1.println();
        writer2.println(player2Name + ", vas a jugar contra " + player1Name + " es hora de demostrar lo que sabes.");
        writer2.println();
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
                    writer1.println("Un momento, es turno de " + player2Name + ".");
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

            //Mostrar como van después de cada partida.
            writer1.println("Marcador: " + player1Name + " " + winsP1 + " - " + winsP2 + " " + player2Name);
            writer2.println("Marcador: " + player2Name + " " + winsP2 + " - " + winsP1 + " " + player1Name);

            //Les digo cuántas partidas les quedan para ganar el juego (es al mejor de 3)
            writer1.println("Te quedan " + (2 - winsP1) + " partidas para ganar el juego.");
            writer2.println("Te quedan " + (2 - winsP2) + " partidas para ganar el juego.");


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

    //Función para el tablero
    private static void showBoard(PrintWriter writer1, PrintWriter writer2, int[][] tablero) {
        StringBuilder board = new StringBuilder();

        //Enseñar los palods de cada fila, con espacio para verlos bien.
        for (int i = 0; i < tablero.length; i++) {
            board.append("Fila " + (i + 1) + ": "); //Para enseñar la fila 1, 1+1, 2+1

            // Dibujar los palos
            for (int j = 0; j < tablero[i].length; j++) {
                board.append("|   ");  //Para añadir espacio entre los palos
            }

            // Salto de línea para la siguiente fila
            board.append("\n");
        }

        // Enviar el tablero a ambos jugadores
        writer1.println(board);
        writer2.println(board);
    }
}

/*
- Falta que cuando ponga una fila que no sea de 1 a 3 no pete.
- Falta que cuando elijo quitar <=0 palos no me pete, y pedir que lo vuelva a preguntar.
 */

/*
Ya está arreglado (en el cliente).
 */

/*
Falta mejorar que cuadno pones los palos a quitar de x fila, si es <=0 te lo vuelve a preguntar sin mostrarte el tablero, en cambio, si te pasas te vuelve a preguntar por la fila y te muestra el tablaro.
Cuando te pregunta por la fila, si no es 1, 2 o 3 te vuelve a preguntar si o si de la misma maner le pongas <=0 o =>4, eso está bien.
 */

/*
Comentado y revisado de la 115 a la 153
 */

/*
No he puesto lo de que elijan los jugadores las rondas, porque como son 2, no lo va a elegir solo 1.
    He puesto que sea a 3 rondas, puedo poner que sea a las rondas que quiera.
    O también puedo preguntarle a los dos, y si la respuesta coincide hacer esas rondas, si no, hacer las que están puestas por defecto. Pero eso me parece muy díficil.
 */
