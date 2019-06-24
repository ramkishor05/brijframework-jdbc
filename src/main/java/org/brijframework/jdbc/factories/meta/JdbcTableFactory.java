package org.brijframework.jdbc.factories.meta;

import org.brijframework.jdbc.JdbcTable;

public interface JdbcTableFactory extends JdbcMetaFactory{

	JdbcTable getTable(String table);
	
	JdbcTable getTable(String catalog, String table);
	
	JdbcTable getTable(String source, String catalog, String table);
}
