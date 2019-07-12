package org.brijframework.jdbc.template;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Constants;

public class JdbcUpdate extends JdbcQualifier<JdbcUpdate>{

	private JdbcCatalog catalog;
	private StringBuilder statement=new StringBuilder();
	private Map<String, Object> parameter=new HashMap<>();
	private JdbcTable table;
	
	public JdbcUpdate(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
		this.table=this.getCatalog().getTables().get(table);
		Assertion.notNull(this.table, "Table not found for id: "+table+" in  catalog : "+this.catalog.getSourceCat());
		this.init();
	} 
	
	public void init() {
		this.statement.append("UPDATE "+table.getTableName()+" SET ");
	}
	
	public JdbcTable getTable() {
		return this.table;
	}
	
	public JdbcUpdate setProperty(String key,Object value) {
		JdbcColumn column = getTable().getColumn(key);
		Assertion.notNull(column, "Column not found for key  : "+key);
		this.parameter.put(key, value);
		return this;
	}
	
	public JdbcUpdate setProperties(Map<String, Object> properties) {
		for (Entry<String, Object> entry : properties.entrySet()) {
			setProperty(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public JdbcUpdate setProperties(String keys,Object...values) {
		String[] keyArray=keys.split(Constants.SPLIT_DOT);
		for (int index = 0; index < keyArray.length; index++) {
			setProperty(keyArray[index], values[index]);
		}
		return this;
	}
	
	public String generatedQuery() {
		AtomicInteger count=new AtomicInteger(parameter.size());
		parameter.forEach((key,value)->{
			statement.append(key+"="+value);
			if(count.decrementAndGet()>0) {
				statement.append(" , ");
			}
		});
		statement.append(getQualifier());
		return statement.toString();
	}
	
	public boolean result() {
		try {
			return table.executeUpdate(generatedQuery());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public JdbcCatalog getCatalog() {
		return this.catalog;
	}
}