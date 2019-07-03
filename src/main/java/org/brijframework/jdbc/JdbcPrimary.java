package org.brijframework.jdbc;

public class JdbcPrimary extends AbstractJdbc{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tableCat;// String => table catalog (may be null)
	String tableSchem; // String => table schema (may be null)
	String tableName; // String => table name
	String columnName;// String => column name
	short keySeq; // KEY_SEQ => sequence number within primary key( a value of 1 represents the
					// first column of the primary key, a value of 2 would represent the second
					// column within the primary key). primary
	String pkName; // String => primary key name (may be null)

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

	public short getKeySeq() {
		return keySeq;
	}

	public void setKeySeq(short keySeq) {
		this.keySeq = keySeq;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public void setId(String columnName2) {
		// TODO Auto-generated method stub
		
	}
}
