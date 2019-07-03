package org.brijframework.jdbc.template;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.asserts.Assertion;

public class JdbcUpdate{

	JdbcCatalog catalog;
	StringBuilder statement=new StringBuilder();
	Map<String, Object> parameter=new HashMap<>();
	
	public JdbcUpdate(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
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
	
	public boolean run() {
		try {
			return catalog.getSource().getConnection().createStatement().execute(query());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}