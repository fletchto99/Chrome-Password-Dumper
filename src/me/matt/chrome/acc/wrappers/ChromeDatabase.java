package me.matt.chrome.acc.wrappers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.matt.chrome.acc.exception.DatabaseConnectionException;
import me.matt.chrome.acc.exception.DatabaseReadException;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteConfig.TransactionMode;

public class ChromeDatabase {

    private Connection connection;
    private File database;

    private ChromeDatabase(Connection connection, File database) {
        this.connection = connection;
        this.database = database;
    }

    public static ChromeDatabase connect(File database)
            throws DatabaseConnectionException {

        File tempDB = new File(database.getAbsolutePath() + "_TEMP");
        try {
            if (!tempDB.exists()) {
                tempDB.createNewFile();
            }
            Files.copy(Paths.get(database.getPath()), new FileOutputStream(
                    tempDB));
        } catch (IOException e) {
            throw new DatabaseConnectionException(
                    "Error copying database! Has chrome updated?");
        }
        Connection db;
        try {
            SQLiteConfig config = new SQLiteConfig();
            config.setReadOnly(true);

            config.setTransactionMode(TransactionMode.EXCLUSIVE);
            db = config.createConnection("jdbc:sqlite:" + tempDB.getPath());
            db.setAutoCommit(true);
        } catch (SQLException e) {
            tempDB.delete();
            // TODO: Better handling of connection
            throw new DatabaseConnectionException(
                    "Error connecting to database! Has chrome updated?");
        }
        return new ChromeDatabase(db, tempDB);
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
                    password = results.getBytes("password_value"); // TODO: Null on mac??
                    accounts.add(new ChromeAccount(username, password, address));
                } catch (SQLException e) {
                    e.printStackTrace();
                    // TODO: Handle when error adding row from db to list of accounts
                }
            }
            results.close();
            results.getStatement().close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseReadException(
                    "Error reading database. Has chrome updated?");
        }
        return accounts;
    }

    public void close() {
        database.delete();
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }
}