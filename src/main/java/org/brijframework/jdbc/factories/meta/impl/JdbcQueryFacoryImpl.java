package org.brijframework.jdbc.factories.meta.impl;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.factories.meta.JdbcQueryFacory;

public class JdbcQueryFacoryImpl extends AbstractFactory<String,JbdcCatalog>  implements JdbcQueryFacory {

	@Override
	public JdbcQueryFacoryImpl loadFactory() {
		return null;
	}

	@Override
	protected void preregister(String key, JbdcCatalog value) {
		
	}

	@Override
	protected void postregister(String key, JbdcCatalog value) {
		
	}

}
