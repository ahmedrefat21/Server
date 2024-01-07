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
        public synchronized void login(String email,String password) throws SQLException{

        pst = con.prepareStatement("update player set ISONLINE = ?  where email = ? and password = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
        pst.setString(1, "true");
        pst.setString(2, email);
        pst.setString(3, password);
        pst.executeUpdate(); 
       // updateResultSet();    
       //waiting for marim func
    }
    
    public synchronized void SignUp(String username , String email, String password) throws SQLException{

        pst = con.prepareStatement("insert into player(username,email,password) values(?,?,?)");
        pst.setString(1, username);
        pst.setString(2, email);
        pst.setString(3, password);
        pst.executeUpdate();
        login(email,password);

    }
    


        
    }
    

