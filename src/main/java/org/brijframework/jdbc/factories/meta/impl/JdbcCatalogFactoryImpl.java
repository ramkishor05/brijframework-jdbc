package org.brijframework.jdbc.factories.meta.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.factories.meta.JdbcCatalogFactory;
import org.brijframework.jdbc.factories.source.impl.JdbcSourceFactoryImpl;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcSourceFactoryImpl.class)
public class JdbcCatalogFactoryImpl extends AbstractFactory<String,JdbcCatalog> implements JdbcCatalogFactory {


	protected JdbcCatalogFactoryImpl() {
	}

	protected static JdbcCatalogFactoryImpl factory;

	@Assignable
	public static JdbcCatalogFactoryImpl getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogFactoryImpl();
		}
		return factory;
	}
	
	@Override
	public JdbcCatalogFactoryImpl loadFactory() {
		JdbcSourceFactoryImpl.getFactory().getCache().forEach((key,datasource)->{
			register(key,datasource);
		});
		return this;
	}

	private void register(String key, JdbcSource datasource) {
		try {
			Connection connection=datasource.getConnection();
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("tableCat" , connection.getCatalog());
			map.put("sourceCat" , datasource.getId());
			map.put("source", datasource);
			register(key, map);
		} catch (Exception e) {
			e.printStackTrace();
			
		}
	}

	private void register(String key, Map<String, Object> map) {
		JdbcCatalog catalog=InstanceUtil.getInstance(JdbcCatalog.class, map);
		catalog.setId(key+"."+catalog.getTableCat());
		register(key+"."+catalog.getTableCat(), catalog);
	}

	@Override
	protected void preregister(String key, JdbcCatalog catalog) {
		System.err.println("Jdbc Catalog : "+key);
	}

	@Override
	protected void postregister(String key, JdbcCatalog catalog) {
		JdbcSource source=JdbcSourceFactoryImpl.getFactory().getJdbcSource(catalog.getSourceCat());
		if(source!=null) {
			catalog.setSource(source);
		}
	}

	public JdbcCatalog getJbdcCatalog(String key) {
		return getCache().get(key);
	}
}
