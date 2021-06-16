package ServerClient;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable {
    private Socket socket;
    private DataInputStream reader;
    private DataOutputStream writer;
    private Scanner scanner;
    private boolean exit = false;


    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.doSome();


    }

    public void doSome() {
        try {
            create();
            new Thread(this).start();

            startMessaging();

            reader.close();
            writer.close();
            socket.close();
            System.exit(0);
        } catch (IOException ioException) {
            System.out.println("Could not connect to the server :(((");
            ioException.printStackTrace();
        }
    }


    private void create() throws IOException {
        String ipAddress = "127.0.0.1", port = "7000";
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("please enter the ip address :");
//        ipAddress = scanner.nextLine();
//        System.out.println("please enter the port :");
//        port = scanner.nextLine();
        socket = new Socket(ipAddress, Integer.parseInt(port));
        System.out.println("\n\nYou are connected to the game server :)");
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());

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
            String rec = reader.readUTF();
            if (rec == null) {
                continue;
            }
            if (rec.equals("Exit")) {
                exit = true;
                System.out.println("\n\nwrite any text you want , or click enter to exit the game :)");
                break;
            }
            System.out.println(rec);
        }
    }

    public void startMessaging() throws IOException {
        String message;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (exit)
                break;
            message = scanner.nextLine();
            if (message != null) {
                writer.writeUTF(message);
            }
        }
    }


}



