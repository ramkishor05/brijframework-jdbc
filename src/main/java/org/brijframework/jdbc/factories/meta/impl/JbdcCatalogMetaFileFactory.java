package org.brijframework.jdbc.factories.meta.impl;

import java.io.File;
import java.io.IOException;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.meta.JdbcMetaFactory;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.location.StreamUtil;

@DepandOn(depand=JdbcColumnFactoryImpl.class)
public class JbdcCatalogMetaFileFactory extends AbstractFactory<File,JdbcCatalog> implements JdbcMetaFactory{

	protected JbdcCatalogMetaFileFactory() {
	}

	protected static JbdcCatalogMetaFileFactory factory;

	@Assignable
	public static JbdcCatalogMetaFileFactory getFactory() {
		if (factory == null) {
			factory = new JbdcCatalogMetaFileFactory();
		}
		return factory;
	}
	
	@Override
	public JbdcCatalogMetaFileFactory loadFactory() {
		JdbcCatalogFactoryImpl.getFactory().getCache().forEach((key,catalog)->{
			register(key,catalog);
		});
		return this;
	}
	
	private void register(String key, JdbcCatalog catalog) {
		String data_backup_json_config=(String) getContainer().getContext().getProperties().get(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_META_JSON_LOCATION);
		if (data_backup_json_config==null) {
			return ;
		}
		
		File currentPath=new File(PathUtil.getResourcesContextPath(),data_backup_json_config);
		File catalogFile=new File(currentPath,catalog.getTableCat());
		if(!catalogFile.exists()) {
			catalogFile.mkdirs();
		}
		register(catalogFile, catalog);
	}

	private void writeFile(File catalogFile, String tableKey, JdbcTable table) {
		File tableFile=new File(catalogFile,table.getTableName()+".json");
		if(!tableFile.exists()) {
			try {
				tableFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			StreamUtil.writeJsonToFile(tableFile.toPath(), table);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void preregister(File key, JdbcCatalog value) {
		System.err.println("Jdbc TableCatalog: "+key);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		catalog.getTables().forEach((tableKey,table)->{
			writeFile(catalogFile, tableKey, table);
		});
	}

}
