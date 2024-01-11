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
    private String userName;
    private String password;
    private String email;
    private int points;
    private boolean isOnline;
    private boolean available;

    public PlayerDTO(String userName, String password, int points, boolean isOnline, boolean available) {
        this.userName = userName;
        this.password = password;
        this.points = points;
        this.isOnline = isOnline;
        this.available = available;
    }
    
        public PlayerDTO(String userName, boolean isOnline, boolean available) {
        this.userName = userName;
        this.isOnline = isOnline;
        this.available = available;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

        public String getEmail() {
        return email;
    }

    public void setPoints(String email ) {
        this.email = email;
    }
    
    
    public int getPoints() {
        return points;
    }

    public void setPoints(int Points) {
        this.points = Points;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
        
}
