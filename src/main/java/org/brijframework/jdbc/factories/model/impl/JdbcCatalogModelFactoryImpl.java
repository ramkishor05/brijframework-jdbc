package org.brijframework.jdbc.factories.model.impl;

import java.sql.Connection;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcIndex;
import org.brijframework.jdbc.JdbcPrimary;
import org.brijframework.jdbc.JdbcReferance;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcMeta;
import org.brijframework.jdbc.factories.meta.JdbcCatalogMetaFactory;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.jdbc.util.JdbcModelUtil;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcSourceFactoryImpl.class)
public class JdbcCatalogModelFactoryImpl extends AbstractFactory<String,JdbcCatalog> implements JdbcCatalogMetaFactory {


	protected JdbcCatalogModelFactoryImpl() {
	}

	protected static JdbcCatalogModelFactoryImpl factory;

	@Assignable
	public static JdbcCatalogModelFactoryImpl getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogModelFactoryImpl();
		}
		return factory;
	}
	
	@Override
	public JdbcCatalogModelFactoryImpl loadFactory() {
		JdbcSourceFactoryImpl.getFactory().getCache().forEach((key,datasource)->{
			register(datasource);
		});
		return this;
	}

	private void register(JdbcSource source) {
		try {
			Connection connection=source.getConnection();
			JdbcCatalog catalog=InstanceUtil.getInstance(JdbcCatalog.class);
			catalog.setId(connection.getCatalog());
			catalog.setTableCat(connection.getCatalog());
			catalog.setSourceCat(source.getId());
			catalog.setSource(source);
			register(catalog.getTableCat(), catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JdbcTable getJdbcTable(JdbcCatalog catalog, Map<String, Object> tableMap) {
		JdbcTable jdbcTable=InstanceUtil.getInstance(JdbcTable.class, tableMap);
		jdbcTable.setCatalog(catalog);
		jdbcTable.setId(jdbcTable.getTableName());
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
				JdbcPrimary jdbcRabRef=InstanceUtil.getInstance(JdbcPrimary.class, tabRefMap);
				jdbcRabRef.setId(jdbcRabRef.getColumnName());
				jdbcTable.getPrimaryKeys().put(jdbcRabRef.getColumnName(), jdbcRabRef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			for(Map<String, Object> tabRefMap:JdbcModelUtil.getIndexes(catalog.getSource().getConnection(),catalog.getTableCat(), jdbcTable.getTableName())) {
				JdbcIndex jdbcRabRef=InstanceUtil.getInstance(JdbcIndex.class, tabRefMap);
				jdbcRabRef.setId(jdbcRabRef.getColumnName());
				jdbcTable.getIndexedKeys().put(jdbcRabRef.getColumnName(), jdbcRabRef);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jdbcTable;
	}


	@Override
	protected void preregister(String key, JdbcCatalog catalog) {
		System.err.println("Jdbc catalog : "+key);
	}

	@Override
	protected void postregister(String key, JdbcCatalog catalog) {
		try {
			for(Map<String, Object> tableMap:JdbcModelUtil.getTablesList(catalog.getSource().getConnection(),catalog.getTableCat(), JdbcMeta.TABLE)) {
				JdbcTable table= getJdbcTable(catalog, tableMap);
				catalog.getTables().put(table.getTableName(), table);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JdbcCatalog getJbdcCatalog(String key) {
		return getCache().get(key);
	}
}
