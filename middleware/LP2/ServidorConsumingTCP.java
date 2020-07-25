package middleware.LP2;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorConsumingTCP {
    private String message;
    
    int nrcli = 0;

    public static final int SERVERPORT = 4444;
    private OnMessageReceived messageListener = null;
    private boolean running = false;
    ServidorConsumingTCPThread[] sendclis = new ServidorConsumingTCPThread[10];

    PrintWriter mOut;
    BufferedReader in;
    
    ServerSocket serverSocket;
    //BattleCity bc = new BattleCity(nrcli);

    //el constructor pide una interface OnMessageReceived
    public ServidorConsumingTCP(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
    }
    
    public OnMessageReceived getMessageListener(){
        return this.messageListener;
    }
    
    public void sendMessageTCPServer(String message){
        for (int i = 1; i <= nrcli; i++) {
            sendclis[i].sendMessage(message);
        }
    }
    
    
    public void run(){
        running = true;
        try{
            System.out.println("Middleware Consuming"+"S : Connecting...");
            serverSocket = new ServerSocket(SERVERPORT);
            
            while(running){
                Socket client = serverSocket.accept();
                System.out.println("Middleware Consuming"+"S: Receiving...");
                nrcli++;
                System.out.println("Conexion establecida con el CONSUMIDOR " + nrcli);
                sendclis[nrcli] = new ServidorConsumingTCPThread(client,this,nrcli,sendclis);
                Thread t = new Thread(sendclis[nrcli]);
                t.start();
                System.out.println("Nuevo conectado: "+ nrcli+"");
                //bc.nrclient++;
                //bc.run()boolean condition = true;
              
            }
            
        }catch( Exception e){
            System.out.println("Error"+e.getMessage());
        }finally{

        }
    }
    public  ServidorConsumingTCPThread[] getClients(){
        return sendclis;
    } 

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
