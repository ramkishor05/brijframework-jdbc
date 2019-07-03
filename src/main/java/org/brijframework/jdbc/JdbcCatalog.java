package org.brijframework.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.jdbc.util.JdbcModelUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JdbcCatalog extends AbstractJdbc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sourceCat;
	private String tableCat;
	
	@JsonIgnore
	private JdbcSource source;
	
	private Map<String, JdbcTable> tables;

	public Map<String, JdbcTable> getTables() {
		if (tables == null) {
			tables = new HashMap<String, JdbcTable>();
		}
		return tables;
	}

	public String getSourceCat() {
		return sourceCat;
	}

	public void setSourceCat(String sourceCat) {
		this.sourceCat = sourceCat;
	}

	public String getTableCat() {
		return tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public void setTables(Map<String, JdbcTable> tables) {
		this.tables = tables;
	}

	@JsonIgnore
	public void setSource(JdbcSource source) {
		this.source = source;
	}

	@JsonIgnore
	public JdbcSource getSource() {
		return source;
	}
	
	@JsonIgnore
	public boolean makeCatalog() throws Exception {
		Connection connection=getSource().getConnection();
		Map<String, Map<String, Object>> catalogMap=JdbcModelUtil.getCatalogMap(connection);
		if(catalogMap.containsKey(getTableCat())) {
			System.err.println("Error   : "+"Catalog already exist.");
			return false;
		}
		Statement statement=connection.createStatement();
		String query = "CREATE DATABASE "+getTableCat();
		System.err.println("Query   : "+query);
		int result=statement.executeUpdate(query);
	    System.err.println("Catalog "+getTableCat()+" created successfully...");
		return result>0;
	}
}
