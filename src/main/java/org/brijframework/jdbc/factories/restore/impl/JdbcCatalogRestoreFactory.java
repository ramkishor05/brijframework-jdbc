package org.brijframework.jdbc.factories.restore.impl;

import java.io.File;

import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.backup.impl.JdbcCatalogBackupFactory;
import org.brijframework.jdbc.factories.data.JdbcDataFactory;
import org.brijframework.jdbc.factories.data.impl.JsonJdbcCatalogDataFactory;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.location.PathUtil;

@DepandOn(depand=JdbcCatalogBackupFactory.class)
public class JdbcCatalogRestoreFactory extends JsonJdbcCatalogDataFactory implements JdbcDataFactory {
	
	protected JdbcCatalogRestoreFactory() {
	}

	protected static JdbcCatalogRestoreFactory factory;

	@Assignable
	public static JdbcCatalogRestoreFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogRestoreFactory();
		}
		return factory;
	}

	@Override
	public Factory loadFactory() {
		String catalogenable=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_RESTORE_JSON_ENABLE);
		if(catalogenable!=null && (catalogenable.equalsIgnoreCase("Y") || Boolean.valueOf(catalogenable) ==true || catalogenable.equalsIgnoreCase("true")|| catalogenable.equalsIgnoreCase("1"))) {
			String catalogKey=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_RESTORE_JSON_CATALOG);
			if (catalogKey==null) {
				return this;
			}
			JdbcCatalog catalog=JdbcCatalogFactoryImpl.getFactory().getJbdcCatalog(catalogKey);
			if(catalog==null) {
				return this;
			}
			register(catalogKey, catalog);
		}
		return this;
	}


	public void register(String catalogKey, JdbcCatalog catalog) {
		String data_backup_json_config=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_RESTORE_JSON_LOCATION);
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

	@Override
	protected void preregister(File catalogFile, JdbcCatalog catalog) {
		System.err.println("Jdbc Restore   : "+catalogFile);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		catalog.getTables().forEach((tableKey,table)->{
			writeFile(catalogFile, tableKey, table);
		});
	}
}
