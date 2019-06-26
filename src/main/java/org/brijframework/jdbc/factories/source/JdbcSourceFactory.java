package org.brijframework.jdbc.factories.source;

import java.sql.Connection;
import java.sql.SQLException;

import org.brijframework.jdbc.factories.meta.JdbcMetaFactory;
import org.brijframework.jdbc.source.JdbcSource;

public interface JdbcSourceFactory<K,T> extends JdbcMetaFactory{

	JdbcSource getJdbcSource(String jdbcSource);

	Connection getConnection(String datasource) throws SQLException;

	void register(K id, T dataSource);

}
