package org.brijframework.jdbc.source;

import java.sql.Connection;

import javax.sql.DataSource;

import org.brijframework.jdbc.AbstractJdbc;
import org.brijframework.jdbc.factories.source.JdbcSourceFactory;

public class JdbcSource extends AbstractJdbc{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private DataSource dataSource;
	private Connection connection;
	private JdbcSourceFactory factory;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Connection getConnection() throws Exception {
		if(connection==null) {
			connection=dataSource.getConnection();
		}
		if(connection.isClosed()) {
			connection=dataSource.getConnection();
		}
		return connection;
	}

	@Override
	public JdbcSourceFactory getFactory() {
		return factory;
	}
	
	public void setFactory(JdbcSourceFactory factory) {
		this.factory = factory;
	}
}
