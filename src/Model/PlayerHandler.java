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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahmed
 */
public class PlayerHandler {
    private String username,email;
   private String password;
   private StringTokenizer token;
   static ArrayList<PlayerHandler> activeUsers = new ArrayList(); 
    static HashMap<String,PlayerHandler> game = new HashMap();
    
    
    
    
    
    
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
    
    
    
     private void signUp(){
        String username = token.nextToken();
        email = token.nextToken();
        String password = token.nextToken();
        System.out.println(username+" "+email+" "+password);
        String data;

       try{
            data = server.checkSignUp(username, email);
            ps.println(data);
            if(data.equals("Registered Successfully")){
                ps.println(username+"###"+email); 
                server.SignUp(username,email,password);
                System.out.println("User is registered now , check database");   
                activeUsers.add(this);
            }else if (data.equals("already signed-up")){
                ps.println("already signed-up"+"###");
            }
       }catch(SQLException e){
           e.printStackTrace();
        }
   }
     
     private void myMove(){
       String mail = token.nextToken();
       String button = token.nextToken();  
       PlayerHandler p = game.get(mail);
       p.ps.println("gameTic");
       p.ps.println(button);
    } 
    
     private void oppositeMove(){
        String mail = token.nextToken();
        String button = token.nextToken();  
        PlayerHandler player = game.get(mail);
        if(game.containsKey(email)){
            game.remove(player.email);
            game.remove(this.email);
        }
        player.ps.println("finalgameTic");
        player.ps.println(button);
    }
     
}
