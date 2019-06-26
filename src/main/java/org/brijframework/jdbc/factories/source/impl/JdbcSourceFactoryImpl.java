package org.brijframework.jdbc.factories.source.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.bean.BeanInfo;
import org.brijframework.bean.factories.impl.BeanInfoFactoryImpl;
import org.brijframework.jdbc.factories.source.JdbcSourceFactory;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.model.factories.ModelFactory;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.asserts.Assertion;

public class JdbcSourceFactoryImpl extends AbstractFactory<String,JdbcSource> implements JdbcSourceFactory{
	
	private static JdbcSourceFactoryImpl factory;
	
	@Assignable
	public static JdbcSourceFactoryImpl getFactory() {
		if(factory==null) {
			factory=new JdbcSourceFactoryImpl();
		}
		return factory;
	}

	@Override
	public JdbcSource getJdbcSource(String jdbcSource) {
		return getCache().get(jdbcSource);
	}
	
	@Override
	public Connection getConnection(String datasource) throws SQLException {
		JdbcSource source=getJdbcSource(datasource);
		Assertion.notNull(source, "Source not found for given id :"+datasource);
		Assertion.notNull(source.getDataSource(), "Data source not found for given id :"+datasource);
		return source.getDataSource().getConnection();
	}

	@Override
	public JdbcSourceFactoryImpl loadFactory() {
		List<BeanInfo> owners= BeanInfoFactoryImpl.getFactory().getBeanInfoList(DataSource.class);
		for(BeanInfo owner:owners) {
			DataSource dataSource=ModelFactory.getFactory().getModel(owner);
			if(dataSource!=null) {
				register(owner.getId(),owner, dataSource);
			}	
		}
		return this;
	}

	public void register(String id, BeanInfo owner, DataSource dataSource) {
		JdbcSource source=new JdbcSource();
		source.setId(id);
		source.setDataSource(dataSource);
		register(id, source);
	}

	@Override
	protected void preregister(String key, JdbcSource value) {
		System.err.println("Jdbc Source  : "+key);
	}

	@Override
	protected void postregister(String key, JdbcSource value) {
		
	}

}
