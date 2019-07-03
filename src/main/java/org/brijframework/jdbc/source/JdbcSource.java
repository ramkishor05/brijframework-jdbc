package org.brijframework.jdbc.source;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.brijframework.jdbc.AbstractJdbc;

public class JdbcSource extends AbstractJdbc{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
	private Connection connection;
	private Map<String,Object> beanSource;
	
	public void setBeanSource(Map<String, Object> beanSource) {
		this.beanSource = beanSource;
	}
	
	public Map<String, Object> getBeanSource() {
		return beanSource;
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
}
