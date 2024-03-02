/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package newpackage;

/**
 *
 * @author abdelrhman_shehata
 */
public class MyTwilio {

    static String ACCOUNT_SID;
    static String AUTH_TOKEN;

    public MyTwilio(String ACCOUNT_SID, String AUTH_TOKEN) {

        this.ACCOUNT_SID = ACCOUNT_SID;
        this.AUTH_TOKEN = AUTH_TOKEN;
    }

    String Get_ACCOUNT_SID() {
        return ACCOUNT_SID;
    }

    String Get_AUTH_TOKEN() {
        return AUTH_TOKEN;
    }

}
