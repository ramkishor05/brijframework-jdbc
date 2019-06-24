package org.brijframework.jdbc;

public abstract class AbstractJdbc implements Jdbc {
	
	private String id;

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


}
