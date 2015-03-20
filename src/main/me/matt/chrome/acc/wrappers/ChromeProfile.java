package me.matt.chrome.acc.wrappers;

public class ChromeProfile {

    private final String name;
    private final int id;

    public ChromeProfile(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return id > 0 ? "Profile " + id : "Default";
    }

}
