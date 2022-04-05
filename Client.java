import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client {
    String name=JOptionPane.showInputDialog(null, "Enter Your Sweet Name");
String friend=JOptionPane.showInputDialog(null,"Enter Your Friend Name ");
    Socket socket;
    BufferedReader br;
    PrintWriter pr;

    public Client() {
        try {
            System.out.println(name+" "+"is waiting to connect");
            socket = new Socket("localhost", 5555);
            System.out.println(friend+" "+"is connected");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pr = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("chat is terminated");
                        socket.close();
                        break;
                    }
                    System.out.println(friend +" :"+ msg);
                }
            } catch (Exception e) {
                System.out.println("error occured");
            }
        };
        new Thread(r1).start();

    }

    public void startWriting() {
        Runnable r2 = () -> {
            try {
                while (true) {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    pr.println(content);
                    pr.flush();
                }

            } catch (Exception e) {
                // TODO: handle exception
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        new Client();
    }
}
