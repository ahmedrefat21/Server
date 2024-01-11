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
        initServer(); // enable socket server

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
    
    private void initServer(){
        try {
            serverSocket = new ServerSocket(5005);
            listener = new Thread(() -> {
                while(true){
                    try {
                        socket = serverSocket.accept();
          //              new ConnectedPlayer(socket); // not mandatory to be online
                    }catch (IOException ex) {
            //            Logger.getLogger(ServerMainPageController.class.getName()).log(Level.SEVERE, null, ex);
                      ex.printStackTrace();
                    }
                    
                }
            });
            listener.start();
        }catch (IOException ex) {
            System.out.println("server exception");
      
        }
    }
    public String checkRegister(PlayerDTO player){
        return databaseInstance.checkRegister(player.getUsername(), player.getEmail());
    }
    public void SignUp(PlayerDTO player) throws SQLException{
        databaseInstance.SignUp(player.getUsername(),player.getEmail(),player.getPassword());
    }
    public ResultSet getResultSet(){
        return databaseInstance.getResultSet();
    }
}
