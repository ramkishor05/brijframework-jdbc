package org.brijframework.jdbc.factories.model.impl;

import java.sql.Connection;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcPrimary;
import org.brijframework.jdbc.JdbcReferance;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcMeta;
import org.brijframework.jdbc.factories.model.JdbcTableMetaFactory;
import org.brijframework.jdbc.util.JdbcModelUtil;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcCatalogModelFactoryImpl.class)
public class JdbcTableFactoryImpl extends AbstractFactory<String,JdbcTable> implements JdbcTableMetaFactory {

	protected JdbcTableFactoryImpl() {
	}

	protected static JdbcTableFactoryImpl factory;

	public static JdbcTableFactoryImpl getFactory() {
		if (factory == null) {
			factory = new JdbcTableFactoryImpl();
		}
		return factory;
	}
	
	@Override
	public JdbcTableFactoryImpl loadFactory() {
		JdbcCatalogModelFactoryImpl.getFactory().getCache().forEach((catalogKey,catalog)->{
			register(catalog);
		});
		return this;
	}

	public void register(String catalogKey, JdbcCatalog catalog) {
		try {
			Connection connection=catalog.getSource().getConnection();
			for(Map<String, Object> map:JdbcModelUtil.getTablesList(connection, JdbcMeta.TABLE)) {
				register(catalogKey, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register(JdbcCatalog catalog) {
		try {
			Connection connection=catalog.getSource().getConnection();
			for(Map<String, Object> tableMap:JdbcModelUtil.getTablesList(connection, JdbcMeta.TABLE)) {
				register(catalog, tableMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register(String catalogKey, Map<String, Object> tableMap) {
		JdbcCatalog catalog=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(catalogKey);
		register(catalog, tableMap);
	}
	
	public void register(JdbcCatalog catalog, Map<String, Object> tableMap) {
		JdbcTable jdbcTable=InstanceUtil.getInstance(JdbcTable.class, tableMap);
		String id=catalog.getTableCat()+"."+jdbcTable.getTableName();
		jdbcTable.setCatalog(catalog);
		jdbcTable.setId(id);
		try {
			for(Map<String, Object> colMap:JdbcModelUtil.getColumnList(catalog.getSource().getConnection(),catalog.getTableCat(), jdbcTable.getTableName())) {
				JdbcColumn jdbcTableColumn=InstanceUtil.getInstance(JdbcColumn.class, colMap);
				jdbcTableColumn.setJdbcTable(jdbcTable);
				jdbcTableColumn.setId(jdbcTableColumn.getColumnName());
				jdbcTable.getColumns().put(jdbcTableColumn.getColumnName(), jdbcTableColumn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for(Map<String, Object> tabRefMap:JdbcModelUtil.getExportedKeys(catalog.getSource().getConnection(),catalog.getTableCat(), jdbcTable.getTableName())) {
				JdbcReferance jdbcRabRef=InstanceUtil.getInstance(JdbcReferance.class, tabRefMap);
				jdbcRabRef.setId(jdbcRabRef.getPkcolumnName());
				jdbcTable.getExportedKeys().put(jdbcRabRef.getPkcolumnName(), jdbcRabRef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for(Map<String, Object> tabRefMap:JdbcModelUtil.getImportedKeys(catalog.getSource().getConnection(),catalog.getTableCat(), jdbcTable.getTableName())) {
				JdbcReferance jdbcRabRef=InstanceUtil.getInstance(JdbcReferance.class, tabRefMap);
				jdbcRabRef.setId(jdbcRabRef.getPkcolumnName());
				jdbcTable.getImportedKeys().put(jdbcRabRef.getPkcolumnName(), jdbcRabRef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for(Map<String, Object> tabRefMap:JdbcModelUtil.getPrimaryKeys(catalog.getSource().getConnection(),catalog.getTableCat(), jdbcTable.getTableName())) {
				JdbcPrimary jdbcPrimary=InstanceUtil.getInstance(JdbcPrimary.class, tabRefMap);
				jdbcPrimary.setId(jdbcPrimary.getColumnName());
				jdbcTable.getPrimaryKeys().put(jdbcPrimary.getColumnName(), jdbcPrimary);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		register(catalog.getTableCat()+"."+jdbcTable.getTableName(), jdbcTable);
	}

	@Override
	protected void preregister(String key, JdbcTable jdbcTable) {
		System.err.println("Jdbc Table   : "+key);
	}

	@Override
	protected void postregister(String key, JdbcTable jdbcTable) {
		jdbcTable.getCatalog().getTables().put(jdbcTable.getTableName(), jdbcTable);
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
			if(catalog.equals(jdbcTable.getTableCat()) && table.equals(jdbcTable.getTableName())) {
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
			if(catalog.equals(jdbcTable.getTableCat()) && table.equals(jdbcTable.getTableName())) {
				return jdbcTable;
			}
		}
		return null;
	}
	
}
