package org.brijframework.jdbc.manager;

import java.sql.Statement;

import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcSource;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.factories.meta.impl.JdbcColumnFactoryImpl;
import org.brijframework.util.asserts.Assertion;

public class JdbcManager {

	public static boolean alterColumnName(String id,String colname) throws Exception {
		System.out.println(id);
		JdbcColumn column= JdbcColumnFactoryImpl.getFactory().getColumn(id);
		System.out.println("column="+column);
		Assertion.notNull(column, "column not found for given :"+id);
		JdbcTable table=column.getJdbcTable();
		Assertion.notNull(table, "table not found for given :"+id);
		JbdcCatalog catalog=table.getCatalog();
		Assertion.notNull(catalog, "catalog not found for given :"+id);
		JdbcSource source= catalog.getSource();
		Assertion.notNull(source, "source not found for given :"+id);
		Statement st = source.getDataSource().getConnection().createStatement();
		String query="ALTER TABLE "+catalog.getTABLE_CAT()+"."+table.getTABLE_NAME() +" CHANGE "+column.getCOLUMN_NAME()+"  "+colname+" "+column.getDATA_TYPE();
		System.out.println("query="+query);
	    int n = st.executeUpdate(query);
	    System.out.println("Query OK, " + n + " rows affected");
		return false;
	}
}
