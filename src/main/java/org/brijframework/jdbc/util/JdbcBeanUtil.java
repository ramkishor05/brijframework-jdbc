package org.brijframework.jdbc.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.util.asserts.Assertion;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JdbcBeanUtil {
	
	public static boolean makeTable(JdbcTable table) throws Exception {
		StringBuilder builder = new StringBuilder("CREATE TABLE " +table.getTableCat()+"." + table.getTableName());
		builder.append("(");
		AtomicInteger count = new AtomicInteger();
		table.getColumns().forEach((key, column) -> {
			builder.append(key);
			JdbcMapper mapper=JdbcMapper.typeOf(column.getTypeName());
			if(mapper!=null && !mapper.arg) { 
				builder.append(" " + column.getTypeName());
			} else if (column.getColumnSize() != null
					&& column.getColumnSize() > 0) {
				builder.append(" " + column.getTypeName() + " (" + column.getColumnSize() + ")");
			} 
			if (count.incrementAndGet() < table.getColumns().size()) {
				builder.append(", ");
			}
		});
		builder.append(")");
		String query = builder.toString();
		return table.executeUpdate(query);
	}
	
	@JsonIgnore
	public static boolean addObject(JdbcTable table,Map<String, Object> map) throws Exception {
		StringBuilder query = new StringBuilder("INSERT INTO "+table.getTableCat()+"."+table.getTableName());
		StringBuilder keyset =new StringBuilder();
		StringBuilder keyval =new StringBuilder();
		keyset.append("(");
		keyval.append(" VALUES(");
		AtomicInteger count=new AtomicInteger(0);
		map.forEach((key,value)->{
			JdbcColumn column=table.getColumn(key);
			if(column!=null) {
				keyset.append(key);
				JdbcMapper mapper=JdbcMapper.typeOf(column.getTypeName());
				if(mapper==null) {
				  keyval.append(JdbcMapper.valueOf(value,column.getTypeName()));
				}else {
				  keyval.append(JdbcMapper.valueOf(value,mapper));
				}
				if(count.incrementAndGet()<map.size()) {
					keyset.append(", ");
					keyval.append(", ");
				}
			}
		});
		keyset.append(")");
		keyval.append(")");
		query.append(keyset);
		query.append(keyval);
		return table.executeUpdate(query.toString());
	}
	

	public static boolean updateObject(JdbcTable table, Map<String, Object> params) throws Exception {
		StringBuilder query = new StringBuilder("UPDATE "+table.getTableCat()+"."+table.getTableName()+ " SET ");
		List<JdbcColumn> qualifColumns=new ArrayList<>();
		List<JdbcColumn> updateColumns=new ArrayList<>();
		params.forEach((key,value)->{
			JdbcColumn column=table.getColumn(key);
			Assertion.notNull(column, "No such type of column found : "+key);
			if(column!=null && !table.getPrimaryKeys().containsKey(key)) {
				updateColumns.add(column);
			}
		});
		
		params.forEach((key,value)->{
			JdbcColumn column=table.getColumn(key);
			if(column!=null && table.getPrimaryKeys().containsKey(key)) {
				qualifColumns.add(column);
			}
		});
		
		AtomicInteger updateColumnsCount=new AtomicInteger(0);
		updateColumns.forEach(column->{
			query.append(column.getColumnName()+" = ");
			JdbcMapper mapper=JdbcMapper.typeOf(column.getTypeName());
			if(mapper==null) {
				query.append(JdbcMapper.valueOf(params.get(column.getColumnName()),column.getTypeName()));
			}else {
				query.append(JdbcMapper.valueOf(params.get(column.getColumnName()),mapper));
			}
			if(updateColumnsCount.incrementAndGet()<updateColumns.size()) {
				query.append(", ");
			}
		});
		query.append(" WHERE ");
		AtomicInteger qualifColumnsCount=new AtomicInteger(0);
		qualifColumns.forEach(column->{
			query.append(column.getColumnName()+" = ");
			JdbcMapper mapper=JdbcMapper.typeOf(column.getTypeName());
			if(mapper==null) {
				query.append(JdbcMapper.valueOf(params.get(column.getColumnName()),column.getTypeName()));
			}else {
				query.append(JdbcMapper.valueOf(params.get(column.getColumnName()),mapper));
			}
			if(qualifColumnsCount.incrementAndGet()<qualifColumns.size()) {
				query.append(" AND ");
			}
		});
		return table.executeUpdate(query.toString());
	}
	
	
	public static List<Map<String, Object>> getDataResultList( ResultSet tables) throws SQLException{
		List<Map<String,Object>> listofTable = new ArrayList<Map<String,Object>>();
	    ResultSetMetaData rsmd = tables.getMetaData();
		int cols = rsmd.getColumnCount();
		while (tables.next()) {
			Map<String,Object> rowMap=new HashMap<>();
			for (int i = 1; i <= cols; i++) {
				rowMap.put(rsmd.getColumnLabel(i), tables.getObject(i));
			}
			listofTable.add(rowMap);
		}
        return listofTable;
	}


	public static boolean updateColumn(JdbcTable table,String oldName,String newName, String type, int size) throws Exception {
		JdbcColumn column = table.getColumn(oldName);
		Assertion.notNull(column, "Column not found for given :" + oldName);
		return column.updateColumn(newName,type,size);
	}
	
	public static boolean addColumn(JdbcTable table,String colname, String type, int size) throws Exception {
		Statement statement= table.getStatement();
		String query = "ALTER TABLE "+table.getTableName()+" ADD "+colname+" "+type+"("+size+")";
		return table.executeUpdate(statement, query);
	}
	
	public static boolean setDefaultColumn(JdbcTable table,String colname, Object value) throws Exception {
		JdbcColumn column = table.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.setDefaultColumn(value);
	}
	
	public static boolean modifyColumn(JdbcTable table,String colname, String type, int size) throws Exception {
		JdbcColumn column = table.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.modifyColumn(type,size);
	}
	
	public static List<Map<String, Object>> getAllRows(JdbcTable table) throws Exception {
		String query = "SELECT * FROM "+table.getTableName()+";";
		ResultSet result=table.executeQuery(query);
		return JdbcBeanUtil.getDataResultList(result);
	}
}
