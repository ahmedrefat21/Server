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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;

/**
 *
 * @author ahmed
 */
public class PlayerHandler extends Thread implements Initializable {
   private ServerHandler server;
   private DataInputStream dis;
   private PrintStream ps;
   private Socket mySocket;
   private String playerData,action;
   private String username,email;
   private String password;
   private StringTokenizer token;
   private Boolean login;
   private Boolean refreshList;   
   private ResultSet result;
   private Thread thread;
   static ArrayList<PlayerHandler> activeUsers = new ArrayList(); 
   static HashMap<String,PlayerHandler> game = new HashMap(); 
    
    
    
    
    
    
    public void run(){
        while(mySocket.isConnected()){
            try {
                playerData = dis.readLine();
                if(playerData != null){
                    token = new StringTokenizer(playerData,"####");
                    action = token.nextToken();
                    switch(action){
                        case "SignIn":
                            login();
                            break;
                        case "playerlist":
                            OnlinePlayerList();
                            break;               
                        case "logout":
                            logout();
                            break;
                        default :
                            break;
                    }
               }
            } catch (IOException ex) {
                

                if(email != null){
                    server.setOnline(false,email);
                    server.setNotBusy(email);
                    activeUsers.remove(this);   

                }else{
                   
                   refreshList = true;
                 }
                try {
                    mySocket.close();
                } catch (IOException ex1) {
                    Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
                this.stop();
            }
        }
        if(mySocket.isClosed()){
            server.getOnlinePlayers();
        }
    }
    
    private void OnlinePlayerList(){
      thread =  new Thread(new Runnable() {
        @Override
        public void run() {
            
            while(true){
                if(ServerMainPageController.serverState){
                    result = server.getActivePlayers();
                    try {
                        while(result.next()){
                            ps.println(result.getString("username")+"###"+
                                    result.getString("email")+"###"+
                                    result.getBoolean("isActive")+"###"+
                                    result.getBoolean("isPlaying")+"###"+
                                    result.getInt("score")
                                );
                        }
                        ps.println("null");
                    } catch (SQLException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   try {
                    Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }else{
                    ps.println("close");
                    thread.stop();
                }
            }
        }
    });
    thread.start();
    }
    
    private void logout(){
        email = token.nextToken();
        System.out.println("Logout Email " + email);
        if(email != null){
            server.setOnline(false, email);
            activeUsers.remove(email);
        }
       try {
           mySocket.close();
       } catch (IOException ex) {
           Logger.getLogger(PlayerHandler.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
}
