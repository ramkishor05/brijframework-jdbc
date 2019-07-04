package org.brijframework.jdbc.factories.model.impl;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.factories.model.JdbcQueryFacory;

public class JdbcQueryFacoryImpl extends AbstractFactory<String,JdbcCatalog>  implements JdbcQueryFacory {

	@Override
	public JdbcQueryFacoryImpl loadFactory() {
		return null;
	}

	@Override
	protected void preregister(String key, JdbcCatalog value) {
		
	}

	@Override
	protected void postregister(String key, JdbcCatalog value) {
		
	}

}
