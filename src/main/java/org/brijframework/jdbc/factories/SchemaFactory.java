package org.brijframework.jdbc.factories;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.brijframework.model.builder.ModelBuilder;
import org.brijframework.model.factories.ModelFactory;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.formatter.PrintUtil;
import org.brijframework.util.printer.GraphPrinter;

public class SchemaFactory {
	
	private static SchemaFactory factory;
	
	public static SchemaFactory factory() {
		if(factory==null) {
			factory=new SchemaFactory();
		}
		return factory;
	}
	
	public Connection getConnection(String schemaID) throws SQLException {
		DataSource dataSource=ModelFactory.getFactory().getModel(schemaID);
		System.out.println(ModelBuilder.getBuilder(dataSource).getProperties());
		Assertion.notNull(dataSource, "Data source not found for given id :"+schemaID);
		return dataSource.getConnection();
	}
}
