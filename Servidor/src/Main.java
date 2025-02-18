import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);

        ServerSocket serverSocket = new ServerSocket(8080);
        Socket socket = serverSocket.accept();

        PrintWriter socketWriter1 = new PrintWriter(socket.getOutputStream(), true);
        var socketReader1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String nombre1 = sc.nextLine();
    }
}