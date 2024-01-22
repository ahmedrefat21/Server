/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import dao.DAO;
import Controller.ServerMainPageController;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ServerHandler {
 
    
    
    
    
    
    
  public void startConnections() throws SQLException{
        
        database = DAO.getDataBase();
        database.changeToOffline();
        database.changeToNotBusy();
        database.ResultSet();
        
        startServer(); 
    }   
    
    
   public void getOnlinePlayers(){
        database.getOnlinePlayers();
    } 
    public ResultSet getActivePlayers(){
        return database.getOnlinePlayers();
    }

    
    
    
}
