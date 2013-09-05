package com.wimove.content.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseDataSourceConnection;
import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Wrapped version of DBUnits DatabaseDataSourceConnection to enable Spring Transaction support.
 */
public class SpringDBUnitConnection extends DatabaseDataSourceConnection {

    private final DataSource dataSource;

    /**
     * @param dataSource
     * @throws SQLException
     */
    public SpringDBUnitConnection(DataSource dataSource) throws SQLException {
        super(dataSource);
        this.dataSource = dataSource;
    }

    /**
     * @see org.dbunit.database.IDatabaseConnection#getConnection()
     */
    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = DataSourceUtils.getConnection(dataSource);
        return new SpringConnection(dataSource, conn);
    }
}

