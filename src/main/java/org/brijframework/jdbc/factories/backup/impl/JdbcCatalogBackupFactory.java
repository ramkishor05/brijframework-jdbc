package org.brijframework.jdbc.factories.backup.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.data.JdbcDataFactory;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.location.StreamUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JdbcCatalogBackupFactory extends AbstractFactory<File,JbdcCatalog> implements JdbcDataFactory {
	
	protected JdbcCatalogBackupFactory() {
	}

	protected static JdbcCatalogBackupFactory factory;

	@Assignable
	public static JdbcCatalogBackupFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogBackupFactory();
		}
		return factory;
	}

	@Override
	public Factory loadFactory() {
		JdbcCatalogFactoryImpl.getFactory().getCache().forEach((catalogKey,catalog)->{
			register(catalogKey, catalog);
		});
		return null;
	}


	private void register(String catalogKey, JbdcCatalog catalog) {
		String data_backup_json_config=(String) getContainer().getContext().getProperties().get(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_BACKUP_JSON_LOCATION);
		if (data_backup_json_config==null) {
			return ;
		}
		
		File currentPath=new File(PathUtil.getResourcesContextPath(),data_backup_json_config);
		File catalogFile=new File(currentPath,catalog.getTABLE_CAT());
		if(!catalogFile.exists()) {
			catalogFile.mkdirs();
		}
		register(catalogFile, catalog);
	}

	private void writeFile(File catalogFile, String tableKey, JdbcTable table) {
		File tableFile=new File(catalogFile,table.getTABLE_NAME()+".json");
		if(!tableFile.exists()) {
			try {
				tableFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			StreamUtil.writeJsonToFile(tableFile.toPath(), table.getAllRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void preregister(File catalogFile, JbdcCatalog catalog) {
		System.err.println("Jdbc Backup   : "+catalogFile);
	}

	@Override
	protected void postregister(File catalogFile, JbdcCatalog catalog) {
		catalog.getTables().forEach((tableKey,table)->{
			writeFile(catalogFile, tableKey, table);
		});
	}
}
