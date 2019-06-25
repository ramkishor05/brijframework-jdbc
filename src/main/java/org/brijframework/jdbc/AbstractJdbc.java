package org.brijframework.jdbc;

import java.io.Serializable;

public abstract class AbstractJdbc implements Jdbc , Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;

	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}


}
