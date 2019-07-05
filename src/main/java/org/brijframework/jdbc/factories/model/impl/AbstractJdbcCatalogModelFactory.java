package org.brijframework.jdbc.factories.model.impl;

import java.io.File;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.location.StreamUtil;

public abstract class AbstractJdbcCatalogModelFactory extends AbstractFactory<File,JdbcCatalog>{

	public void build(File catalogFile, JdbcCatalog catalog) {
		try {
			catalog.makeCatalog();
			catalog.getTables().forEach((tableKey,table)->{
				try {
					table.setCatalog(catalog);
					if(table.getTableCat()==null) {
						table.setTableCat(catalog.getTableCat());
					}
					JdbcBeanUtil.makeTable(table);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void upload(File catalogFile, JdbcCatalog catalog) {
		try {
			catalog.makeCatalog();
			catalog.getTables().forEach((tableKey,table)->{
				try {
					table.setCatalog(catalog);
					if(table.getTableCat()==null) {
						table.setTableCat(catalog.getTableCat());
					}
					JdbcBeanUtil.makeTable(table);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void download(File catalogFile, JdbcCatalog catalog) {
		StreamUtil.writeJsonToFile(catalogFile.toPath(), catalog);
	}

}
