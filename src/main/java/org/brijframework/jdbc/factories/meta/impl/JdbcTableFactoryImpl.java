package org.brijframework.jdbc.factories.meta.impl;

import java.sql.Connection;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcMeta;
import org.brijframework.jdbc.factories.meta.JdbcTableFactory;
import org.brijframework.jdbc.util.JdbcUtil;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcCatalogFactoryImpl.class)
public class JdbcTableFactoryImpl extends AbstractFactory<String,JdbcTable> implements JdbcTableFactory {

	protected JdbcTableFactoryImpl() {
	}

	protected static JdbcTableFactoryImpl factory;

	@Assignable
	public static JdbcTableFactoryImpl getFactory() {
		if (factory == null) {
			factory = new JdbcTableFactoryImpl();
		}
		return factory;
	}
	
	@Override
	public JdbcTableFactoryImpl loadFactory() {
		JdbcCatalogFactoryImpl.getFactory().getCache().forEach((catalogKey,catalog)->{
			register(catalog);
		});
		return this;
	}

	public void register(String catalogKey, JbdcCatalog catalog) {
		try {
			Connection connection=catalog.getSource().getConnection();
			for(Map<String, Object> map:JdbcUtil.getTablesList(connection, JdbcMeta.TABLE)) {
				register(catalogKey, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register(JbdcCatalog catalog) {
		try {
			Connection connection=catalog.getSource().getConnection();
			for(Map<String, Object> tableMap:JdbcUtil.getTablesList(connection, JdbcMeta.TABLE)) {
				register(catalog, tableMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register(String catalogKey, Map<String, Object> tableMap) {
		JbdcCatalog catalog=JdbcCatalogFactoryImpl.getFactory().getJbdcCatalog(catalogKey);
		register(catalog, tableMap);
	}
	
	public void register(JbdcCatalog catalog, Map<String, Object> tableMap) {
		JdbcTable jdbcTable=InstanceUtil.getInstance(JdbcTable.class, tableMap);
		jdbcTable.setCatalog(catalog);
		register(catalog.getTABLE_CAT()+"."+jdbcTable.getTABLE_NAME(), jdbcTable);
	}

	@Override
	protected void preregister(String key, JdbcTable jdbcTable) {
		System.err.println("Jdbc Table   : "+key);
	}

	@Override
	protected void postregister(String key, JdbcTable jdbcTable) {
		jdbcTable.getCatalog().getTables().put(jdbcTable.getTABLE_NAME(), jdbcTable);
	}

	@Override
	public JdbcTable getTable(String table_id) {
		return getCache().get(table_id);
	}

	@Override
	public JdbcTable getTable(String catalog,String table) {
		for(JdbcTable jdbcTable:getCache().values()) {
			if(jdbcTable.getCatalog()==null) {
				continue;
			}
			if(catalog.equals(jdbcTable.getTABLE_CAT()) && table.equals(jdbcTable.getTABLE_NAME())) {
				return jdbcTable;
			}
		}
		return null;
	}

	@Override
	public JdbcTable getTable(String source, String catalog, String table) {
		for(JdbcTable jdbcTable:getCache().values()) {
			if(jdbcTable.getCatalog()==null) {
				continue;
			}
			if(jdbcTable.getCatalog().getSource()==null) {
				continue;
			}
			if(catalog.equals(jdbcTable.getTABLE_CAT()) && table.equals(jdbcTable.getTABLE_NAME())) {
				return jdbcTable;
			}
		}
		return null;
	}
	
}
