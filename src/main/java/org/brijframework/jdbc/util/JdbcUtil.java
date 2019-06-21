package org.brijframework.jdbc.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.jdbc.constants.JdbcMeta;

public class JdbcUtil {
     
	public static List<Map<String, String>> getTablesList(Connection conn, JdbcMeta ...types) throws SQLException {
		String [] type=new String[types.length];
		for (int i = 0; i < types.length; i++) {
			type[i]=types[i].toString();
		}
		System.out.println(conn.getCatalog());
		List<Map<String,String>> listofTable = new ArrayList<Map<String,String>>();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getTables(conn.getCatalog(), null, "%", type);
        ResultSetMetaData rsmd = tables.getMetaData();
		int cols = rsmd.getColumnCount();
		while (tables.next()) {
			Map<String,String> rowMap=new HashMap<>();
			for (int i = 1; i <= cols; i++) {
				rowMap.put(tables.getMetaData().getColumnLabel(i), tables.getString(i));
			}
			listofTable.add(rowMap);
		}
        return listofTable;
    }
	
}
