package com.future.rocket.distributelock.db.lock;

import com.future.rocket.distributelock.db.config.ConfigLoader;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DbDistributedLock {

    private static final String DB_URL = ConfigLoader.getProperty("db.url");
    private static final String DB_USER = ConfigLoader.getProperty("db.user");
    private static final String DB_PASSWORD = ConfigLoader.getProperty("db.password");

    private final String lockName;
    private final String lockOwner;
    private final Connection connection;

    public DbDistributedLock(String lockName, String lockOwner) throws SQLException {
        this.lockName = lockName;
        this.lockOwner = lockOwner;
        this.connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean acquireLock(int timeoutSeconds) throws SQLException {
        Instant expiresAt = Instant.now().plus(timeoutSeconds, ChronoUnit.SECONDS);
        try (PreparedStatement insertStmt = connection.prepareStatement(
                "INSERT INTO distributed_lock (lock_name, lock_owner, expires_at) VALUES (?, ?, ?)"
        )) {
            insertStmt.setString(1, lockName);
            insertStmt.setString(2, lockOwner);
            insertStmt.setTimestamp(3, Timestamp.from(expiresAt));
            return insertStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            try (PreparedStatement updateStmt = connection.prepareStatement(
                    "UPDATE distributed_lock SET lock_owner = ?, expires_at = ? WHERE lock_name = ? AND expires_at < NOW()"
            )) {
                updateStmt.setString(1, lockOwner);
                updateStmt.setTimestamp(2, Timestamp.from(expiresAt));
                updateStmt.setString(3, lockName);
                return updateStmt.executeUpdate() > 0;
            }
        }
    }

    public boolean releaseLock() throws SQLException {
        try (PreparedStatement deleteStmt = connection.prepareStatement(
                "DELETE FROM distributed_lock WHERE lock_name = ? AND lock_owner = ?"
        )) {
            deleteStmt.setString(1, lockName);
            deleteStmt.setString(2, lockOwner);
            return deleteStmt.executeUpdate() > 0;
        }
    }

    public void close() throws SQLException {
        connection.close();
    }

}
