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
        public synchronized void login(PlayerDTO player) throws SQLException{
                //take email and password
        pst = con.prepareStatement("update player set ISONLINE = ?  where email = ? and password = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
        pst.setString(1, "true");
        pst.setString(2, player.getEmail());
        pst.setString(3, player.getPassword());
        pst.executeUpdate(); 
       // updateResultSet();    
       //waiting for marim func
    }
    //username ,email, pass
    public synchronized void SignUp(PlayerDTO player) throws SQLException{
                //take username - email - password
        pst = con.prepareStatement("insert into player(username,email,password) values(?,?,?)");
        pst.setString(1, player.getUsername());
        pst.setString(2, player.getEmail());
        pst.setString(3, player.getPassword());
        pst.executeUpdate();
        login(player); ///it takes username + password

    }
    
        public synchronized void changeOnline(){
        try {
            pst = con.prepareStatement("update player set ISONLINE = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, "false"); //not online
            pst.executeUpdate(); 
         //   updateResultSet();
             } catch (SQLException ex) {
            System.out.println("Changed user status to offline");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }}
        
       public synchronized void changeToNotAvailable(){
         try {
            pst = con.prepareStatement("update player set AVALIBLE = ? ",ResultSet.TYPE_SCROLL_SENSITIVE ,ResultSet.CONCUR_UPDATABLE  );
            pst.setString(1, "false"); //he is playing now
            //mafrood a7ot playe.setAvailabe("false")???????
            pst.executeUpdate();
          //  updateResultSet();
          } catch (SQLException ex) {
            System.out.println("Changed user status to NOT AVALIBLE");
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
       
        public synchronized void disableConnection() throws SQLException{
            changeOnline();
            changeToNotAvailable();

           con.close();
           rs.close();
           pst.close();
           DataRefaerence = null;
    }

        
    }
    

