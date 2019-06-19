package org.brijframework.jdbc.factories.asm;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.brijframework.container.Container;
import org.brijframework.data.factories.asm.ClassDataInfoFactoryImpl;
import org.brijframework.data.info.ClassDataInfo;
import org.brijframework.jdbc.factories.JdbcFactory;
import org.brijframework.model.factories.ModelFactory;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.asserts.Assertion;

public class DataSourceJdbcFactory implements JdbcFactory{
	
	private static DataSourceJdbcFactory factory;
	
	@Assignable
	public static DataSourceJdbcFactory factory() {
		if(factory==null) {
			factory=new DataSourceJdbcFactory();
		}
		return factory;
	}

	private Container container;
	
	private ConcurrentHashMap<String, DataSource> cache=new ConcurrentHashMap<>();
	
	public DataSource getDataSource(String datasource) {
		return getCache().get(datasource);
	}
	
	public Connection getConnection(String datasource) throws SQLException {
		DataSource dataSource=getDataSource(datasource);
		Assertion.notNull(dataSource, "Data source not found for given id :"+datasource);
		return dataSource.getConnection();
	}

	@Override
	public DataSourceJdbcFactory loadFactory() {
		List<ClassDataInfo> owners= ClassDataInfoFactoryImpl.getFactory().getDataList(DataSource.class.getName());
		for(ClassDataInfo owner:owners) {
			DataSource dataSource=ModelFactory.getFactory().getModel(owner);
			if(dataSource!=null) {
				getCache().put(owner.getId(), dataSource);
			}	
		}
		return this;
	}

	@Override
	public Container getContainer() {
		return this.container;
	}

	@Override
	public void setContainer(Container container) {
		this.container=container;
	}

	@Override
	public ConcurrentHashMap<String, DataSource> getCache() {
		return cache;
	}

	@Override
	public DataSourceJdbcFactory clear() {
		this.getCache().clear();
		return this;
	}
}
