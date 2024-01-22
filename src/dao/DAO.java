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
import org.apache.derby.jdbc.ClientDriver;

/**
 *
 * @author ahmed
 */
public class DAO {
    
    private static DAO db;
    private Connection connection;
    private ResultSet result ;
    private PreparedStatement pst;
    
    public synchronized ResultSet getResultSet(){
        return result;
    }
    
    private  DAO() throws SQLException{
         DriverManager.registerDriver(new ClientDriver());
         connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToe","root","root");
    }
    
    
    
    public synchronized int retriveScore(String email){
        try {
            DriverManager.registerDriver(new ClientDriver());
            connection = DriverManager.getConnection("jdbc:derby://localhost:1527/TicTacToy", "root", "root");
            PreparedStatement pst = connection.prepareStatement("select * from player where email = ?");
            pst.setString(1, email);
            result = pst.executeQuery();
            result.next();
            int score = result.getInt(5);
            return score;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    } 
    
    
    public synchronized String checkisalreadyloginIn(String email, String password){
        if(!checkIsOnline(email)){
            try { 
                PreparedStatement pst = connection.prepareStatement("select * from player where email = ? ");
                pst.setString(1, email);
                result = pst.executeQuery();
                if(result.next()){
                    if(password.equals(result.getString(4))){
                        return "Logged in successfully";
                    }
                    return "Password is incorrect";
                }
                return "Email is incorrect";
            } catch (SQLException ex) {
                ex.printStackTrace();
                return "Connection issue, please try again later";
            }
        }else{
            return "This Email is alreay sign-in";  
        }
              
    }
    
}
