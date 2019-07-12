package org.brijframework.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.brijframework.jdbc.factories.model.JdbcTableMetaFactory;
import org.brijframework.jdbc.source.JdbcSource;
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
	
	private Map<String, JdbcPrimary> primaryKeys;
	
	private Map<String, JdbcReferance> foreignKeys;
	
	private Map<String, JdbcReferance> exportedKeys;
	
	private Map<String, JdbcReferance> importedKeys;
	
	private Map<String, JdbcIndex> indexedKeys;
	
	@JsonIgnore
	private JdbcCatalog catalog;
	
	@JsonIgnore
	private JdbcTableMetaFactory factory;

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
	
	public void setForeignKeys(Map<String, JdbcReferance> refTabs) {
		this.foreignKeys = refTabs;
	}
	
	public Map<String, JdbcReferance> getForeignKeys() {
		if(foreignKeys==null) {
			foreignKeys=new HashMap<>();
		}
		return foreignKeys;
	}	

	public Map<String, JdbcPrimary> getPrimaryKeys() {
		if(primaryKeys==null) {
			primaryKeys=new HashMap<>();
		}
		return primaryKeys;
	}

	public void setPrimaryKeys(Map<String, JdbcPrimary> primaryKeys) {
		this.primaryKeys = primaryKeys;
	}
	
	public Map<String, JdbcReferance> getExportedKeys() {
		if(exportedKeys==null) {
			exportedKeys=new HashMap<>();
		}
		return exportedKeys;
	}

	public void setExportedKeys(Map<String, JdbcReferance> exportedKeys) {
		this.exportedKeys = exportedKeys;
	}

	public Map<String, JdbcReferance> getImportedKeys() {
		if(importedKeys==null) {
			importedKeys=new HashMap<>();
		}
		return importedKeys;
	}

	public void setImportedKeys(Map<String, JdbcReferance> importedKeys) {
		this.importedKeys = importedKeys;
	}

	public Map<String, JdbcIndex> getIndexedKeys() {
		if(indexedKeys==null) {
			indexedKeys=new HashMap<>();
		}
		return indexedKeys;
	}

	public void setIndexedKeys(Map<String, JdbcIndex> indexedKeys) {
		this.indexedKeys = indexedKeys;
	}


	@JsonIgnore
	public JdbcTableMetaFactory getFactory() {
		return factory;
	}

	@JsonIgnore
	public void setFactory(JdbcTableMetaFactory factory) {
		this.factory = factory;
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
	public JdbcColumn getColumn(String key) {
		for (Entry<String, JdbcColumn> entry : getColumns().entrySet()) {
			if(entry.getKey().equalsIgnoreCase(key)) {
				return entry.getValue();
			}
		}
		return null;
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
			System.err.println("Query OK, " + result.getFetchSize() + " rows fetched");
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
	public ResultSet executeQuery(String query,int fatch,int limit) throws Exception {
		Statement statement= getStatement();
		statement.setFetchSize(fatch);
		statement.setMaxRows(limit);
		return executeQuery(statement, query);
	}
	
	@JsonIgnore
	public Statement getStatement() throws Exception {
		JdbcTable table = this;
		JdbcSource source = table.getCatalog().getSource();
		Assertion.notNull(source, "Source not found ");
		return source.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	}
	
	
}
