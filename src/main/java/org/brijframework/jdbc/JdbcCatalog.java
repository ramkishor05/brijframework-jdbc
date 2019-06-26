package org.brijframework.jdbc;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.brijframework.jdbc.factories.meta.JdbcCatalogFactory;
import org.brijframework.jdbc.source.JdbcSource;

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
	
	@JsonIgnore
	private JdbcCatalogFactory factory;

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
	@Override
	public JdbcCatalogFactory getFactory() {
		return factory;
	}

	@JsonIgnore
	public void setFactory(JdbcCatalogFactory factory) {
		this.factory = factory;
	}
	
	@JsonIgnore
	public boolean makeCatalog() throws Exception {
		Connection connection=getSource().getConnection();
		Statement statement=connection.createStatement();
		String query = "CREATE DATABASE "+getTableCat();
		System.err.println("Query   : "+query);
		int result=statement.executeUpdate(query);
	    System.out.println("Catalog "+getTableCat()+" created successfully...");
		return result>0;
	}
}
