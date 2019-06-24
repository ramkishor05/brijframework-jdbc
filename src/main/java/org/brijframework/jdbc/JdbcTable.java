package org.brijframework.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.brijframework.jdbc.factories.meta.JdbcTableFactory;
import org.brijframework.jdbc.util.JdbcUtil;
import org.brijframework.util.asserts.Assertion;

public class JdbcTable extends AbstractJdbc{

	private String TABLE_CAT;
	private String TABLE_NAME;
	private String SELF_REFERENCING_COL_NAME;
	private String TABLE_SCHEM;
	private String TYPE_SCHEM;
	private String TYPE_CAT;
	private String TABLE_TYPE;
	private String REMARKS;
	private String REF_GENERATION;
	private String TYPE_NAME;
	
	private JbdcCatalog catalog;
	
	private Map<String, JdbcColumn> columns;
	private JdbcTableFactory factory;
	

	@Override
	public JdbcTableFactory getFactory() {
		return factory;
	}
	
	public void setFactory(JdbcTableFactory factory) {
		this.factory = factory;
	}
	
	public String getTABLE_CAT() {
		return TABLE_CAT;
	}
	public void setTABLE_CAT(String tABLE_CAT) {
		TABLE_CAT = tABLE_CAT;
	}
	public String getTABLE_NAME() {
		return TABLE_NAME;
	}
	public void setTABLE_NAME(String tABLE_NAME) {
		TABLE_NAME = tABLE_NAME;
	}
	public String getSELF_REFERENCING_COL_NAME() {
		return SELF_REFERENCING_COL_NAME;
	}
	public void setSELF_REFERENCING_COL_NAME(String sELF_REFERENCING_COL_NAME) {
		SELF_REFERENCING_COL_NAME = sELF_REFERENCING_COL_NAME;
	}
	public String getTABLE_SCHEM() {
		return TABLE_SCHEM;
	}
	public void setTABLE_SCHEM(String tABLE_SCHEM) {
		TABLE_SCHEM = tABLE_SCHEM;
	}
	public String getTYPE_SCHEM() {
		return TYPE_SCHEM;
	}
	public void setTYPE_SCHEM(String tYPE_SCHEM) {
		TYPE_SCHEM = tYPE_SCHEM;
	}
	public String getTYPE_CAT() {
		return TYPE_CAT;
	}
	public void setTYPE_CAT(String tYPE_CAT) {
		TYPE_CAT = tYPE_CAT;
	}
	public String getTABLE_TYPE() {
		return TABLE_TYPE;
	}
	public void setTABLE_TYPE(String tABLE_TYPE) {
		TABLE_TYPE = tABLE_TYPE;
	}
	public String getREMARKS() {
		return REMARKS;
	}
	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}
	public String getREF_GENERATION() {
		return REF_GENERATION;
	}
	public void setREF_GENERATION(String rEF_GENERATION) {
		REF_GENERATION = rEF_GENERATION;
	}
	public String getTYPE_NAME() {
		return TYPE_NAME;
	}
	public void setTYPE_NAME(String tYPE_NAME) {
		TYPE_NAME = tYPE_NAME;
	}
	
	public void setCatalog(JbdcCatalog catalog) {
		this.catalog = catalog;
	}
	
	public JbdcCatalog getCatalog() {
		return catalog;
	}
	
	public Map<String, JdbcColumn> getColumns() {
		if (columns==null) {
			columns=new HashMap<String, JdbcColumn>();
		}
		return columns;
	}
	
	public JdbcColumn getColumn(String key) {
		return getColumns().get(key);
	}
	
	public boolean  execute(Statement st,String query) {
		int n = 0;
		try {
			System.err.println("Query   :" + query);
			n = st.executeUpdate(query);
			System.err.println("Query OK, " + n + " rows affected");
		} catch (SQLException e) {
			System.err.println("Query Error, "+e.getMessage());
		}
		return n>0;
	}
	
	public ResultSet executeQuery(Statement st,String query) {
		ResultSet result = null;
		try {
			System.err.println("Query   :" + query);
			result = st.executeQuery(query);
			System.err.println("Query OK, " + result.getFetchSize() + " rows affected");
		} catch (SQLException e) {
			System.err.println("Query Error, "+e.getMessage());
		}
		return result;
	}
	
	public ResultSet executeQuery(String query) throws Exception {
		Statement statement= getStatement();
		return executeQuery(statement, query);
	}
	
	public Statement getStatement() throws Exception {
		JdbcTable table = this;
		JdbcSource source = table.getCatalog().getSource();
		Assertion.notNull(source, "Source not found ");
		Statement st = source.getConnection().createStatement();
		return st;
	}
	
	public boolean updateColumn(String oldName,String newName, String type, int size) throws Exception {
		JdbcColumn column = this.getColumn(oldName);
		Assertion.notNull(column, "Column not found for given :" + oldName);
		return column.updateColumn(newName,type,size);
	}
	
	public boolean addColumn(String colname, String type, int size) throws Exception {
		Statement statement= getStatement();
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" ADD "+colname+" "+type+"("+size+")";
		return execute(statement, query);
	}
	
	public boolean setDefaultColumn(String colname, Object value) throws Exception {
		JdbcColumn column = this.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.setDefaultColumn(value);
	}
	
	public boolean modifyColumn(String colname, String type, int size) throws Exception {
		JdbcColumn column = this.getColumn(colname);
		Assertion.notNull(column, "column not found for given :" + colname);
		return column.modifyColumn(type,size);
	}
	
	public List<Map<String, Object>> getAllRows() throws Exception {
		String query = "SELECT * FROM "+this.getTABLE_NAME()+";";
		ResultSet result=executeQuery(query);
		return JdbcUtil.getList(result);
	}
}
