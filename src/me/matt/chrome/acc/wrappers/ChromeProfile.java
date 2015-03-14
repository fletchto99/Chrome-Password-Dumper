package me.matt.chrome.acc.wrappers;

public class ChromeProfile {

    private String name;
    private int id;

    public ChromeProfile(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getPath() {
        return id > 0 ? "Profile " + id : "Default";
    }

    public String getName() {
        return name;
    }

}
