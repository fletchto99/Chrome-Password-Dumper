package me.matt.chrome.acc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import com.mcdermottroe.apple.OSXKeychain;
import com.mcdermottroe.apple.OSXKeychainException;
import com.sun.jna.platform.win32.Crypt32Util;

public class ChromeSecurity {

    public static String getWin32Password(byte[] encryptedData) {
        return new String(Crypt32Util.cryptUnprotectData(encryptedData));
    }

    public static String getOSXKeychainPasswordAdmin(String host) {
        try {
            /*
             * May require admin password for pc? Looking for workaround, but keychain seems pretty secure ATM
             * (perhaps I can spoof being google chrome and load their keychain auth? Is this something done
             * locally?
             */
            host = host.replace("https://", "").replace("http://", "");
            String command = "security find-internet-password -gs "
                    + host.substring(
                            0,
                            host.indexOf('/') > 0 ? host.indexOf('/') : host
                                    .length()) + " -w";
            System.out.println("Command: " + command);
            Process result = Runtime.getRuntime().exec(command);
            BufferedReader out = new BufferedReader(new InputStreamReader(
                    result.getInputStream()));
            String password = out.readLine();
            out.close();
            return password;
        } catch (IOException e) {
            return "";
        }
    }

    public static String getOSXKeychainPassword(String host, String username) {
        try {
            /*
             * May require admin password for pc? Looking for workaround, but keychain seems pretty secure ATM
             * (perhaps I can spoof being google chrome and load their keychain auth? Is this something done
             * locally?
             */
            return OSXKeychain.getInstance()
                    .findInternetPassword(
                            new URL(host.substring(0, host.indexOf(10, '/'))),
                            username);
        } catch (OSXKeychainException | MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}
