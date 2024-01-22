/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import dao.DAO;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ahmed
 */
public class ServerHandler {
    
    private static ServerHandler server;
    public DAO database ;
    private ServerSocket serverSocket ;
    private Socket socket ;
    private Thread thread;

    
    private ServerHandler(){}
    
    public static ServerHandler getServer(){
        if(server == null){
            server = new ServerHandler();
        }
        return server;
    }
    
    
    private void startServer(){
        try {
            serverSocket = new ServerSocket(5005);
            thread = new Thread(() -> {
                while(true){
                    try {
                        socket = serverSocket.accept();
                        new PlayerHandler(socket); 
                    }catch (IOException ex) {
                        ex.printStackTrace();
                    }    
                }
            });
            thread.start();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public String checkIslogin(String email,String password){
        return database.checkisalreadyloginIn(email, password);
    }
    
     public void setOnline(Boolean state, String mail){
        database.setOnline(false,mail);
    }
     
    public int retriveScore(String email){
        return database.retriveScore(email);
    }
    
    public void login(String email,String password) throws SQLException{
        database.login(email, password);
    }
    
    
    
}
