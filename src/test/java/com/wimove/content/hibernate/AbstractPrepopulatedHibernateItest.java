package com.wimove.content.hibernate;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-persistenceContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class AbstractPrepopulatedHibernateItest extends
		AbstractTransactionalDataSourceSpringContextTests {
	// private static final String[] DATABASE_INIT_DATA = new String[] {
	// "wimove-sampleData.xml" };

	private static final String[] DATABASE_INIT_DATA = new String[] { "wimove-sampleData.xml" };

	private final DatabaseOperation CLEAN_INSERT = DatabaseOperation.CLEAN_INSERT;
	private final DatabaseOperation DELETE = DatabaseOperation.DELETE;

	@Autowired
	DataSource dataSource;

	@Before
	public void setUpTestDataWithinTransaction() throws DataSetException,
			DatabaseUnitException, SQLException, IOException {
		//performDbOperation(CLEAN_INSERT);
	}

	@After
	public void tearDownWithinTransaction() throws Exception {
		
		super.onTearDown();
	}

	/**
	 * A basic operation against the Db performed by DBUNIT. The
	 * {@link DataSource} is provided by Spring. The data are in the XML FLat
	 * data file.
	 * 
	 * @param databaseOperation
	 * @throws SQLException
	 * @throws DatabaseUnitException
	 * @throws IOException
	 * @throws DataSetException
	 */
	private void performDbOperation(DatabaseOperation databaseOperation)
			throws SQLException, DatabaseUnitException, IOException,
			DataSetException {

		IDatabaseConnection connection = new DatabaseDataSourceConnection(
				dataSource);

		for (String filename : DATABASE_INIT_DATA) {
			/*
			 * Refresh the contents of each dataset. A CLEAN_INSTALL is not
			 * performed as this would mean that two datasets could not insert
			 * data into the same table (the table would be dropped when the
			 * second set is loaded). //
			 */
			FlatXmlDataSet dataSet = new FlatXmlDataSet(new ClassPathResource(
					filename).getFile());

			// ReplacementDataSet replacementDataSet = new ReplacementDataSet(
			// dataSet );
			// replacementDataSet.addReplacementObject( "[point(13.34,33.53)]",
			// point(13.34,33.53) );
			//			
			// QueryDataSet dataSet = new FlatXmlDataSet(
			// new ClassPathResource(filename).getFile());

			// dataSet.("[NULL]", null);
			databaseOperation.execute(connection, dataSet);
		}
	}

	public static class SpringDBUnitConnection extends
			DatabaseDataSourceConnection {

		private final DataSource dataSource;

		/**
		 * @param dataSource
		 * @throws SQLException
		 */
		public SpringDBUnitConnection(DataSource dataSource)
				throws SQLException {
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
}
