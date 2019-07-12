package org.brijframework.jdbc.template;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.asserts.Assertion;

public class JdbcFetch extends JdbcQualifier<JdbcFetch>{
	private  JdbcCatalog catalog;
	private  JdbcTable table;
	private  StringBuilder statement;
	private  List<String> parameter=new ArrayList<>();
	private Integer limit=0;
	private Integer offset=0;
	
	public JdbcFetch(JdbcCatalog catalog,String table) {
		this.catalog=catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
		this.table=this.catalog.getTables().get(table);
		Assertion.notNull(this.table, "Table not found. this is reqired");
	} 
	
	public JdbcFetch fatchProperty(String key) {
		this.parameter.add(key);
		return this;
	}
	
	public JdbcFetch fatchProperties(List<String> properties) {
		for (String entry : properties) {
			fatchProperty(entry);
		}
		return this;
	}
	
	public JdbcFetch fatchProperties(String ...properties) {
		for (String entry : properties) {
			fatchProperty(entry);
		}
		return this;
	}
	
	public JdbcFetch all() {
		this.parameter.add(" * ");
		return this;
	}
	
	public String fatchQuery() {
		if(parameter.isEmpty()) {
			return "";
		}
		statement=new StringBuilder("SELECT ");
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
	
	public String countQuery() {
		statement=new StringBuilder("SELECT count(*) FROM "+this.table.getTableName());
		statement.append(getQualifier());
		return statement.toString();
	}
	
	public int countRows() throws Exception {
		ResultSet result=table.executeQuery(countQuery());
		result.next();
		return result.getInt(1);
	}
	

	public Map<String,Object> unique() {
		try {
			ResultSet result=table.executeQuery(fatchQuery());
			List<Map<String, Object>>  list=JdbcBeanUtil.getDataResultList(result);
			return list.iterator().next();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Map<String, Object>> list() {
		try {
			ResultSet result=table.executeQuery(fatchQuery());
			return JdbcBeanUtil.getDataResultList(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void forBatch(Consumer<List<Map<String, Object>>> action) {
		try {
			int count=countRows();
			for(int offset=this.offset; offset<=count;offset=offset+limit) {
				String query="";
				if(limit>0) {
					query=fatchQuery()+" LIMIT "+(limit)+" OFFSET "+(offset);
				}else {
					query=fatchQuery();
				}
			    ResultSet result=table.executeQuery(query);
			    action.accept(JdbcBeanUtil.getDataResultList(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void forEach(Consumer<Map<String, Object>> action) {
		try {
			ResultSet result=table.executeQuery(fatchQuery());
			for(Map<String, Object> row:JdbcBeanUtil.getDataResultList(result)) {
				action.accept(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isFatch() {
		return !parameter.isEmpty();
	}

	public JdbcFetch limit(Integer limit) {
		this.limit=limit;
		return this;
	}
	
	public JdbcFetch offset(Integer offset) {
		this.offset=offset;
		return this;
	}
}
