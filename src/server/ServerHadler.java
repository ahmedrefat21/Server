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
      //  databaseInstance.changeStateToOffline();
      //  databaseInstance.changeStateToNotPlaying();
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
    
    public void login(PlayerDTO player) throws SQLException{
            String email=player.getEmail();
            String password=player.getPassword();
        databaseInstance.login(player);
    }

       
    public String checkRegister(PlayerDTO player){
     String Username=player.getUsername();
      String email=player.getEmail();
        return databaseInstance.checkRegister(player);
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
    public void getActivePlayersOne(){
        databaseInstance.getOnlinePlayers();
    }

   
}
