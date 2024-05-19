import java.awt.BorderLayout;
import java.awt.Font;
import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class client extends JFrame {
    Socket socket;
    BufferedReader br;  //data read
    PrintWriter out;    //data write

    //declare components
    private JLabel heading= new JLabel("Client Area");
    private JTextArea messageArea= new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);


    public client(){
        try{
            System.out.println("Sending request to server");
            socket= new Socket("127.0.0.1",7777);           //requst will be send 
            System.out.println("Connection done");
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out=new PrintWriter(socket.getOutputStream());
            creatGUI();
            handleEvents();

            startReading();
            // startWriting();

        } catch (Exception e) {

        }
    }

    private void handleEvents() {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // System.out.println("key released "+e.getKeyCode());
                if(e.getKeyCode()==10){                 //enter button
                    // System.out.println("You have pressed enter button");
                    String contentToSend=messageInput.getText();
                    messageArea.append("Me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();

                }
            }

        });

    }

    private void creatGUI() {
        this.setTitle("Client Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //coding for component 
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        


        //Frame layout 
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading,BorderLayout.NORTH);
        this.add(messageArea,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        

    }

    //reading from client 
    public void startReading() {
        //thread = read
        Runnable r1=()-> {
            System.out.println("Reader started...");
        try{
            while (true) { //msg from client
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Server terminated the chat");
                    JOptionPane.showMessageDialog(this,"Server terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                    }
                // System.out.println("Server: "+msg);
                    messageArea.append("Server: "+msg+ "\n");

            }
        } catch(Exception e){
            // e.printStackTrace();
            System.out.println("Connection is Closed");

        }
    };
    //Start thread
    new Thread (r1). start();
}
    public void startWriting() {
        //thread = data from user and send to client 
        Runnable r2=()-> {
            System.out.println("Writer started..");
        try{   
            while (!socket.isClosed()) {
               
                BufferedReader br1= new BufferedReader(new InputStreamReader(System.in));

                String content = br1.readLine();
                out.println(content);
                out.flush();

                if(content.equals("exit")){
                    socket.close();
                    break;
                }
            }
            System.out.println("Connection is Closed");
            }catch(Exception e){
                e.printStackTrace();
            }
        };
        //start thread
        new Thread (r2). start();
    }
    public static void main(String[] args) {
        System.out.println("This is Client..");

        //calling constructor is needed orelse run nahi hoga
        new client();

    }
}
