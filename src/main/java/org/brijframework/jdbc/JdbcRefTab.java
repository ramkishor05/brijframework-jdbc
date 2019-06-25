package org.brijframework.jdbc;

public class JdbcRefTab{
	private String id;
	private String pktableCat; // String => primary key table catalog (may be null) 
	private String pktableSchem; //String => primary key table schema (may be null) 
	private String pktableName; //String => primary key table name 
	private String pkcolumnName; //String => primary key column name 
	private String fktableCat; //String => foreign key table catalog (may be null) being exported (may be null) 
	private String fktableSchem; //String => foreign key table schema (may be null) being exported (may be null) 
	private String fktableName; //String => foreign key table name being exported 
	private String fkcolumnName; // String => foreign key column name being exported 
	private String keySeq;// short => sequence number within foreign key( a value of 1 represents the first column of the foreign key, a value of 2 would represent the second column within the foreign key). 
	private String updateRule;// short => What happens to foreign key when primary is updated: ◦ importedNoAction - do not allow update of primary key if it has been imported 
	/*		◦ importedKeyCascade - change imported key to agree with primary key update 
			◦ importedKeySetNull - change imported key to NULL if its primary key has been updated 
			◦ importedKeySetDefault - change imported key to default values if its primary key has been updated 
			◦ importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
    */
	private String deleteRule; // short => What happens to the foreign key when primary is deleted. ◦ importedKeyNoAction - do not allow delete of primary key if it has been imported 
	/*	◦ importedKeyCascade - delete rows that import a deleted key 
		◦ importedKeySetNull - change imported key to NULL if its primary key has been deleted 
		◦ importedKeyRestrict - same as importedKeyNoAction (for ODBC 2.x compatibility) 
		◦ importedKeySetDefault - change imported key to default if its primary key has been deleted 
	*/

	private String fkName; // String => foreign key name (may be null) 
	private String pkName; //String => primary key name (may be null) 
	private String deferrability; //short => can the evaluation of foreign key constraints be deferred until commit ◦ importedKeyInitiallyDeferred - see SQL92 for definition 
	/*	◦ importedKeyInitiallyImmediate - see SQL92 for definition 
		◦ importedKeyNotDeferrable - see SQL92 for definition 
	*/

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPktableCat() {
		return pktableCat;
	}

	public void setPktableCat(String pktableCat) {
		this.pktableCat = pktableCat;
	}

	public String getPktableSchem() {
		return pktableSchem;
	}

	public void setPktableSchem(String pktableSchem) {
		this.pktableSchem = pktableSchem;
	}

	public String getPktableName() {
		return pktableName;
	}

	public void setPktableName(String pktableName) {
		this.pktableName = pktableName;
	}

	public String getPkcolumnName() {
		return pkcolumnName;
	}

	public void setPkcolumnName(String pkcolumnName) {
		this.pkcolumnName = pkcolumnName;
	}

	public String getFktableCat() {
		return fktableCat;
	}

	public void setFktableCat(String fktableCat) {
		this.fktableCat = fktableCat;
	}

	public String getFktableSchem() {
		return fktableSchem;
	}

	public void setFktableSchem(String fktableSchem) {
		this.fktableSchem = fktableSchem;
	}

	public String getFktableName() {
		return fktableName;
	}

	public void setFktableName(String fktableName) {
		this.fktableName = fktableName;
	}

	public String getFkcolumnName() {
		return fkcolumnName;
	}

	public void setFkcolumnName(String fkcolumnName) {
		this.fkcolumnName = fkcolumnName;
	}

	public String getKeySeq() {
		return keySeq;
	}

	public void setKeySeq(String keySeq) {
		this.keySeq = keySeq;
	}

	public String getUpdateRule() {
		return updateRule;
	}

	public void setUpdateRule(String updateRule) {
		this.updateRule = updateRule;
	}

	public String getDeleteRule() {
		return deleteRule;
	}

	public void setDeleteRule(String deleteRule) {
		this.deleteRule = deleteRule;
	}

	public String getFkName() {
		return fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getDeferrability() {
		return deferrability;
	}

	public void setDeferrability(String deferrability) {
		this.deferrability = deferrability;
	}
	
}
