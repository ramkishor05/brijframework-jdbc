package org.brijframework.jdbc.template;
public class JdbcQualifier<T> {
	StringBuilder qual=new StringBuilder();
	
	@SuppressWarnings("unchecked")
	public T where() {
		qual.append(" WHERE ");
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T equalTo(String key,Object value) {
		qual.append(""+ key+ " = "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T notEqualTo(String key,Object value) {
		qual.append(""+ key+ " != "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T greaterThan(String key,Object value) {
		qual.append(""+ key+ " > "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T notGreaterThan(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T greaterThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " >= "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T lessThan(String key,Object value) {
		qual.append(""+ key+ " < "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T notLessThan(String key,Object value) {
		qual.append(""+ key+ " !< "+value);
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T lessThanEqualTo(String key,Object value) {
		qual.append(""+ key+ " <= "+value);
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	public T and() {
		qual.append(" AND ");
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public T or() {
		qual.append(" OR ");
		return (T) this;
	}
	
	public String getQualifier() {
		return this.qual.toString();
	}
	
	@SuppressWarnings("unchecked")
	public T setQualifier(String qual) {
		this.qual=new StringBuilder(qual);
		return (T) this;
	}
	
}