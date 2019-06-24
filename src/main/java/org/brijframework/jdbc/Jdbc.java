package org.brijframework.jdbc;

import org.brijframework.jdbc.factories.JdbcFactory;

public interface Jdbc {

	public JdbcFactory getFactory();

	public String getId();
}
