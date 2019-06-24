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
     
	public static List<Map<String, Object>> getTablesList(Connection conn, JdbcMeta ...types) throws SQLException {
		String [] type=new String[types.length];
		for (int i = 0; i < types.length; i++) {
			type[i]=types[i].toString();
		}
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getTables(conn.getCatalog(), null, "%", type);
        return getList(tables);
    }
	
	public static List<Map<String, Object>> getCatalogList(Connection conn) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getCatalogs();
        return getList(tables);
    }
	
	public static List<Map<String, Object>> getColumnList(Connection conn,String table) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getColumns(conn.getCatalog(), null, table, null);
        return getList(tables);
    }
	
	public static List<Map<String, Object>> getList( ResultSet tables) throws SQLException{
		List<Map<String,Object>> listofTable = new ArrayList<Map<String,Object>>();
	    ResultSetMetaData rsmd = tables.getMetaData();
		int cols = rsmd.getColumnCount();
		while (tables.next()) {
			Map<String,Object> rowMap=new HashMap<>();
			for (int i = 1; i <= cols; i++) {
				rowMap.put(tables.getMetaData().getColumnLabel(i), tables.getObject(i));
			}
			listofTable.add(rowMap);
		}
        return listofTable;
	}
}
