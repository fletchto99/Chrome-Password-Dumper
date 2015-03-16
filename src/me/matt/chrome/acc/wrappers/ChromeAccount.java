package me.matt.chrome.acc.wrappers;

public class ChromeAccount {

    private String username;
    private String URL;
    private String password;

    public ChromeAccount(String username, String password, String URL) {
        this.username = username;
        this.URL = URL;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getURL() {
        return URL;
    }

    public String getPassword() {
        return password;
    }

}
