package org.brijframework.jdbc;

import java.sql.Statement;

import org.brijframework.jdbc.factories.meta.JdbcColumnFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class JdbcColumn extends AbstractJdbc {
	
	private static final long serialVersionUID = 1L;
	private String tableCat; // TABLE_CAT String => table catalog (may be null) 
	private String tableSchem; //TABLE_SCHEM String => table schema (may be null) 
	private String tableName; //TABLE_NAME String => table name 
	private String columnName; //COLUMN_NAME String => column name 
	private Integer dataType; //DATA_TYPE Integer => SQL type from java.sql.Types 
	private String typeName; //TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
	private Integer columnSize; //COLUMN_SIZE Integer => column size. 
	private String bufferLength;//BUFFER_LENGTH is not used. 
	private Integer decimalDigits; //DECIMAL_DIGITS Integer => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable. 
	private Integer numPrecRadix; //NUM_PREC_RADIX Integer => Radix (typically either 10 or 2) 
	private Integer nullable;  //NULLABLE Integer => is NULL allowed. ◦ columnNoNulls - might not allow NULL values 
	/*
	◦ columnNullable - definitely allows NULL values 
	◦ columnNullableUnknown - nullability unknown 
	*/
	private String remarks; //REMARKS String => comment describing column (may be null) 
	private String columnDef; //COLUMN_DEF String => default value for the column, which should be Integererpreted as a string when the value is enclosed in single quotes (may be null) 
	private Integer sqlDataType; //SQL_DATA_TYPE Integer => unused 
	private Integer sqlDatetimeSub; //SQL_DATETIME_SUB Integer => unused 
	private String charOctetLength; //CHAR_OCTET_LENGTH Integer => for char types the maximum number of bytes in the column 
	private Integer ordinalPosition; //ORDINAL_POSITION Integer => index of column in table (starting at 1) 
	private String isNullable;  //IS_NULLABLE String => ISO rules are used to determine the nullability for a column. ◦ YES --- if the column can include NULLs 
	/*	
	 ◦ NO --- if the column cannot include NULLs 
	 ◦ empty string --- if the nullability for the column is unknown 
	*/
	private String scopeCatalog; //SCOPE_CATALOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF) 
	private String scopeSchema; //SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
	private String scopeTable; // SCOPE_TABLE String => table name that this the scope of a reference attribute (null if the DATA_TYPE isn't REF) 
	private Short sqlDateType; //SOURCE_DATA_TYPE Short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
	private String sourceDataType; //SOURCE_DATA_TYPE Short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF) 
	
	private String isAutoincrement; //IS_AUTOINCREMENT String => Indicates whether this column is auto incremented ◦ YES --- if the column is auto incremented 
	/*
	◦ NO --- if the column is not auto incremented 
	◦ empty string --- if it cannot be determined whether the column is auto incremented 
	*/
	private String isGeneratedcolumn;  //IS_GENERATEDCOLUMN String => Indicates whether this is a generated column ◦ YES --- if this a generated column 
	/*
	◦ NO --- if this not a generated column 
	◦ empty string --- if it cannot be determined whether this is a generated column  
	*/
	
	@JsonIgnore
	private JdbcTable jdbcTable;
	
	@JsonIgnore
	private JdbcColumnFactory factory;
	
	@JsonIgnore
	public JdbcTable getJdbcTable() {
		return jdbcTable;
	}

	@JsonIgnore
	public void setJdbcTable(JdbcTable jdbcTable) {
		this.jdbcTable = jdbcTable;
	}

	@JsonIgnore
	@Override
	public JdbcColumnFactory getFactory() {
		return factory;
	}

	@JsonIgnore
	public void setFactory(JdbcColumnFactory factory) {
		this.factory = factory;
	}

	public String getTableCat() {
		return tableCat;
	}

	public void setTableCat(String tableCat) {
		this.tableCat = tableCat;
	}

	public String getTableSchem() {
		return tableSchem;
	}

	public void setTableSchem(String tableSchem) {
		this.tableSchem = tableSchem;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Integer getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(Integer columnSize) {
		this.columnSize = columnSize;
	}

	public String getBufferLength() {
		return bufferLength;
	}

	public void setBufferLength(String bufferLength) {
		this.bufferLength = bufferLength;
	}

	public Integer getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(Integer decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public Integer getNumPrecRadix() {
		return numPrecRadix;
	}

	public void setNumPrecRadix(Integer numPrecRadix) {
		this.numPrecRadix = numPrecRadix;
	}

	public Integer getNullable() {
		return nullable;
	}

	public void setNullable(Integer nullable) {
		this.nullable = nullable;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getColumnDef() {
		return columnDef;
	}

	public void setColumnDef(String columnDef) {
		this.columnDef = columnDef;
	}

	public Integer getSqlDataType() {
		return sqlDataType;
	}

	public void setSqlDataType(Integer sqlDataType) {
		this.sqlDataType = sqlDataType;
	}

	public Integer getSqlDatetimeSub() {
		return sqlDatetimeSub;
	}

	public void setSqlDatetimeSub(Integer sqlDatetimeSub) {
		this.sqlDatetimeSub = sqlDatetimeSub;
	}

	public String getCharOctetLength() {
		return charOctetLength;
	}

	public void setCharOctetLength(String charOctetLength) {
		this.charOctetLength = charOctetLength;
	}

	public Integer getOrdinalPosition() {
		return ordinalPosition;
	}

	public void setOrdinalPosition(Integer ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public String getScopeCatalog() {
		return scopeCatalog;
	}

	public void setScopeCatalog(String scopeCatalog) {
		this.scopeCatalog = scopeCatalog;
	}

	public String getScopeSchema() {
		return scopeSchema;
	}

	public void setScopeSchema(String scopeSchema) {
		this.scopeSchema = scopeSchema;
	}

	public String getScopeTable() {
		return scopeTable;
	}

	public void setScopeTable(String scopeTable) {
		this.scopeTable = scopeTable;
	}

	public Short getSqlDateType() {
		return sqlDateType;
	}

	public void setSqlDateType(Short sqlDateType) {
		this.sqlDateType = sqlDateType;
	}

	public String getSourceDataType() {
		return sourceDataType;
	}

	public void setSourceDataType(String sourceDataType) {
		this.sourceDataType = sourceDataType;
	}

	public String getIsAutoincrement() {
		return isAutoincrement;
	}

	public void setIsAutoincrement(String isAutoincrement) {
		this.isAutoincrement = isAutoincrement;
	}

	public String getIsGeneratedcolumn() {
		return isGeneratedcolumn;
	}

	public void setIsGeneratedcolumn(String isGeneratedcolumn) {
		this.isGeneratedcolumn = isGeneratedcolumn;
	}


	@JsonIgnore
	public boolean updateColumn(String colname) throws Exception {
		Statement statement = this.getJdbcTable().getStatement();
		String query = "ALTER TABLE " + this.getTableName() + " CHANGE " + this.getColumnName() + " " + colname + " "
				+ this.getTypeName();
		boolean reslt = this.getJdbcTable().executeUpdate(statement, query);
		if (reslt) {
			this.setColumnName(colname);
		}
		return reslt;
	}

	@JsonIgnore
	public boolean updateColumn(String colname, String type) throws Exception {
		Statement statement = this.getJdbcTable().getStatement();
		String query = "ALTER TABLE " + this.getTableName() + " CHANGE " + this.getColumnName() + " " + colname + " "
				+ type;
		boolean reslt = this.getJdbcTable().executeUpdate(statement, query);
		if (reslt) {
			this.setColumnName(colname);
			this.setTypeName(type);
		}
		return reslt;
	}

	@JsonIgnore
	public boolean updateColumn(String colname, String type, Integer size) throws Exception {
		Statement statement = this.getJdbcTable().getStatement();
		String query = "ALTER TABLE " + this.getTableName() + " CHANGE " + this.getColumnName() + " " + colname + " "
				+ type + "(" + size + ")";
		boolean reslt = this.getJdbcTable().executeUpdate(statement, query);
		if (reslt) {
			this.setColumnName(colname);
			this.setTypeName(type);
			this.setColumnSize(size);
		}
		return reslt;
	}

	@JsonIgnore
	public boolean setDefaultColumn(Object value) throws Exception {
		Statement statement = this.getJdbcTable().getStatement();
		String query = "ALTER TABLE " + this.getTableName() + " ALTER " + this.getColumnName() + " SET DEFAULT "
				+ value;
		return this.getJdbcTable().executeUpdate(statement, query);
	}

	@JsonIgnore
	public boolean modifyColumn(String type, Integer size) throws Exception {
		Statement statement = this.getJdbcTable().getStatement();
		String query = "ALTER TABLE " + this.getTableName() + " MODIFY " + this.getColumnName() + " " + type + "("
				+ size + ")";
		boolean reslt = this.getJdbcTable().executeUpdate(statement, query);
		if (reslt) {
			this.setTypeName(type);
			this.setColumnSize(size);
		}
		return reslt;
	}

}
