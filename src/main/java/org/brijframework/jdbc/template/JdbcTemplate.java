package org.brijframework.jdbc.template;

import java.util.List;
import java.util.Map;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.asserts.Assertion;

public class JdbcTemplate {
	
    private JdbcCatalog catalog;
    
	public JdbcTemplate(String catalogId) {
		this.catalog=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(catalogId);
		Assertion.notNull(this.catalog, "No such type of catalog found for given id :"+catalogId);
	} 
	
	public JdbcCatalog getCatalog() {
		return catalog;
	}
	
	public List<Map<String, Object>> fatch(String tableId){
		JdbcTable table= getCatalog().getTables().get(tableId);
		Assertion.notNull(table, "No such type of table found for given id :"+tableId);
		try {
			return JdbcBeanUtil.getAllRows(table);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean add(String tableId,Map<String, Object> params){
		JdbcTable table= getCatalog().getTables().get(tableId);
		Assertion.notNull(table, "No such type of table found for given id :"+tableId);
		try {
			return JdbcBeanUtil.addObject(table,params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	

	public JdbcUpdate update(String tableId) {
		JdbcTable table= getCatalog().getTables().get(tableId);
		Assertion.notNull(table, "No such type of table found for given id :"+tableId);
		return null;
	}
	
	public JdbcStatement statement() {
		return new JdbcStatement(catalog);
	}
}