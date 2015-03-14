package me.matt.chrome.acc;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import me.matt.chrome.acc.exception.DatabaseException;
import me.matt.chrome.acc.exception.UnsupportedOperatingSystemException;
import me.matt.chrome.acc.util.Dumper;

public class Application {

    public static void main(String[] args) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException,
            UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        String message;
        boolean silent = args.length > 0 ? args[0].equalsIgnoreCase("silent")
                : false;
        try {
            Dumper d = Dumper.dumpAccounts();
            message = d.saveToFile() ? "Finished dumping " + d.getDumpSize()
                    + " accounts from " + d.getAmountOfProfiles()
                    + " profiles to " + d.getDumpLocation()
                    : d.getDumpLocation()
                            + " file already exists. Please rename or remove it before trying again.";
        } catch (DatabaseException | IOException
                | UnsupportedOperatingSystemException e) {
            e.printStackTrace();
            message = e.getMessage();
        }
        if (!silent) {
            JOptionPane.showMessageDialog(null, message);
        }
    }
}
