package org.brijframework.jdbc;

import java.util.HashMap;
import java.util.Map;

import org.brijframework.jdbc.factories.meta.JdbcCatalogFactory;

public class JbdcCatalog extends AbstractJdbc{
	
	private String SOURCE_CAT;
	private String TABLE_CAT;
	private JdbcSource source;
	private JdbcCatalogFactory factory;
	
	private Map<String, JdbcTable> tables;

	public Map<String, JdbcTable> getTables() {
		if(tables==null) {
			tables=new HashMap<String, JdbcTable>();
		}
		return tables;
	}
	
	public String getTABLE_CAT() {
		return TABLE_CAT;
	}

	public void setTABLE_CAT(String tABLE_CAT) {
		TABLE_CAT = tABLE_CAT;
	}

	public void setSOURCE_CAT(String sOURCE_CAT) {
		SOURCE_CAT = sOURCE_CAT;
	}
	
	public String getSOURCE_CAT() {
		return SOURCE_CAT;
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
