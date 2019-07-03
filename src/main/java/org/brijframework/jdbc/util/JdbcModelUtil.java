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
import org.brijframework.util.text.StringUtil;

public class JdbcModelUtil {

	private static String getJavaVarKey(String key) {
		StringBuilder builder=new StringBuilder();
		String[] keyPoint=key.split("_");
		for(int index=0;index<keyPoint.length;index++) {
			if(index==0) {
				builder.append(keyPoint[index].toLowerCase());
				continue;
			}
			builder.append(StringUtil.toCamelCase(keyPoint[index]));
		}
		return builder.toString();
	}
	
	public static Map<String, Map<String, Object>> getTablesMap(Connection conn) throws SQLException {
		Map<String, Map<String, Object>> tables=new HashMap<>();
		for(Map<String, Object> map:getTablesList(conn)) {
			tables.put((String) map.get("tableName"), map);
		}
        return tables;
    }
	
	public static Map<String, Map<String, Object>> getCatalogMap(Connection conn) throws SQLException {
		Map<String, Map<String, Object>> tables=new HashMap<>();
		for(Map<String, Object> map:getCatalogList(conn)) {
			tables.put((String) map.get("tableCat"), map);
		}
        return tables;
    }
     
	public static List<Map<String, Object>> getMetaResultList( ResultSet tables) throws SQLException{
		List<Map<String,Object>> listofTable = new ArrayList<Map<String,Object>>();
	    ResultSetMetaData rsmd = tables.getMetaData();
		int cols = rsmd.getColumnCount();
		while (tables.next()) {
			Map<String,Object> rowMap=new HashMap<>();
			for (int i = 1; i <= cols; i++) {
				rowMap.put(getJavaVarKey(rsmd.getColumnLabel(i)), tables.getObject(i));
			}
			listofTable.add(rowMap);
		}
        return listofTable;
	}
	
	public static List<Map<String, Object>> getCatalogList(Connection conn) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getCatalogs();
        return getMetaResultList(tables);
    }
	
	public static List<Map<String, Object>> getTablesList(Connection conn, JdbcMeta ...types) throws SQLException {
		String [] type=new String[types.length];
		for (int i = 0; i < types.length; i++) {
			type[i]=types[i].toString();
		}
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getTables(conn.getCatalog(), null, "%", type);
        return getMetaResultList(tables);
    }
	
	public static List<Map<String, Object>> getTablesList(Connection conn,String catalog, JdbcMeta ...types) throws SQLException {
		String [] type=new String[types.length];
		for (int i = 0; i < types.length; i++) {
			type[i]=types[i].toString();
		}
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getTables(catalog, null, "%", type);
        return getMetaResultList(tables);
    }
	
	public static List<Map<String, Object>> getColumnList(Connection conn,String table) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getColumns(conn.getCatalog(), null, table, null);
        return getMetaResultList(tables);
    }
	
	public static List<Map<String, Object>> getColumnList(Connection conn,String catalog,String table) throws SQLException {
        DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getColumns(catalog, null, table, null);
        return getMetaResultList(tables);
    }
	
	/**
	 * Retrieves a description of the given table's primary key columns. They are ordered by COLUMN_NAME. Each primary key column description 

	 * @param conn
	 * @param catalog
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getPrimaryKeys(Connection conn,String catalog,String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getPrimaryKeys(catalog, null, table);
        return getMetaResultList(tables);
	}
	
	
	/**
	 * Retrieves a description of the primary key columns that are referenced by the given table's foreign key columns (the primary keys imported by a table). They are ordered by PKTABLE_CAT, PKTABLE_SCHEM
	 * @param conn
	 * @param catalog
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getImportedKeys(Connection conn,String catalog,String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getImportedKeys(catalog, null, table);
        return getMetaResultList(tables);
	}
	
	/**
	 * Retrieves a description of the primary key columns that are referenced by the given table's foreign key columns (the primary keys imported by a table). They are ordered by PKTABLE_CAT, PKTABLE_SCHEM
	 * @param conn
	 * @param table
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getImportedKeys(Connection conn,String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getImportedKeys(conn.getCatalog(), null, table);
        return getMetaResultList(tables);
	}
	
	/**
	 * 
	 * @param conn
	 * @param catalog
	 * @param table
	 * To retrieve a description of the foreign key columns that reference the given table’s primary key columns (the foreign keys exported by a table), you can use the DatabaseMetaData. getExportedKeys() method. This method returns its result as a ResultSet object
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getExportedKeys(Connection conn,String catalog,String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getExportedKeys(catalog, null, table);
        return getMetaResultList(tables);
	}
	
	
	/**
	 * 
	 * @param conn
	 * @param table
	 * To retrieve a description of the foreign key columns that reference the given table’s primary key columns (the foreign keys exported by a table), you can use the DatabaseMetaData. getExportedKeys() method. This method returns its result as a ResultSet object
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getExportedKeys(Connection conn,String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getExportedKeys(conn.getCatalog(), null, table);
        return getMetaResultList(tables);
	}
	/**
	 * Retrieves a description of the foreign key columns in the given foreign key table that reference the primary key or the columns representing a unique constraint of the parent table (could be the same or a different table). The number of columns returned from the parent table must match the number of columns that make up the foreign key. They are ordered by FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, and KEY_SEQ
	 * @param conn
	 * @param parentTable
	 * @param foreignTable
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> getForeignKeys(Connection conn,String parentTable, String foreignTable) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getCrossReference(conn.getCatalog(), null, parentTable, conn.getCatalog(), null, foreignTable);
        return getMetaResultList(tables);
	}

	public static List<Map<String, Object>> getIndexes(Connection conn, String tableCat, String table) throws SQLException {
		DatabaseMetaData md = conn.getMetaData();
        ResultSet tables = md.getIndexInfo(tableCat, null, table, true, true);
        return getMetaResultList(tables);
	}
}
