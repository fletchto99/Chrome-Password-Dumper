package me.matt.chrome.acc.util;

import java.io.File;

public enum OperatingSystem {

    WINDOWS(System.getProperty("user.home") + File.separator
            + "AppData\\Local\\Google\\Chrome\\User Data\\"), MAC(System
            .getProperty("user.home")
            + File.separator
            + "Library/Application Support/Google/Chrome/"), LINUX(System
            .getProperty("user.home")
            + File.separator
            + ".config/google-chrome/"), UNKNOWN("");

    OperatingSystem(String path) {
        this.path = path;
    }

    public String getChromePath() {
        return path;
    }

    public static OperatingSystem getOperatingsystem() {
        final String os = System.getProperty("os.name");
        if (os.contains("Mac")) {
            return MAC;
        } else if (os.contains("Windows")) {
            return WINDOWS;
        } else if (os.contains("Linux")) {
            return LINUX;
        } else {
            return UNKNOWN;
        }
    }

    private String path;

}
