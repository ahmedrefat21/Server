/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahmed
 */
public class PlayerHandler {
    
    
    
    
    
    
    
    
    public PlayerHandler(Socket socket){
       server = ServerHandler.getServer();
       try {
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
            mySocket = socket;
            this.start();
       }catch (IOException ex) {
            ex.printStackTrace();
           try {
               socket.close();
           } catch (IOException ex1) {
               Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex1);
           }
       }
   }
}
