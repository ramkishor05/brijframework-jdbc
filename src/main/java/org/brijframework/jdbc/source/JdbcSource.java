package org.brijframework.jdbc.source;

import java.sql.Connection;

import javax.sql.DataSource;

import org.brijframework.bean.BeanInfo;
import org.brijframework.jdbc.AbstractJdbc;
import org.brijframework.jdbc.factories.source.JdbcSourceFactory;

public class JdbcSource extends AbstractJdbc{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
	private Connection connection;
	private BeanInfo owner;
	
	private JdbcSourceFactory<String,JdbcSource> factory;

	public BeanInfo getOwner() {
		return owner;
	}
	
	public void setOwner(BeanInfo owner) {
		this.owner = owner;
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
	public JdbcSourceFactory<String,JdbcSource> getFactory() {
		return factory;
	}
	
	public void setFactory(JdbcSourceFactory<String,JdbcSource> factory) {
		this.factory = factory;
	}
}
