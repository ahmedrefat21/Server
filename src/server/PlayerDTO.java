/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

/**
 *
 * @author Kimo Store
*/
public class PlayerDTO {
    private String Username;
    private String Password;
    private String email;
    private int Points;
    private boolean ISONLINE;
    private boolean Available;

    public PlayerDTO(String Username, String Password, int Points, boolean Online, boolean Available) {
        this.Username = Username;
        this.Password = Password;
        this.Points = Points;
        this.ISONLINE = ISONLINE;
        this.Available = Available;
    }
    
        public PlayerDTO(String Username, boolean Online, boolean Available) {
        this.Username = Username;
        this.ISONLINE = ISONLINE;
        this.Available = Available;
    }
         public PlayerDTO(String email){
         this.email=email;
         }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

        public String getEmail() {
        return email;
    }

    public void setPoints(String email ) {
        this.email = email;
    }
    
    
    public int getPoints() {
        return Points;
    }

    public void setPoints(int Points) {
        this.Points = Points;
    }

    public boolean isOnline() {
        return ISONLINE;
    }

    public void setOnline(boolean Online) {
        this.ISONLINE = ISONLINE;
    }

    public boolean isAvailable() {
        return Available;
    }

    public void setAvailable(boolean Available) {
        this.Available = Available;
    }
        
}
