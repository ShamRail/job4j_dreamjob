package ru.job4j.dreamjob.store.db;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.dreamjob.config.AppConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static final BasicDataSource CONNECTION_POOL = new BasicDataSource();

    private static final ConnectionPool POOL = new ConnectionPool();

    public static ConnectionPool getPool() {
        return POOL;
    }

    private ConnectionPool() {
        AppConfig cfg = AppConfig.getConfig();
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        CONNECTION_POOL.setDriverClassName(cfg.getProperty("jdbc.driver"));
        CONNECTION_POOL.setUrl(cfg.getProperty("jdbc.url"));
        CONNECTION_POOL.setUsername(cfg.getProperty("jdbc.username"));
        CONNECTION_POOL.setPassword(cfg.getProperty("jdbc.password"));
        CONNECTION_POOL.setMinIdle(5);
        CONNECTION_POOL.setMaxIdle(10);
        CONNECTION_POOL.setMaxOpenPreparedStatements(100);
    }

    public Connection getConnection() throws SQLException {
        return CONNECTION_POOL.getConnection();
    }

}
