package org.brijframework.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.jdbc.factories.meta.JdbcCatalogFactory;

public class JdbcCatalog extends AbstractJdbc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sourceCat;
	private String tableCat;
	private JdbcSource source;
	
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

	public void setSource(JdbcSource source) {
		this.source = source;
	}

	public JdbcSource getSource() {
		return source;
	}

	@Override
	public JdbcCatalogFactory getFactory() {
		return factory;
	}

	public void setFactory(JdbcCatalogFactory factory) {
		this.factory = factory;
	}
}
