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

public class JdbcUtil {
     
	public static List<Map<String, String>> getTablesList(Connection conn, String ...types) throws SQLException {
		List<Map<String,String>> listofTable = new ArrayList<Map<String,String>>();
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getTables(null, null, "%", types);
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
