package org.brijframework.jdbc.template;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.util.asserts.Assertion;

public class JdbcStatement {
	private JdbcCatalog catalog;;

	public JdbcStatement(JdbcCatalog catalog) {
		this.catalog = catalog;
		Assertion.notNull(this.catalog, "Catalog is reqired");
	}

	public JdbcFetch fetch(String table) {
		return new JdbcFetch(catalog, table);
	}

	public JdbcUpdate update(String table) {
		return new JdbcUpdate(catalog, table);
	}
	
	public JdbcInsert insert(String table) {
		return new JdbcInsert(catalog, table);
	}
}