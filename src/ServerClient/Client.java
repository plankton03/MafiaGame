package ServerClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;


    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.doSome();

    }

    public void doSome() {
        try {
            create();
            new Thread(this).start();

            startMessaging();
        } catch (IOException ioException) {
            System.out.println("Could not connect to the server :(((");
            ioException.printStackTrace();
        }
    }


    private void create() throws IOException {
        String ipAddress, port;
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter the ip address :");
        ipAddress = scanner.nextLine();
        System.out.println("please enter the port :");
        port = scanner.nextLine();
        socket = new Socket(ipAddress, Integer.parseInt(port));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }


    @Override
    public void run() {
        try {
            listening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void listening() throws IOException {
        while (true) {
            String rec = reader.readLine();
            if (rec == null) {
                continue;
            }
            System.out.println(rec);
        }
    }

    public void startMessaging() {
        String message;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            message = scanner.nextLine();
            if (message != null) {
                writer.println(message);
            }
        }
    }
}



