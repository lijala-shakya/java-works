/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author DELL
 */
public class SessionInfo {
     private String username;
    private String loginTime;

    public SessionInfo(String username, String loginTime) {
        this.username = username;
        this.loginTime = loginTime;
    }

    public String getUsername() {
        return username;
    }

    public String getLoginTime() {
        return loginTime;
    }
}
