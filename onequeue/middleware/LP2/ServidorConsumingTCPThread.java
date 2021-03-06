package middleware.LP2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class ServidorConsumingTCPThread extends Thread{
    private Socket client;
    private ServidorConsumingTCP tcpserver;
    private int clientID;                 
    private boolean running = false;
    public PrintWriter mOut;
    public BufferedReader in;
    private ServidorConsumingTCP.OnMessageReceived messageListener = null;
    private String message;
    ServidorConsumingTCPThread[] cli_amigos;

    public ServidorConsumingTCPThread(Socket client_, ServidorConsumingTCP tcpserver_, int clientID_,ServidorConsumingTCPThread[] cli_ami_) {
        this.client = client_;
        this.tcpserver = tcpserver_;
        this.clientID = clientID_;
        this.cli_amigos = cli_ami_;
    }

    
    public void run() {
        running = true;
        try {
            try {               
                boolean soycontador = false;                
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
                //System.out.println("TCP Server"+ "C: Sent.");
                messageListener = tcpserver.getMessageListener();
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while (running) {
                    message = in.readLine();
                    
                    if (message != null && messageListener != null) {
                        
                        messageListener.messageReceived(message);
                    }
                    

                    message = null;
                }
                System.out.println("RESPONSE FROM CLIENT"+ " S: Received Message: '" + message + "'");
            } catch (Exception e) {
                System.out.println("Middleware Consumming"+ " S: Error"+ e);
            } finally {
                client.close();
            }

        } catch (Exception e) {
            System.out.println("Middleware Consumming"+ " C: Error"+ e);
        }
    }
    
    public void stopClient(){
        running = false;
    }
    
    public void sendMessage(String message){//funcion de trabajo
        if (mOut != null && !mOut.checkError()) {
            mOut.println( message);
            mOut.flush();
        }
    }
}
