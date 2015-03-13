package me.matt.chrome.acc.exception;

import java.io.IOException;

public class ChromeNotFoundException extends IOException {

    /**
     * 
     */
    private static final long serialVersionUID = -3586073044510230978L;

    public ChromeNotFoundException(String message) {
        super(message);
    }
}
