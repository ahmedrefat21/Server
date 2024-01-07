/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ahmed
 */
public class DataAccessLayer {
    
    private static DataAccessLayer DataRefaerence;
    private Connection con;
    private ResultSet rs ;
    private PreparedStatement pst;
    
    public synchronized ResultSet getResultSet(){
        return rs;
    }
    
    private  DataAccessLayer() throws SQLException{
         DriverManager.registerDriver(new ClientDriver());
         con = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe","root","root");
    }

    public synchronized static DataAccessLayer startDataBase() throws SQLException {
        if(DataRefaerence == null){
            DataRefaerence = new DataAccessLayer();
        }
        return DataRefaerence;
    }
    
    
    public synchronized void updateResultSet(){
    
        try {
            this.pst= con.prepareStatement("SELECT * FROM PLAYER",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            this.rs=pst.executeQuery();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
 
    }
    
    public synchronized int countOfflineUsers(){
    
        try {
            this.pst=con.prepareStatement("SELECT COUNT(*) FROM PLAYER WHERE ISONLINE = false",ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
            ResultSet result=pst.executeQuery();
            return result.next() ? result.getInt(1):result.getInt(-1);
        } catch (SQLException ex) {
             ex.printStackTrace();
        }
        return -1;
    }
    
    public synchronized void setOnline(boolean state,String email){
    
        try {
            pst=con.prepareStatement("UPDATE PLAYER SET ISONLINE = ? WHERE EMAIL = ?");
            pst.setString(1,state+"");
            pst.setString(2, email);
            System.out.println("email:"+email+"is online now");
            pst.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
         
         
    
    }
}
