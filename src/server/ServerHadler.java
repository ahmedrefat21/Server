/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerHadler {
    private static Server server;
    public DataAccessLayer databaseInstance ;
    private ServerSocket serverSocket ;
    private Socket socket ;
    private Thread listener;
    PlayerDTO player;

    
    private ServerHadler(){
        
    }
    
    public static Server getServer(){
        if(server == null){
            server = new Server();
        }
        return server;
    }
    public void enableConnections() throws SQLException{

       databaseInstance = DataAccessLayer.startDataBase();
       databaseInstance.changeToOffline(player);
       databaseInstance.setAvalible(player);
       databaseInstance.updateResultSet();
        enableSocketServer(); 

    }
    
    
    public void disableConnections(){
        try {
            databaseInstance.disableConnection();
            listener.stop();
            serverSocket.close();
        } catch (SQLException ex) {
            System.out.print("disable connection server");
        } catch (IOException ex) {
            System.out.print("disable connection server");
        }
    }
    
    private void enableSocketServer(){
        try {
            serverSocket = new ServerSocket(5005);
            listener = new Thread(() -> {
                while(true){
                    try {
                        socket = serverSocket.accept();
                       new ConnectedPlayer(socket); // not mandatory to be online
                    }catch (IOException ex) {
                      ex.printStackTrace();
                    }
                    
                }
            });
            listener.start();
        }catch (IOException ex) {
            System.out.println("server exception");
      
        }
    }
    
 
  
    
    public String getUserName(PlayerDTO player){
         String email=player.getEmail();
        return databaseInstance.getUserName(player);
    }

    public String checkRegister(PlayerDTO player){
     String Username=player.getUsername();
      String email=player.getEmail();
        return databaseInstance.checkRegister(player);
    }
        public void login(PlayerDTO player) throws SQLException{
            String email=player.getEmail();
            String password=player.getPassword();
        databaseInstance.login(player);
    }
        public String checkSignIn(PlayerDTO player){
        return databaseInstance.checkSignIn(player);
    }
    public void SignUp(PlayerDTO player) throws SQLException{
       String Username=player.getUsername();
       String email=player.getEmail();
       String Password =player.getPassword();
        databaseInstance.SignUp(player);
    }
    public ResultSet getResultSet(){
        return databaseInstance.getResultSet();
    }
        public ResultSet getActivePlayers(){
        return databaseInstance.getOnlinePlayers();
    }
    public void getActivePlayersOne(){
        databaseInstance.getOnlinePlayers();
    }
    public void setOnline(PlayerDTO player){ //changing his status to online
        String email=player.getEmail();
        boolean isOnline = true;
        player.setOnline(isOnline);
        databaseInstance.setOnline(player);
    }
        public void setOffline(PlayerDTO player){ //changing his status to offline
        String email=player.getEmail();
        boolean isOnline = false;
        player.setOnline(isOnline);
        databaseInstance.changeToOffline(player);
    }
        public void setNotAvalible(PlayerDTO player){
        String email=player.getEmail();
//        boolean available = true;
//         player.setAvailable(available);
        databaseInstance.setNotAvalible(player);
    }
    public void setAvalible(PlayerDTO player){
        String email=player.getEmail();
        databaseInstance.setAvalible(player);
    }
      public void setPlaying(PlayerDTO player1,PlayerDTO player2){
        databaseInstance.isPlaying(player1, player2);
    }
      
         public void setNotPlaying(PlayerDTO player1,PlayerDTO player2){
        databaseInstance.isNotPlaying(player1, player2);
    }
        public int getScore(PlayerDTO player){
        String email=player.getEmail();
        return databaseInstance.getScore(player);
    }
        
         public void updateScore(PlayerDTO player){
         String email=player.getEmail();
         int score=player.getPoints();
        databaseInstance.updateScore(player);
    }
}
