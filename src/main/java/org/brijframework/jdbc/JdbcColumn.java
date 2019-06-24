package org.brijframework.jdbc;

import java.sql.Statement;

import org.brijframework.jdbc.factories.meta.JdbcColumnFactory;

public class JdbcColumn extends AbstractJdbc{

	private String SCOPE_TABLE;
	private String TABLE_CAT;
	private String BUFFER_LENGTH;
	private String IS_NULLABLE;
	private String TABLE_NAME;
	private String COLUMN_DEF;
	private String SCOPE_CATALOG;
	private String TABLE_SCHEM;
	private String COLUMN_NAME;
	private String NULLABLE;
	private String REMARKS;
	private String DECIMAL_DIGITS;
	private String NUM_PREC_RADIX;
	private String SQL_DATETIME_SUB;
	private String IS_GENERATEDCOLUMN;
	private String IS_AUTOINCREMENT;
	private String SQL_DATA_TYPE;
	private String CHAR_OCTET_LENGTH;
	private String ORDINAL_POSITION;
	private String SCOPE_SCHEMA;
	private String SOURCE_DATA_TYPE;
	private String DATA_TYPE;
	private String TYPE_NAME;
	private int COLUMN_SIZE;

	private JdbcTable jdbcTable;
	private JdbcColumnFactory factory;

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

	public String getTABLE_SCHEM() {
		return TABLE_SCHEM;
	}

	public void setTABLE_SCHEM(String tABLE_SCHEM) {
		TABLE_SCHEM = tABLE_SCHEM;
	}

	public String getREMARKS() {
		return REMARKS;
	}

	public void setREMARKS(String rEMARKS) {
		REMARKS = rEMARKS;
	}

	public String getTYPE_NAME() {
		return TYPE_NAME;
	}

	public void setTYPE_NAME(String tYPE_NAME) {
		TYPE_NAME = tYPE_NAME;
	}

	public String getSCOPE_TABLE() {
		return SCOPE_TABLE;
	}

	public void setSCOPE_TABLE(String sCOPE_TABLE) {
		SCOPE_TABLE = sCOPE_TABLE;
	}

	public String getBUFFER_LENGTH() {
		return BUFFER_LENGTH;
	}

	public void setBUFFER_LENGTH(String bUFFER_LENGTH) {
		BUFFER_LENGTH = bUFFER_LENGTH;
	}

	public String getIS_NULLABLE() {
		return IS_NULLABLE;
	}

	public void setIS_NULLABLE(String iS_NULLABLE) {
		IS_NULLABLE = iS_NULLABLE;
	}

	public String getCOLUMN_DEF() {
		return COLUMN_DEF;
	}

	public void setCOLUMN_DEF(String cOLUMN_DEF) {
		COLUMN_DEF = cOLUMN_DEF;
	}

	public String getSCOPE_CATALOG() {
		return SCOPE_CATALOG;
	}

	public void setSCOPE_CATALOG(String sCOPE_CATALOG) {
		SCOPE_CATALOG = sCOPE_CATALOG;
	}

	public String getCOLUMN_NAME() {
		return COLUMN_NAME;
	}

	public void setCOLUMN_NAME(String cOLUMN_NAME) {
		COLUMN_NAME = cOLUMN_NAME;
	}

	public String getNULLABLE() {
		return NULLABLE;
	}

	public void setNULLABLE(String nULLABLE) {
		NULLABLE = nULLABLE;
	}

	public String getDECIMAL_DIGITS() {
		return DECIMAL_DIGITS;
	}

	public void setDECIMAL_DIGITS(String dECIMAL_DIGITS) {
		DECIMAL_DIGITS = dECIMAL_DIGITS;
	}

	public String getNUM_PREC_RADIX() {
		return NUM_PREC_RADIX;
	}

	public void setNUM_PREC_RADIX(String nUM_PREC_RADIX) {
		NUM_PREC_RADIX = nUM_PREC_RADIX;
	}

	public String getSQL_DATETIME_SUB() {
		return SQL_DATETIME_SUB;
	}

	public void setSQL_DATETIME_SUB(String sQL_DATETIME_SUB) {
		SQL_DATETIME_SUB = sQL_DATETIME_SUB;
	}

	public String getIS_GENERATEDCOLUMN() {
		return IS_GENERATEDCOLUMN;
	}

	public void setIS_GENERATEDCOLUMN(String iS_GENERATEDCOLUMN) {
		IS_GENERATEDCOLUMN = iS_GENERATEDCOLUMN;
	}

