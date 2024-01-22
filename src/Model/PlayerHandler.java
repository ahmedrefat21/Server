/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Controller.ServerMainPageController;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
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
    
    @Override
  public void initialize(URL location, ResourceBundle resources) {
        login = false;
        System.out.println("initi");
        result = server.getResultSet();
    }
    
    
    
    
    
    
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
    
    private void login(){
        email = token.nextToken();
        String password = token.nextToken();
        String data;
        int score;
        try{
            data = server.checkIslogin(email, password);
            if(data.equals("Logged in successfully")){
                score = server.retriveScore(email);
                username = server.getPlayerUserName(email);
                server.login(email, password);
                ps.println(data +"###" + score);
                ps.println(username+"###"+email+"###"+score); // send data to registerController
                login = true;
                activeUsers.add(this);
            }else if(data.equals("This Email is alreay sign-in")){
                System.out.println("alread in connected");
                ps.println(data +"###");
            }else if(data.equals("Email is incorrect")){
                ps.println(data +"###");
            }else if(data.equals("Password is incorrect")){
                ps.println(data +"###");
            }else if(data.equals("Connection issue, please try again later")){
                ps.println(data +"###");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        token = null;
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
     private void sendInvitation(){ 
        String player2Mail = token.nextToken(); 
        String player1Data = token.nextToken(""); 
        for(PlayerHandler i : activeUsers){
            System.out.println("Data of enemy sent");
            if(i.email.equals(player2Mail)){
              i.ps.println("requestPlaying");
              i.ps.println(player1Data);
            }
        }      
    }
    
   
     private void acceptGame(){
  
        String player2 = token.nextToken(); 
        String player2Name = token.nextToken();
        String player1 = token.nextToken();
        server.makeBusy(player1, player2);
        System.out.println("Two Players playing now");
        PlayerHandler p1 = null;
        PlayerHandler p2 = null;
        for(PlayerHandler i : activeUsers){
            if(i.email.equals(player1)){
                p1 = i;
            }else if(i.email.equals(player2)){
                p2 = i;
            }
        }
        if(p1 == null || p2 == null){
        System.out.print("Error!, please try again");
        System.out.println("Not Found");
        }else{
            game.put(player1, p2);
            game.put(player2, p1);
            p1.ps.println("gameOn");
            p1.ps.println(player2Name);
            p1.ps.println(p2.server.retriveScore(p2.email));
        } 
     }
    private void withdraw(){ 
        PlayerHandler player = null;
        player = game.get(this.email);
        if(player != null){
            player.ps.println("withdraw");
            game.remove(this.email);
            game.remove(player.email);
        }
    } 
    
    private void refuseGame(){
            System.out.println("refused");
            String OpponentMail = token.nextToken();
            for(PlayerHandler player : activeUsers){
                if(player.email.equals(OpponentMail)){
                    player.ps.println("decline");
                }
            }
        }
    
    private synchronized void restate(){
        System.out.println("reset: " +email);
        server.setNotBusy(email);
    }
    
    
    private void updateScore(){
       String mail = token.nextToken();
       int score = Integer.parseInt(token.nextToken());
       System.out.println(score);
       server.updateScore(mail, score);
   }

}
