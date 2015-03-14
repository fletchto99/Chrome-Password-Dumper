package me.matt.chrome.acc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.jna.platform.win32.Crypt32Util;

public class ChromeSecurity {

    public static String getWin32Password(byte[] encryptedData) {
        return new String(Crypt32Util.cryptUnprotectData(encryptedData));
    }

    public static String getOSXKeychainPassword(String host) {
        try {
            /*
             * May require admin password for pc? Looking for workaround, but keychain seems pretty secure ATM
             * (perhaps I can spoof being google chrome and load their keychain auth? Is this something done
             * locally?
             */
            String command = "security find-internet-password -gs "
                    + host.substring(0, host.indexOf('/'));
            Process result = Runtime.getRuntime().exec(command);
            BufferedReader out = new BufferedReader(new InputStreamReader(
                    result.getInputStream()));
            String password = out.readLine();
            System.out.println("pwd: " + password);
            return password;
        } catch (IOException e) {
            return "";
        }
    }
}
