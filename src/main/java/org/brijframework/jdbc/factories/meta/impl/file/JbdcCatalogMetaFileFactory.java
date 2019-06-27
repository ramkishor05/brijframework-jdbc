package org.brijframework.jdbc.factories.meta.impl.file;

import java.io.File;
import java.io.IOException;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.meta.JdbcMetaFactory;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.model.Assignable;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.location.StreamUtil;

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
			try {
				if(catalog.getSource().getConnection().getCatalog()!=null && !catalog.getSource().getConnection().getCatalog().isEmpty()) {
					register(key,catalog);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return this;
	}
	
	private void register(String key, JdbcCatalog catalog) {
		String data_backup_json_config=(String) getContainer().getContext().getProperties().get(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_META_JSON_LOCATION);
		if (data_backup_json_config==null) {
			return ;
		}
		
		File currentPath=new File(PathUtil.getResourcesContextPath(),data_backup_json_config);
		if(!currentPath.exists()) {
			currentPath.mkdirs();
		}
		File catalogFile=new File(currentPath,catalog.getTableCat()+".json");
		if(catalogFile.exists()) {
			catalogFile.delete();
		}
		try {
			catalogFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		register(catalogFile, catalog);
	}

	private void writeFile(File catalogFile, JdbcCatalog catalog) {
		
		try {
			StreamUtil.writeJsonToFile(catalogFile.toPath(), catalog);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void preregister(File key, JdbcCatalog value) {
		System.err.println("Jdbc Catalog: "+key);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		writeFile(catalogFile, catalog);
	}

}
