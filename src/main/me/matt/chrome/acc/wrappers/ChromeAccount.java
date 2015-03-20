package me.matt.chrome.acc.wrappers;

public class ChromeAccount {

    private final String username;
    private final String URL;
    private final String password;

    public ChromeAccount(final String username, final String password,
            final String URL) {
        this.username = username;
        this.URL = URL;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getURL() {
        return URL;
    }

    public String getUsername() {
        return username;
    }

}
