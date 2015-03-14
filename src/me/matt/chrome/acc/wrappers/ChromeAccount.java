package me.matt.chrome.acc.wrappers;

import com.sun.jna.platform.win32.Crypt32Util;

public class ChromeAccount {

    private String username;
    private String URL;
    private String decryptedPassword;

    public ChromeAccount(String username, byte[] encryptedPassword, String URL) {
        this.username = username;
        this.URL = URL;
        this.decryptedPassword = new String(
                Crypt32Util.cryptUnprotectData(encryptedPassword));// TODO: Figure out mac encryption
    }

    public String getUsername() {
        return username;
    }

    public String getURL() {
        return URL;
    }

    public String getPassword() {
        return decryptedPassword;
    }

}
