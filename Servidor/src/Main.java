import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        //reciclamos código del juego de piedra papel o tijera, ya que al principio hay que hacer lo mismo
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Esperando jugadores... Un momento por favor.");

        Socket socket1 = serverSocket.accept();
        PrintWriter writer1 = new PrintWriter(socket1.getOutputStream(), true);
        BufferedReader reader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

        Socket socket2 = serverSocket.accept();
        PrintWriter writer2 = new PrintWriter(socket2.getOutputStream(), true);
        BufferedReader reader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));

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

        //eso es para contar las partidas de cada player
        int winsP1 = 0, winsP2 = 0;

        //esto se hará mientras ninguno de los dos haya ganado (no hayan llegado a dos victorias)
        while (winsP1 < 2 && winsP2 < 2) {
            //la forma del tablero h palos que tiene
            int[][] tablero = {
                    {1, 1, 1, 1, 1},
                    {1, 1, 1, 1},
                    {1, 1, 1}
            };

            //empieza el jugador 1
            int turnoJugador = 1;

            //mostramos el tablero
            showBoard(writer1, writer2, tablero);

            //while true para que se ejecute siempre hasta que no queden palos (se activa el breack)
            while (true) {
                //cuando le toca al jugador 1, se lo dice y al dos que espere
                if (turnoJugador == 1) {
                    writer1.println(player1Name + ", es tu turno.");
                    writer2.println("Un momento, es turno de " + player1Name);
                    //convierte el texto en numero (lo parsea) y le resta uno porque en los arrays se empieza por 0
                    int fila = Integer.parseInt(reader1.readLine()) - 1;
                    //lo mismo pero sin -1 porque no hay array
                    int palos = Integer.parseInt(reader1.readLine());

                    //si hay más palos de los que quiere quitar
                    if (tablero[fila].length >= palos) {
                        //quita las fichas, crea una copia del tablero igual, pero con x palos menos (x= palos que quiere quitar el jugador)
                        tablero[fila] = Arrays.copyOf(tablero[fila], tablero[fila].length - palos);
                        //ponemos el turno al otro jugador
                        turnoJugador = 2;
                    } else {
                        //si elige mas fichas le informamos
                        writer1.println("No puedes quitar más fichas de las que hay en el montón.");
                    }
                    //lo mismo exacto, pero para el jugador 2
                } else {
                    writer2.println(player2Name + ", es tu turno.");
                    writer1.println("Un momento, es turno de " +player2Name);
                    int fila = Integer.parseInt(reader2.readLine()) - 1;
                    int palos = Integer.parseInt(reader2.readLine());

                    if (tablero[fila].length >= palos) {
                        tablero[fila] = Arrays.copyOf(tablero[fila], tablero[fila].length - palos);
                        //una vez hecho, otra vez al primer jugador
                        turnoJugador = 1;
                    } else {
                        writer2.println("No puedes quitar más fichas de las que hay en el montón.");
                    }
                }

                //pongo esto para que despues de cada jugada muestre el tablero
                showBoard(writer1, writer2,tablero);

                //creo un boolean para que se haga lo de arriba mientras no se gane
                //la inicio true y si el juego no se ha acabado la pongo false
                boolean gameWon = true;
                //recorro el tablero
                for (int i = 0; i < tablero.length; i++) {
                    if (tablero[i].length > 0) {
                        //si aun quedan fichas la pongo false porque el juego no ha acabado
                        gameWon = false;
                        break;
                    }
                }

                //informo de quien gana si se gana
                if (gameWon) {
                    if (turnoJugador == 1) {
                        winsP2++;
                        writer1.println("Lo siento "+player1Name+ " has perdido :/");
                        writer2.println("Muy bien "+player2Name+ "has ganado!!");
                    } else {
                        winsP1++;
                        writer1.println("Muy bien "+player1Name+ "has ganado!!");
                        writer2.println("Lo siento "+player2Name+ " has perdido :/");
                    }
                    //salimos del while true
                    break;
                }
            }

            //Mostrar como van después de cada partida.
            writer1.println("Marcador: " + player1Name + " " + winsP1 + " - " + winsP2 + " " +player2Name);
            writer2.println("Marcador: " + player2Name + " " + winsP2 + " - " + winsP1 + " " +player1Name);

            //Les digo cuántas partidas les quedan para ganar el juego (es al mejor de 3)
            writer1.println("Te quedan " + (2 - winsP1) + " partida/as para ganar el juego.");
            writer2.println("Te quedan " + (2 -winsP2) + " partida/as para ganar el juego.");


        }

        //si han llegado a las partidas que tienen que llegar, se lo digo y se avaba, al que pierde también se lo digo (CÓDIGO FACIL)
        if (winsP1 == 2) {
            writer1.println("Enhorabuena " + player1Name + " como ya has ganado dos partidas, HAS GANADO EL JUEGO !!!");
            writer2.println("Lo siento " +player2Name +" otra vez será, "+player1Name+ " te ha ganado");
        } else {
            writer1.println("¡" + player1Name + " ha perdido! " + player2Name + " ha ganado el juego.");
            writer2.println("¡Felicidades, " + player2Name + "! Has ganado el juego.");
        }

        //CERRAMOS TODO
        socket1.close();
        socket2.close();
        serverSocket.close();
    }

    //Función para el tablero         estos PrintWriter son para enviar el tablero
    private static void showBoard(PrintWriter writer1, PrintWriter writer2, int[][] tablero) {
        //esto admito que lo he buscado, sirve para representar el tablero (sirve como un string, pero así no ponemos todo el rato +)
        StringBuilder board = new StringBuilder();

        //Enseñar los palods de cada fila, con espacio para verlos bien.
        for (int i = 0; i < tablero.length; i++) {
            board.append("Fila " + (i + 1) + ": "); //Para enseñar la fila 1, 1+1, 2+1

            // Dibujar los palos
            for (int j = 0; j < tablero[i].length; j++) {
                board.append("|   ");  //Para añadir espacio entre los palos
            }

            //cuando acaba de dibujar los palos de una fila salta a la otra
            board.append("\n");
        }

        //enseñar el tablaro a los dos jugadores
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
También se puede poner que si eliges una fila que no tiene palos, directamente te ponga que esa fila no la puedes elegir.
 */

/*
Comentado y revisado all
 */

/*
No he puesto lo de que elijan los jugadores las rondas, porque como son 2, no lo va a elegir solo 1.
    He puesto que sea a 3 rondas, puedo poner que sea a las rondas que quiera.
    O también puedo preguntarle a los dos, y si la respuesta coincide hacer esas rondas, si no, hacer las que están puestas por defecto. Pero eso me parece muy díficil.
 */
