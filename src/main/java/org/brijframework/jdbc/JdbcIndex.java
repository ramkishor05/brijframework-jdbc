package org.brijframework.jdbc;

public class JdbcIndex extends AbstractJdbc{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String tableCat;//  => table catalog (may be null) 
    String tableSchem; //  => table schema (may be null) 
	String tableName; // String => table name 
	boolean nonUnique; //NON_UNIQUE  => Can index values be non-unique. false when TYPE is tableIndexStatistic 
	String indexQualifier; //INDEX_QUALIFIER String => index catalog (may be null); null when TYPE is tableIndexStatistic 
	String indexName; //INDEX_NAME String => index name; null when TYPE is tableIndexStatistic 
	short type; //TYPE short => index type: 
			//tableIndexStatistic - this identifies table statistics that are returned in conjuction with a table's index descriptions 
			//tableIndexClustered - this is a clustered index 
			//tableIndexHashed - this is a hashed index 
			//tableIndexOther - this is some other style of index 
	short ordinalPosition;//ORDINAL_POSITION short => column sequence number within index; zero when TYPE is tableIndexStatistic 
	String columnName; //	COLUMN_NAME String => column name; null when TYPE is tableIndexStatistic 
	String ascOrDesc;//ASC_OR_DESC String => column sort sequence, "A" => ascending, "D" => descending, may be null if sort sequence is not supported; null when TYPE is tableIndexStatistic 
	long cardinality;//CARDINALITY long => When TYPE is tableIndexStatistic, then this is the number of rows in the table; otherwise, it is the number of unique values in the index. 
	long pages; //	PAGES long => When TYPE is tableIndexStatisic then this is the number of pages used for the table, otherwise it is the number of pages used for the current index. 
	String filterCondition;//	FILTER_CONDITION String => Filter condition, if any. (may be null) 
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
	public boolean isNonUnique() {
		return nonUnique;
	}
	public void setNonUnique(boolean nonUnique) {
		this.nonUnique = nonUnique;
	}
	public String getIndexQualifier() {
		return indexQualifier;
	}
	public void setIndexQualifier(String indexQualifier) {
		this.indexQualifier = indexQualifier;
	}
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public short getType() {
		return type;
	}
	public void setType(short type) {
		this.type = type;
	}
	public short getOrdinalPosition() {
		return ordinalPosition;
	}
	public void setOrdinalPosition(short ordinalPosition) {
		this.ordinalPosition = ordinalPosition;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getAscOrDesc() {
		return ascOrDesc;
	}
	public void setAscOrDesc(String ascOrDesc) {
		this.ascOrDesc = ascOrDesc;
	}
	public long getCardinality() {
		return cardinality;
	}
	public void setCardinality(long cardinality) {
		this.cardinality = cardinality;
	}
	public long getPages() {
		return pages;
	}
	public void setPages(long pages) {
		this.pages = pages;
	}
	public String getFilterCondition() {
		return filterCondition;
	}
	public void setFilterCondition(String filterCondition) {
		this.filterCondition = filterCondition;
	}

}
