package me.matt.chrome.acc.util;

import java.io.File;

import me.matt.chrome.acc.Application;

public enum OperatingSystem {

    WINDOWS(System.getProperty("user.home") + File.separator
            + "AppData\\Local\\Google\\Chrome\\User Data\\", Application.class
            .getProtectionDomain().getCodeSource().getLocation().toString()
            .replace("%20", " ").replace("file:/", "")
            .replace("/", File.separator)), MAC(System.getProperty("user.home")
            + File.separator + "Library/Application Support/Google/Chrome/",
            Application.class.getProtectionDomain().getCodeSource()
                    .getLocation().toString().replace("%20", " ")
                    .replace("file:", "").replace("/", File.separator)), LINUX(
            System.getProperty("user.home") + File.separator
                    + ".config/google-chrome/", Application.class
                    .getProtectionDomain().getCodeSource().getLocation()
                    .toString().replace("%20", " ").replace("file:", "")
                    .replace("/", File.separator)), UNKNOWN("", "");

    OperatingSystem(String path, String runningPath) {
        this.path = path;
        this.runningPath = runningPath;
    }

    public String getChromePath() {
        return path;
    }

    public String getSavePath() {
        return runningPath.substring(0,
                runningPath.lastIndexOf(File.separatorChar) + 1)
                + "Accounts";
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
    private String runningPath;

}
