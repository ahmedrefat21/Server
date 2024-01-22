/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.io.IOException;

/**
 *
 * @author ahmed
 */
public class ServerHandler {
    
    
    
    
    public void endConnections(){
        try {
            database.disConnection();
            thread.stop();
            serverSocket.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    
    public void setNotBusy(String email){
        database.setNotBusy(email);
    
        
        
        public String getPlayerUserName(String email){
        return database.getUserName(email);
    }
        
        
        
        public String checkSignUp(String username,String email){
        return database.checkIsalreadysignedup(username, email);
    }
}






