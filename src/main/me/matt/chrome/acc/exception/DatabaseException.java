package me.matt.chrome.acc.exception;

import java.sql.SQLException;

public class DatabaseException extends SQLException {

    /**
     *
     */
    private static final long serialVersionUID = 1925456190643862568L;

    DatabaseException(final String message) {
        super(message);
    }

}