	public String getIS_AUTOINCREMENT() {
		return IS_AUTOINCREMENT;
	}

	public void setIS_AUTOINCREMENT(String iS_AUTOINCREMENT) {
		IS_AUTOINCREMENT = iS_AUTOINCREMENT;
	}

	public String getSQL_DATA_TYPE() {
		return SQL_DATA_TYPE;
	}

	public void setSQL_DATA_TYPE(String sQL_DATA_TYPE) {
		SQL_DATA_TYPE = sQL_DATA_TYPE;
	}

	public String getCHAR_OCTET_LENGTH() {
		return CHAR_OCTET_LENGTH;
	}

	public void setCHAR_OCTET_LENGTH(String cHAR_OCTET_LENGTH) {
		CHAR_OCTET_LENGTH = cHAR_OCTET_LENGTH;
	}

	public String getORDINAL_POSITION() {
		return ORDINAL_POSITION;
	}

	public void setORDINAL_POSITION(String oRDINAL_POSITION) {
		ORDINAL_POSITION = oRDINAL_POSITION;
	}

	public String getSCOPE_SCHEMA() {
		return SCOPE_SCHEMA;
	}

	public void setSCOPE_SCHEMA(String sCOPE_SCHEMA) {
		SCOPE_SCHEMA = sCOPE_SCHEMA;
	}

	public String getSOURCE_DATA_TYPE() {
		return SOURCE_DATA_TYPE;
	}

	public void setSOURCE_DATA_TYPE(String sOURCE_DATA_TYPE) {
		SOURCE_DATA_TYPE = sOURCE_DATA_TYPE;
	}

	public String getDATA_TYPE() {
		return DATA_TYPE;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}

	public int getCOLUMN_SIZE() {
		return COLUMN_SIZE;
	}

	public void setCOLUMN_SIZE(int cOLUMN_SIZE) {
		COLUMN_SIZE = cOLUMN_SIZE;
	}

	public void setJdbcTable(JdbcTable jdbcTable) {
		this.jdbcTable = jdbcTable;
	}

	public void setTable(JdbcTable jdbcTable) {
		this.jdbcTable = jdbcTable;
	}

	public JdbcTable getJdbcTable() {
		return jdbcTable;
	}
	
	public boolean updateColumn(String colname) throws Exception {
		Statement statement= this.getJdbcTable().getStatement();
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" CHANGE "+this.getCOLUMN_NAME()+" "+colname+" "+this.getTYPE_NAME();
		boolean reslt=this.getJdbcTable().executeUpdate(statement, query);
		if(reslt) {
			this.setCOLUMN_NAME(colname);
		}
		return reslt;
	}
	
	public boolean updateColumn(String colname,String type) throws Exception {
		Statement statement= this.getJdbcTable().getStatement();
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" CHANGE "+this.getCOLUMN_NAME()+" "+colname+" "+type;
		boolean reslt=this.getJdbcTable().executeUpdate(statement, query);
		if(reslt) {
			this.setCOLUMN_NAME(colname);
			this.setTYPE_NAME(type);
		}
		return reslt;
	}
	
	
	public boolean updateColumn(String colname,String type, int size) throws Exception {
		Statement statement= this.getJdbcTable().getStatement();
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" CHANGE "+this.getCOLUMN_NAME()+" "+colname+" "+type+"("+size+")";
		boolean reslt=this.getJdbcTable().executeUpdate(statement, query);
		if(reslt) {
			this.setCOLUMN_NAME(colname);
			this.setTYPE_NAME(type);
			this.setCOLUMN_SIZE(size);
		}
		return reslt;
	}
	
	public boolean setDefaultColumn(Object value) throws Exception {
		Statement statement= this.getJdbcTable().getStatement();
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" ALTER "+this.getCOLUMN_NAME()+" SET DEFAULT "+value;
		return this.getJdbcTable().executeUpdate(statement, query);
	}
	
	public boolean modifyColumn(String type, int size) throws Exception {
		Statement statement= this.getJdbcTable().getStatement();		
		String query = "ALTER TABLE "+this.getTABLE_NAME()+" MODIFY "+this.getCOLUMN_NAME()+" "+type+"("+size+")";
		boolean reslt=this.getJdbcTable().executeUpdate(statement, query);
		if(reslt) {
			this.setTYPE_NAME(type);
			this.setCOLUMN_SIZE(size);
		}
		return reslt;
	}

	@Override
	public JdbcColumnFactory getFactory() {
		return factory;
	}
	
	public void setFactory(JdbcColumnFactory factory) {
		this.factory = factory;
	}
}
