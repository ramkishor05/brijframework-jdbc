package org.brijframework.jdbc.factories.restore.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.backup.impl.JdbcCatalogBackupFactory;
import org.brijframework.jdbc.factories.data.JdbcDataFactory;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.model.Assignable;
import org.brijframework.support.model.DepandOn;
import org.brijframework.util.location.PathUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DepandOn(depand=JdbcCatalogBackupFactory.class)
public class JdbcCatalogRestoreFactory extends AbstractFactory<File,JbdcCatalog> implements JdbcDataFactory {
	
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
		System.out.println("catalogenable="+catalogenable);
		if(catalogenable!=null && (catalogenable.equalsIgnoreCase("Y") || Boolean.valueOf(catalogenable) ==true || catalogenable.equalsIgnoreCase("true")|| catalogenable.equalsIgnoreCase("1"))) {
			String catalogKey=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_RESTORE_JSON_CATALOG);
			System.out.println("catalogKey="+catalogKey);
			if (catalogKey==null) {
				return this;
			}
			JbdcCatalog catalog=JdbcCatalogFactoryImpl.getFactory().getJbdcCatalog(catalogKey);
			System.out.println("catalog="+catalog);
			if(catalog==null) {
				return this;
			}
			register(catalogKey, catalog);
		}
		
		return this;
	}


	private void register(String catalogKey, JbdcCatalog catalog) {
		String data_backup_json_config=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_RESTORE_JSON_LOCATION);
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
			return;
		}
		ObjectMapper mapper = new ObjectMapper();
		try (FileInputStream reader = new FileInputStream(tableFile)) {
			List<Map<String, Object>> lst = mapper.readValue(reader, new TypeReference<List<Map<String, Object>>>() {});
			lst.forEach(entityModel -> {
				try {
					table.addRow(entityModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void preregister(File catalogFile, JbdcCatalog catalog) {
		System.err.println("Jdbc Restore   : "+catalogFile);
	}

	@Override
	protected void postregister(File catalogFile, JbdcCatalog catalog) {
		System.out.println(catalog.getTables());
		catalog.getTables().forEach((tableKey,table)->{
			System.out.println("tableKey");
			writeFile(catalogFile, tableKey, table);
		});
	}
}
