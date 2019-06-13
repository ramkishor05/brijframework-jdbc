package org.brijframework.jdbc.constants;

public enum JdbcMeta {

	TABLE("TABLE"), 
	VIEW("VIEW"), 
	SYSTEM_TABLE("SYSTEM TABLE"), 
	GLOBAL_TEMPORARY("GLOBAL TEMPORARY"),
	LOCAL_TEMPORARY("LOCAL TEMPORARY"), 
	ALIAS("ALIAS"), 
	SYNONYM("SYNONYM");

	private String type;

	private JdbcMeta(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}
}
