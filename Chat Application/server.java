import java.io.*;
import java.net.*;
class server {
    ServerSocket server;
    Socket socket;
    BufferedReader br;  //data read
    PrintWriter out;    //data write


    public server(){    //constructor
        try{
        server= new ServerSocket(7777);
        System.out.println("Server is ready to accept connection");
        System.out.println("waiting...");
        socket=server.accept();
        
        br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting(); 

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void startReading() {
        //thread = read
        Runnable r1=()-> {
            System.out.println("Reader started...");
        try{
            while (true) { //msg from client
                String msg = br.readLine();
                if(msg.equals("exit")){
                    System.out.println("Client terminated the chat");
                    socket.close();             //when the message gets exits 
                    break;
                    }
                    System.out.println("Client: "+msg);
                } 
        }catch(Exception e){
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
        System.out.println("This is server..going to start server");
        new server();
    }
}