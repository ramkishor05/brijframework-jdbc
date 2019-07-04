package org.brijframework.jdbc.factories.model;

import org.brijframework.jdbc.JdbcTable;

public interface JdbcTableMetaFactory extends JdbcMetaFactory{

	JdbcTable getTable(String table);
	
	JdbcTable getTable(String catalog, String table);
	
	JdbcTable getTable(String source, String catalog, String table);
}
