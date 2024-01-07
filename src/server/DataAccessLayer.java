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
    
    public synchronized String checkRegister(String username , String email){
        ResultSet checkRs;
        PreparedStatement pstCheck;
        
        try {
            String queryString= new String("select USERNAME from PLAYER where USERNAME = ?");
            pstCheck = con.prepareStatement("select * from PLAYER where USERNAME = ? and EMAIL = ?");
            pstCheck.setString(1, username);
            pstCheck.setString(2, email);
            checkRs = pstCheck.executeQuery();
            if(checkRs.next()){
                return "already signed-up";
            }
        } catch (SQLException ex) {
            System.out.println("here ");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Registered Successfully";
    } 
    
    public synchronized String checkSignIn(String email, String password){
        ResultSet checkRs;
        PreparedStatement pstCheck;
        String check;       
        System.out.println("checkSignIn " +checkIsActive(email));
        if(!checkIsActive(email)){
            System.out.println(" checkSignIn: " +checkIsActive(email));
                try { 
            pstCheck = con.prepareStatement("select * from PLAYER where EMAIL = ? ");
            pstCheck.setString(1, email);
            checkRs = pstCheck.executeQuery();
            if(checkRs.next()){
                if(password.equals(checkRs.getString(4))){
                    return "Logged in successfully";
                }
                return "Password is incorrect";
            }
            return "Email is incorrect";
          } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
            return "Connection issue, please try again later";
             }
        }else{
            System.out.println("This Email alreay sign-in " + checkIsActive(email));
           return "This Email is alreay sign-in";  
        }
              
    }
    
     public synchronized int getScore(String email){
        int score;
        ResultSet checkRs;
        PreparedStatement pstCheck;
 
        try {
            pstCheck = con.prepareStatement("select * from PLAYER where EMAIL = ?");
            pstCheck.setString(1, email);
            checkRs = pstCheck.executeQuery();
            checkRs.next();
            score = checkRs.getInt(5);
            return score;
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    } 
       
}