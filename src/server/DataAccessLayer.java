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
    
    public synchronized String getUserName(String email){
        String userName;
        ResultSet result;
        PreparedStatement pstCheck;
        try {
            pstCheck = con.prepareStatement("select * from player where email = ?");
            pstCheck.setString(1, email);
            result = pstCheck.executeQuery();
            result.next();
            userName = result.getString(2);
            return userName;
        } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public synchronized void isPlaying(String player1, String player2){
        try {
            pst = con.prepareStatement("update player set avalible = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, player1);
            pst.executeUpdate(); 
            pst = con.prepareStatement("update player set avalible = true  where email = ?",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, player2);
            pst.executeUpdate();
            //updateResultSet();
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Boolean checkIsOnline(String email){
          ResultSet checkRs;
          PreparedStatement pstCheck;
          Boolean isActive ;
         try {
            pstCheck = con.prepareStatement("select isonline from player where email = ?");
            pstCheck.setString(1, email);
            checkRs = pstCheck.executeQuery();
            checkRs.next();
            System.out.println("checkIsActive true ");
            isActive = checkRs.getBoolean("isactive");
            System.out.println("checkIsActive " +isActive);
            return isActive ;
         } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            //Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
         }
         return false;
         
     }
    
    public synchronized boolean checkPlaying(String player){

        boolean available;
        ResultSet result;
        PreparedStatement pstCheckAv;
        try {
            pstCheckAv = con.prepareStatement("select * from player where username = ?");
            pstCheckAv.setString(1, player);
            result = pstCheckAv.executeQuery();
            result.next();
            available = result.getBoolean(4);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public synchronized String getData(String username){
        String email;
        ResultSet result;
        PreparedStatement pstCheck;
        try {
            pstCheck = con.prepareStatement("select * from player where email = ?");
            pstCheck.setString(1, username);
            result = pstCheck.executeQuery();
            result.next();
            email = result.getString(3);
            return email;
        } catch (SQLException ex) {
            System.out.println("Invalod Email address");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    
}
