package org.brijframework.jdbc.factories.meta.impl;

import java.sql.Connection;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.factories.meta.JdbcColumnFactory;
import org.brijframework.jdbc.util.JdbcUtil;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcTableFactoryImpl.class)
public class JdbcColumnFactoryImpl extends AbstractFactory<String,JdbcColumn> implements JdbcColumnFactory{

	protected JdbcColumnFactoryImpl() {
	}

	protected static JdbcColumnFactoryImpl factory;

	@Assignable
	public static JdbcColumnFactoryImpl getFactory() {
		if (factory == null) {
			factory = new JdbcColumnFactoryImpl();
		}
		return factory;
	}
	
	@Override
	public JdbcColumnFactoryImpl loadFactory() {
		JdbcTableFactoryImpl.getFactory().getCache().forEach((tableKey,table)->{
			register(tableKey,table);
		});
		return this;
	}

	public void register(String tableKey, JdbcTable table) {
		try {
			Connection connection=table.getCatalog().getSource().getConnection();
			for(Map<String, Object> map:JdbcUtil.getColumnList(connection, table.getTableName())) {
				register(tableKey,table, map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void register(String tableKey,JdbcTable jdbcTable, Map<String, Object> map) {
		JdbcColumn jdbcTableColumn=InstanceUtil.getInstance(JdbcColumn.class, map);
		jdbcTableColumn.setJdbcTable(jdbcTable);
		jdbcTableColumn.setId(tableKey+"."+jdbcTableColumn.getColumnName());
		register(tableKey+"."+jdbcTableColumn.getColumnName(), jdbcTableColumn);
	}

	@Override
	protected void preregister(String key, JdbcColumn jdbcTable) {
		System.err.println("Jdbc Column  : "+key);
	}

	@Override
	protected void postregister(String key, JdbcColumn jdbcTableColumn) {
		jdbcTableColumn.getJdbcTable().getColumns().put(jdbcTableColumn.getColumnName(), jdbcTableColumn);
	}

	public JdbcColumn getColumn(String id) {
		return getCache().get(id);
	}
}
