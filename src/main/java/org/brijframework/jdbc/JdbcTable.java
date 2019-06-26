package org.brijframework.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.brijframework.jdbc.factories.meta.JdbcTableFactory;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.jdbc.util.JdbcUtil;
import org.brijframework.util.asserts.Assertion;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JdbcTable extends AbstractJdbc{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tableCat;
	private String tableName;
	private String selfReferencingColName;
	private String tableSchem;
	private String typeSchem;
	private String typeCat;
	private String tableType;
	private String remarks;
	private String refGeneration;
	private String typeName;

	private Map<String, JdbcColumn> columns;
	
	private Map<String, JdbcRefTab> foreignKeys;
	
	private Map<String, JdbcRefTab> referenKeys;
	
	@JsonIgnore
	private JdbcCatalog catalog;
	
	@JsonIgnore
	private JdbcTableFactory factory;

	public String getTableCat() {
		return tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSelfReferencingColName() {
		return selfReferencingColName;
	}

	public void setSelfReferencingColName(String selfReferencingColName) {
		this.selfReferencingColName = selfReferencingColName;
	}

	public String getTableSchem() {
		return tableSchem;
	}

	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}

	public String getTypeSchem() {
		return typeSchem;
	}

	public void setTypeSchem(String typeSchem) {
		this.typeSchem = typeSchem;
	}

	public String getTypeCat() {
		return typeCat;
	}

	public void setTypeCat(String typeCat) {
		this.typeCat = typeCat;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRefGeneration() {
		return refGeneration;
	}

	public void setRefGeneration(String refGeneration) {
		this.refGeneration = refGeneration;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public void setColumns(Map<String, JdbcColumn> columns) {
		this.columns = columns;
	}

	public Map<String, JdbcColumn> getColumns() {
		if (columns==null) {
			columns=new HashMap<String, JdbcColumn>();
		}
		return columns;
	}
	
	public void setForeignKeys(Map<String, JdbcRefTab> refTabs) {
		this.foreignKeys = refTabs;
	}
	
	public Map<String, JdbcRefTab> getForeignKeys() {
		if(foreignKeys==null) {
			foreignKeys=new HashMap<>();
		}
		return foreignKeys;
	}	

	public Map<String, JdbcRefTab> getReferenKeys() {
		if(referenKeys==null) {
			referenKeys=new HashMap<>();
		}
		return referenKeys;
	}

	public void setReferenKeys(Map<String, JdbcRefTab> referenKeys) {
		this.referenKeys = referenKeys;
	}
	
	@JsonIgnore
	public void setCatalog(JdbcCatalog catalog) {
		this.catalog = catalog;
	}
	
	@JsonIgnore
	public JdbcCatalog getCatalog() {
		return catalog;
	}
	
	@JsonIgnore
	@Override
	public JdbcTableFactory getFactory() {
		return factory;
	}
	
	@JsonIgnore
	public void setFactory(JdbcTableFactory factory) {
		this.factory = factory;
	}
	
	@JsonIgnore
	public JdbcColumn getColumn(String key) {
		return getColumns().get(key);
	}
	
	@JsonIgnore
	public boolean  executeUpdate(Statement st,String query) {
		int n = 0;
		try {
			System.err.println("Query        :" + query);
			n = st.executeUpdate(query);
			System.err.println("Query OK, " + n + " rows affected");
		} catch (SQLException e) {
			System.err.println("Query Error, "+e.getMessage());
		}
		return n>0;
	}
	
	@JsonIgnore
	public boolean  executeUpdate(String query) throws Exception {
		return executeUpdate(getStatement(), query);
	}
	
	@JsonIgnore
	public ResultSet executeQuery(Statement st,String query) {
		ResultSet result = null;
		try {
			System.err.println("Query        :" + query);
			result = st.executeQuery(query);
			System.err.println("Query OK, " + result.getFetchSize() + " rows affected");
		} catch (SQLException e) {
			System.err.println("Query Error, "+e.getMessage());
		}
		return result;
	}
	
	@JsonIgnore
	public ResultSet executeQuery(String query) throws Exception {
		Statement statement= getStatement();
		return executeQuery(statement, query);
	}
	
	@JsonIgnore
	public Statement getStatement() throws Exception {
		JdbcTable table = this;
		JdbcSource source = table.getCatalog().getSource();
		Assertion.notNull(source, "Source not found ");
		return source.getConnection().createStatement();
	}
	
	@JsonIgnore
	public boolean updateColumn(String oldName,String newName, String type, int size) throws Exception {
		JdbcColumn column = this.getColumn(oldName);
		Assertion.notNull(column, "Column not found for given :" + oldName);
		return column.updateColumn(newName,type,size);
	}
	
	@JsonIgnore
	public boolean addColumn(String colname, String type, int size) throws Exception {
		Statement statement= getStatement();
		String query = "ALTER TABLE "+this.getTableName()+" ADD "+colname+" "+type+"("+size+")";
		return executeUpdate(statement, query);
	}
	
	@JsonIgnore
	public boolean setDefaultColumn(String colname, Object value) throws Exception {
		JdbcColumn column = this.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.setDefaultColumn(value);
	}
	
	@JsonIgnore
	public boolean modifyColumn(String colname, String type, int size) throws Exception {
		JdbcColumn column = this.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.modifyColumn(type,size);
	}
	
	@JsonIgnore
	public List<Map<String, Object>> getAllRows() throws Exception {
		String query = "SELECT * FROM "+this.getTableName()+";";
		ResultSet result=executeQuery(query);
		return JdbcUtil.getResultList(result);
	}
	
	@JsonIgnore
	public boolean addRow(Map<String, Object> map) throws Exception {
		StringBuilder query = new StringBuilder("INSERT INTO "+this.getTableName());
		StringBuilder keyset =new StringBuilder();
		StringBuilder keyval =new StringBuilder();
		keyset.append("(");
		keyval.append(" VALUES(");
		AtomicInteger count=new AtomicInteger(0);
		map.forEach((key,val)->{
			keyset.append(key);
			if(val instanceof String)
			    keyval.append("'"+val+"'");
			else
				keyval.append(val);
			if(count.incrementAndGet()<map.size()) {
				keyset.append(", ");
				keyval.append(", ");
			}
		});
		keyset.append(")");
		keyval.append(")");
		query.append(keyset);
		query.append(keyval);
		return executeUpdate(query.toString());
	}
	
	
	public boolean makeTable() throws Exception {
		StringBuilder builder = new StringBuilder("CREATE TABLE " + getTableCat() + "." + getTableName());
		builder.append("(");
		AtomicInteger count = new AtomicInteger();
		getColumns().forEach((key, column) -> {
			builder.append(key);
			if(nonArgTypes.contains(column.getTypeName())) {
				builder.append(" " + column.getTypeName());
			} else if (column.getColumnSize() != null
					&& column.getColumnSize() > 0) {
				builder.append(" " + column.getTypeName() + " (" + column.getColumnSize() + ")");
			} 
			if (count.incrementAndGet() < getColumns().size()) {
				builder.append(", ");
			}
		});
		builder.append(")");
		String query = builder.toString();
		return this.executeUpdate(query);
	}
	
	public static Set<String> nonArgTypes=new HashSet<String>();
	static {
		nonArgTypes.add("CLOB");
		nonArgTypes.add("BLOB");
		nonArgTypes.add("TINYBLOB");
		nonArgTypes.add("DOUBLE");
		nonArgTypes.add("DATE");
		nonArgTypes.add("DATETIME");
		nonArgTypes.add("TIME");
		nonArgTypes.add("TIMESTAMP");
		nonArgTypes.add("TIME_WITH_TIMEZONE");
		nonArgTypes.add("TIMESTAMP_WITH_TIMEZONE");
	}
}
