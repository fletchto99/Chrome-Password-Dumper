package me.matt.chrome.acc.wrappers;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.matt.chrome.acc.exception.DatabaseConnectionException;
import me.matt.chrome.acc.exception.DatabaseReadException;

import org.sqlite.SQLiteConfig;

public class ChromeDatabase {

    private Connection connection;

    private ChromeDatabase(Connection connection) {
        this.connection = connection;
    }

    public static ChromeDatabase connect(File database)
            throws DatabaseConnectionException {
        Connection db;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(true);
            db = config.createConnection("jdbc:sqlite:" + database.getPath());
        } catch (SQLException e) {
            // TODO: Better handling of connection
            throw new DatabaseConnectionException(
                    "Error connecting to database! Has chrome updated?");
        }
        return new ChromeDatabase(db);
    }

    public ArrayList<ChromeAccount> selectAccounts()
            throws DatabaseConnectionException, DatabaseReadException {
        try {
            if (connection.isClosed()) {
                throw new DatabaseConnectionException(
                        "Connection to database has been terminated.");
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException(
                    "Connection status to the database could not be determined. Has chrome updated?");
        }
        ArrayList<ChromeAccount> accounts = new ArrayList<ChromeAccount>();
        try {
            ResultSet results = connection
                    .createStatement()
                    .executeQuery(
                            "SELECT action_url, username_value, password_value FROM logins");
            while (results.next()) {
                String address, username;
                byte[] password;
                try {
                    address = results.getString("action_url");
                    username = results.getString("username_value");
                    password = results.getBytes("password_value");
                    accounts.add(new ChromeAccount(username, password, address));
                } catch (SQLException e) {
                    // TODO: Handle when error adding row from db to list of accounts
                }
            }
            results.close();
            results.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseReadException(
                    "Error reading database. Has chrome updated?"
                            + e.getMessage());
        }
        return accounts;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }
}