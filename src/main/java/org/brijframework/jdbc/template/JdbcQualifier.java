package org.brijframework.jdbc.template;
public class JdbcQualifier {
	StringBuilder qual=new StringBuilder();
	
	public JdbcQualifier where() {
		qual.append(" WHERE ");
		return this;
	}

	public JdbcQualifier equalTo(String key,Object value) {
		qual.append(""+ key+ " = "+value);
		return this;
	}
	
	public JdbcQualifier notEqualTo(String key,Object value) {
		qual.append(""+ key+ " != "+value);
		return this;
	}
	
	public JdbcQualifier greaterThan(String key,Object value) {
		qual.append(""+ key+ " > "+value);
		return this;
	}
	
	public JdbcQualifier notGreaterThan(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcQualifier greaterThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return this;
	}
	
	public JdbcQualifier lessThan(String key,Object value) {
		qual.append(""+ key+ " < "+value);
		return this;
	}
	
	public JdbcQualifier notLessThan(String key,Object value) {
		qual.append(""+ key+ " !< "+value);
		return this;
	}
	
	public JdbcQualifier lessThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " <= "+value);
		return this;
	}

	public JdbcQualifier and() {
		qual.append(" AND ");
		return this;
	}
	
	public JdbcQualifier or() {
		qual.append(" OR ");
		return this;
	}
	
	public String getQualifier() {
		return this.qual.toString();
	}
}