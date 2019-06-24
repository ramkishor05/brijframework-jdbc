package org.brijframework.jdbc.factories.meta.impl;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcSource;
import org.brijframework.jdbc.factories.meta.JdbcCatalogFactory;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcSourceFactoryImpl.class)
public class JdbcCatalogFactoryImpl extends AbstractFactory<String,JbdcCatalog> implements JdbcCatalogFactory {


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
			map.put("TABLE_CAT" , connection.getCatalog());
			map.put("SOURCE_CAT" , datasource.getId());
			map.put("source", datasource);
			register(key, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void register(String key, Map<String, Object> map) {
		JbdcCatalog catalog=InstanceUtil.getInstance(JbdcCatalog.class, map);
		register(key+"."+catalog.getTABLE_CAT(), catalog);
	}

	@Override
	protected void preregister(String key, JbdcCatalog catalog) {
		System.err.println("Jdbc Catalog : "+key);
	}

	@Override
	protected void postregister(String key, JbdcCatalog catalog) {
		JdbcSource source=JdbcSourceFactoryImpl.getFactory().getJdbcSource(catalog.getSOURCE_CAT());
		if(source!=null) {
			source.setCatalog(catalog);
			catalog.setSource(source);
		}
	}

	public JbdcCatalog getJbdcCatalog(String key) {
		return getCache().get(key);
	}
}
