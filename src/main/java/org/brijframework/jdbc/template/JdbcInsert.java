package org.brijframework.jdbc.template;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.util.JdbcMapper;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.support.Constants;

public class JdbcInsert extends JdbcQualifier<JdbcInsert>{

	private JdbcCatalog catalog;
	private StringBuilder statement=new StringBuilder();
	private Map<String, Object> parameter=new LinkedHashMap<>();
	private JdbcTable table;
	private JdbcFetch fetch;
	
	public JdbcInsert(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
		this.table=this.getCatalog().getTables().get(table);
		Assertion.notNull(this.table, "Table not found for id: "+table+" in  catalog : "+this.catalog.getSourceCat());
		this.init();
	} 
	
	public void init() {
		
	}
	
	public JdbcTable getTable() {
		return this.table;
	}
	
	public JdbcInsert setProperty(String key,Object value) {
		JdbcColumn column = getTable().getColumn(key);
		Assertion.notNull(column, "Column not found for key  : "+key);
		this.parameter.put(key, value);
		return this;
	}
	
	public JdbcInsert setProperties(Map<String, Object> properties) {
		for (Entry<String, Object> entry : properties.entrySet()) {
			setProperty(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public JdbcInsert setProperties(String keys,Object...values) {
		String[] keyArray=keys.split(Constants.DEFFER);
		for (int index = 0; index < keyArray.length; index++) {
			setProperty(keyArray[index], values[index]);
		}
		return this;
	}
	
	public String generatedQuery() {
		AtomicInteger keycount=new AtomicInteger(parameter.size());
		statement.append(" (");
		parameter.forEach((key,value)->{
			statement.append(key);
			if(keycount.decrementAndGet()>0) {
				statement.append(" , ");
			}
		});
		statement.append(" ) VALUES ( ");
		AtomicInteger valcount=new AtomicInteger(parameter.size());
		parameter.forEach((key,value)->{
			JdbcColumn column=getTable().getColumn(key);
			JdbcMapper mapper=JdbcMapper.typeOf(column.getTypeName());
			if(mapper==null) {
				statement.append(JdbcMapper.valueOf(value,column.getTypeName()));
			}else {
				statement.append(JdbcMapper.valueOf(value,mapper));
			}
			if(valcount.decrementAndGet()>0) {
				statement.append(" , ");
			}
		});
		statement.append(" )");
		return statement.toString();
	}
	
	public String selectedQuery() {
		this.statement.append("INSERT INTO "+this.getTable().getTableName()+" ");
		AtomicInteger keycount=new AtomicInteger(parameter.size());
		statement.append("(");
		parameter.forEach((key,value)->{
			statement.append(key);
			if(keycount.decrementAndGet()>0) {
				statement.append(" , ");
			}
		});
		statement.append(") ");
		this.statement.append(this.fetch.setQualifier(getQualifier()).fatchQuery());
		return statement.toString();
	}

	public boolean result() {
		try {
			if(fetch!=null) {
				return table.executeUpdate(selectedQuery());
			}else {
				return table.executeUpdate(generatedQuery());
			}
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

	public JdbcInsert fatch(String table) {
		this.fetch=new JdbcFetch(getCatalog(), table);
		return this;
	}

	public JdbcInsert fatchProperty(String key) {
		Assertion.notNull(this.fetch, "Fatch table configration error.");
		this.fetch.fatchProperty(key);
		return this;
	}

	public JdbcInsert setProperty(String key) {
		this.setProperty(key, null);
		return this;
	}
}