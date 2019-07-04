package org.brijframework.jdbc.template;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.asserts.Assertion;

public class JdbcFetch extends JdbcQualifier{
	JdbcCatalog catalog;
	JdbcTable table;
	StringBuilder statement=new StringBuilder("SELECT ");
	List<String> parameter=new ArrayList<>();
	
	public JdbcFetch(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
		this.table=this.catalog.getTables().get(table);
		Assertion.notNull(this.table, "Table not found. this is reqired");
	} 
	
	public JdbcFetch selected(String key) {
		this.parameter.add(key);
		return this;
	}
	
	public JdbcFetch all() {
		this.parameter.add(" * ");
		return this;
	}
	
	public String query() {
		AtomicInteger count=new AtomicInteger(parameter.size());
		parameter.forEach((key)->{
			statement.append(key);
			if(count.decrementAndGet()>0) {
				statement.append(" , ");
			}
		});
		statement.append(" FROM "+this.table.getTableName()+" ");
		statement.append(getQualifier());
		return statement.toString();
	}
	
	StringBuilder qual=new StringBuilder();
	
	public JdbcFetch where() {
		qual.append(" WHERE ");
		return this;
	}

	public JdbcFetch equalTo(String key,Object value) {
		qual.append(""+ key+ " = "+value);
		return this;
	}
	
	public JdbcFetch notEqualTo(String key,Object value) {
		qual.append(""+ key+ " != "+value);
		return this;
	}
	
	public JdbcFetch greaterThan(String key,Object value) {
		qual.append(""+ key+ " > "+value);
		return this;
	}
	
	public JdbcFetch notGreaterThan(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcFetch greaterThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcFetch lessThan(String key,Object value) {
		qual.append(""+ key+ " < "+value);
		return this;
	}
	
	public JdbcQualifier notLessThan(String key,Object value) {
		qual.append(""+ key+ " !< "+value);
		return this;
	}
	
	public JdbcFetch lessThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " <= "+value);
		return this;
	}

	public JdbcFetch and() {
		qual.append(" AND ");
		return this;
	}
	
	public JdbcFetch or() {
		qual.append(" OR ");
		return this;
	}
	
	public String getQualifier() {
		return this.qual.toString();
	}

	public Map<String,Object> unique() {
		try {
			ResultSet result=table.executeQuery(query());
			List<Map<String, Object>>  list=JdbcBeanUtil.getDataResultList(result);
			return list.iterator().next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> list() {
		try {
			ResultSet result=table.executeQuery(query());
			return JdbcBeanUtil.getDataResultList(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}