package org.brijframework.jdbc.template;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.util.asserts.Assertion;

public class JdbcUpdate{

	private JdbcCatalog catalog;
	private StringBuilder statement=new StringBuilder();
	private Map<String, Object> parameter=new HashMap<>();
	private JdbcTable table;
	
	public JdbcUpdate(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
		this.table=this.catalog.getTables().get(table);
		Assertion.notNull(this.table, "Table not found for id: "+table+" in  catalog : "+this.catalog.getSourceCat());
		this.statement.append("UPDATE "+table+" SET ");
	} 
	
	public JdbcUpdate set(String key,Object value) {
		this.parameter.put(key, value);
		return this;
	}
	
	public String query() {
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
	
	StringBuilder qual=new StringBuilder();
	
	public JdbcUpdate where() {
		qual.append(" WHERE ");
		return this;
	}

	public JdbcUpdate equalTo(String key,Object value) {
		qual.append(""+ key+ " = "+value);
		return this;
	}
	
	public JdbcUpdate notEqualTo(String key,Object value) {
		qual.append(""+ key+ " != "+value);
		return this;
	}
	
	public JdbcUpdate greaterThan(String key,Object value) {
		qual.append(""+ key+ " > "+value);
		return this;
	}
	
	public JdbcUpdate notGreaterThan(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcUpdate greaterThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcUpdate lessThan(String key,Object value) {
		qual.append(""+ key+ " < "+value);
		return this;
	}
	
	public JdbcUpdate notLessThan(String key,Object value) {
		qual.append(""+ key+ " !< "+value);
		return this;
	}
	
	public JdbcUpdate lessThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " <= "+value);
		return this;
	}

	public JdbcUpdate and() {
		qual.append(" AND ");
		return this;
	}
	
	public JdbcUpdate or() {
		qual.append(" OR ");
		return this;
	}
	
	public String getQualifier() {
		return this.qual.toString();
	}
	
	public boolean execute() {
		try {
			return table.executeUpdate(query());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}