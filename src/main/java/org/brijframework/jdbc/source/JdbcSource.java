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
	
	private JdbcSourceFactory factory;

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
	public JdbcSourceFactory getFactory() {
		return factory;
	}
	
	public void setFactory(JdbcSourceFactory factory) {
		this.factory = factory;
	}
}
