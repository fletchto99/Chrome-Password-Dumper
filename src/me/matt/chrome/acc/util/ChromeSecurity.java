package me.matt.chrome.acc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.sun.jna.platform.win32.Crypt32Util;

public class ChromeSecurity {

    public static String getWin32Password(byte[] encryptedData) {
        return new String(Crypt32Util.cryptUnprotectData(encryptedData));
    }

    public static String getOSXKeychainPasswordAsAdmin(String host) {
        try {
            /*
             * May require admin password for pc? Looking for workaround, but keychain seems pretty secure ATM
             * (perhaps I can spoof being google chrome and load their keychain auth? Is this something done
             * locally?
             * 
             * Does linux behave this way too?
             */
            host = host.replace("https://", "").replace("http://", "");
            String command = "security find-internet-password -gs "
                    + host.substring(
                            0,
                            host.indexOf('/') > 0 ? host.indexOf('/') : host
                                    .length()) + " -w";
            Process result = Runtime.getRuntime().exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    result.getInputStream()));
            String password = in.readLine();
            in.close();
            return password != null ? password : "";
        } catch (IOException e) {
            return "";
        }
    }
}
