package com.wimove.content.hibernate;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.DataSourceUtils;

/**
 * Wrapped Connection enable Spring Transaction support.
 */
// XXX: Do we really need this class *and* SpringConnection? Delphi seems to work without both.
public class SpringConnection implements Connection {

    private final DataSource dataSource;
    private final Connection conn;

    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Properties getClientInfo() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getClientInfo(String name) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isValid(int timeout) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public SQLXML createSQLXML() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NClob createNClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Blob createBlob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Clob createClob() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    
 
    
   
    
    /**
     * @param dataSource
     * @param conn
     * @throws SQLException
     */
    public SpringConnection(DataSource dataSource, Connection conn) {
        this.dataSource = dataSource;
        this.conn = conn;
    }

    /**
     * calls DataSourceUtils.closeConnectionIfNecessary rather than directly closing the connection
     * @throws java.sql.SQLException
     */
    public void close() throws SQLException {
        DataSourceUtils.doReleaseConnection(conn, dataSource);
    }

    /**
     * @throws java.sql.SQLException
     */
    public void clearWarnings() throws SQLException {
        conn.clearWarnings();
    }

    /**
     * @throws java.sql.SQLException
     */
    public void commit() throws SQLException {
        //conn.commit();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public Statement createStatement() throws SQLException {
        return conn.createStatement();
    }

    /**
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws java.sql.SQLException
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws java.sql.SQLException
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
        throws SQLException {
        return conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        return conn.equals(obj);
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean getAutoCommit() throws SQLException {
        return conn.getAutoCommit();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public String getCatalog() throws SQLException {
        return conn.getCatalog();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getHoldability() throws SQLException {
        return conn.getHoldability();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public DatabaseMetaData getMetaData() throws SQLException {
        return conn.getMetaData();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public int getTransactionIsolation() throws SQLException {
        return conn.getTransactionIsolation();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("unchecked")
	public Map getTypeMap() throws SQLException {
        return conn.getTypeMap();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public SQLWarning getWarnings() throws SQLException {
        return conn.getWarnings();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return conn.hashCode();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isClosed() throws SQLException {
        return conn.isClosed();
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public boolean isReadOnly() throws SQLException {
        return conn.isReadOnly();
    }

    /**
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public String nativeSQL(String sql) throws SQLException {
        return conn.nativeSQL(sql);
    }

    /**
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public CallableStatement prepareCall(String sql) throws SQLException {
        return conn.prepareCall(sql);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws java.sql.SQLException
     */
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws java.sql.SQLException
     */
    public CallableStatement prepareCall(
        String sql,
        int resultSetType,
        int resultSetConcurrency,
        int resultSetHoldability)
        throws SQLException {
        return conn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @param sql
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return conn.prepareStatement(sql);
    }

    /**
     * @param sql
     * @param autoGeneratedKeys
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return conn.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
        throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(
        String sql,
        int resultSetType,
        int resultSetConcurrency,
        int resultSetHoldability)
        throws SQLException {
        return conn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @param sql
     * @param columnIndexes
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return conn.prepareStatement(sql, columnIndexes);
    }

    /**
     * @param sql
     * @param columnNames
     * @return
     * @throws java.sql.SQLException
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return conn.prepareStatement(sql, columnNames);
    }

    /**
     * @param savepoint
     * @throws java.sql.SQLException
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        conn.releaseSavepoint(savepoint);
    }

    /**
     * @throws java.sql.SQLException
     */
    public void rollback() throws SQLException {
        conn.rollback();
    }

    /**
     * @param savepoint
     * @throws java.sql.SQLException
     */
    public void rollback(Savepoint savepoint) throws SQLException {
        //conn.rollback(savepoint);
    }

    /**
     * @param autoCommit
     * @throws java.sql.SQLException
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        conn.setAutoCommit(autoCommit);
    }

    /**
     * @param catalog
     * @throws java.sql.SQLException
     */
    public void setCatalog(String catalog) throws SQLException {
        conn.setCatalog(catalog);
    }

    /**
     * @param holdability
     * @throws java.sql.SQLException
     */
    public void setHoldability(int holdability) throws SQLException {
        conn.setHoldability(holdability);
    }

    /**
     * @param readOnly
     * @throws java.sql.SQLException
     */
    public void setReadOnly(boolean readOnly) throws SQLException {
        conn.setReadOnly(readOnly);
    }

    /**
     * @return
     * @throws java.sql.SQLException
     */
    public Savepoint setSavepoint() throws SQLException {
        return conn.setSavepoint();
    }

    /**
     * @param name
     * @return
     * @throws java.sql.SQLException
     */
    public Savepoint setSavepoint(String name) throws SQLException {
        return conn.setSavepoint(name);
    }

    /**
     * @param level
     * @throws java.sql.SQLException
     */
    public void setTransactionIsolation(int level) throws SQLException {
        conn.setTransactionIsolation(level);
    }

 

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return conn.toString();
    }

	@Override
	public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSchema(String schema) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSchema() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void abort(Executor executor) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setNetworkTimeout(Executor executor, int milliseconds)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getNetworkTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}