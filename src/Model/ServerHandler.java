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
}






