package org.brijframework.jdbc.factories.meta;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.brijframework.jdbc.JdbcSource;
import org.brijframework.jdbc.factories.meta.JdbcMetaFactory;

public interface JdbcSourceFactory extends JdbcMetaFactory{

	JdbcSource getJdbcSource(String jdbcSource);

	Connection getConnection(String datasource) throws SQLException;

	void register(String id, DataSource dataSource);

}
