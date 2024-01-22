/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

public class DAO {
    
    public synchronized ResultSet getOnlinePlayers( ){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps =connection.prepareStatement("Select * from player where isactive = true ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return ps.executeQuery(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
        
    } 
        public synchronized int getOfflineUsers() {
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement ps = connection.prepareStatement("select count(*) from player where isactive = false", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }   
        
        
        public synchronized ResultSet getActivePlayersChart(){
        try {
            this.pst =connection.prepareStatement("SELECT COUNT(*) AS true_count FROM PLAYER WHERE isactive = true",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return pst.executeQuery(); 
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println("catch getactive");
            return null;
        }
        
    }
        
        public synchronized ResultSet getOfflinePlayers( ){   
        try {
            this.pst =connection.prepareStatement("SELECT COUNT(*) AS false_count FROM PLAYER WHERE isactive = false",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_READ_ONLY  );
            return pst.executeQuery(); 
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("catch getactive");
            return null;
        }
        
    }
    
   

    
    public synchronized void disConnection() {
        try {
            changeToOffline();
            changeToNotBusy();
            result.close();
            pst.close();
            connection.close();
            db = null;
             } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }    
    
    
}
