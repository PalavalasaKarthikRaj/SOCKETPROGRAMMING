import java.net.*;
import java.io.*;
import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;

public class Server {
    static String name = JOptionPane.showInputDialog(null, "Enter Your Sweet Name");
    static String friend = JOptionPane.showInputDialog(null, "Enter Your Friend Name ");
    static String purpose1 = JOptionPane.showInputDialog(null, "what is your purpose of chat ");
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter pr;
    
    public Server() {
        try {
            server = new ServerSocket(5555);
            System.out.println(name + " " + "is waiting to connect");
            socket = server.accept();
            System.out.println(friend + " " + "is connected");
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
                    System.out.println(friend + " :" + msg);
                    
                }
            } catch (Exception e) {
                System.out.println("chat ended");
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
                System.out.println("error occured");
            }
        };
        new Thread(r2).start();
    }
    
    public static void main(String[] args) throws ClassNotFoundException {
        Server server = new Server();
      
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat", "root", "SmnPkr@14321");
        Statement stmt = conn.createStatement();) {
            java.util.Date date = new java.util.Date();
            ResultSet rs = stmt.executeQuery("SELECT time FROM chat_table ORDER BY id DESC LIMIT 1;");
            while(rs.next()) { 
                System.out.print("last login time: " + rs.getTime("time")+","+rs.getDate("time")+"\n");
            }
            java.sql.Timestamp sqlTime = new java.sql.Timestamp(date.getTime());
            PreparedStatement ps = conn.prepareStatement("insert into chat_table (time,name,friend,purpose) values(?,?,?,?)");
            ps.setTimestamp(1, sqlTime);
            ps.setString(2, name);
            ps.setString(3, friend);
            ps.setString(4, purpose1);
            ps.executeUpdate();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}