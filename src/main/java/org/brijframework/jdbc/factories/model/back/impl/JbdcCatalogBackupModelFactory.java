package org.brijframework.jdbc.factories.model.back.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.config.ResourcesJdbcConfig;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.model.JdbcMetaFactory;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.jdbc.factories.model.impl.JsonJdbcCatalogMetaFactory;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.InstanceUtil;

public class JbdcCatalogBackupModelFactory extends JsonJdbcCatalogMetaFactory implements JdbcMetaFactory{

	protected JbdcCatalogBackupModelFactory() {
	}

	protected static JbdcCatalogBackupModelFactory factory;

	@Assignable
	public static JbdcCatalogBackupModelFactory getFactory() {
		if (factory == null) {
			factory = new JbdcCatalogBackupModelFactory();
		}
		return factory;
	}
	

	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_BACKUP_JSON);
		if (resources==null) {
			System.err.println("Jdbc back model configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_BACKUP_JSON);
			return null;
		}
		System.err.println("Jdbc back model configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_BACKUP_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc back model configration : "+resources);
			return null;
		}
	}
	
	private List<ResourcesJdbcConfig> build(Map<String, Object> resource) {
		List<ResourcesJdbcConfig> configs=new ArrayList<ResourcesJdbcConfig>();
		configs.add(InstanceUtil.getInstance(ResourcesJdbcConfig.class, resource));
		return configs;
	}

	private List<ResourcesJdbcConfig> build(List<Map<String, Object>> resources) {
		List<ResourcesJdbcConfig> configs=new ArrayList<ResourcesJdbcConfig>();
		for(Map<String, Object> resource:resources) {
			configs.add(InstanceUtil.getInstance(ResourcesJdbcConfig.class, resource));
		}
		return configs;
	}
	
	
	@Override
	public JbdcCatalogBackupModelFactory loadFactory() {
		List<ResourcesJdbcConfig> configs=configration();
		if(configs==null) {
			System.err.println("Invalid jdbc back model configration : "+configs);
		    return this;
		}
		for(ResourcesJdbcConfig modelConfig:configs) {
			System.out.println(modelConfig.getLocation());
			if(!modelConfig.isEnable()) {
				System.err.println("Jdbc back model configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			JdbcCatalog catalog=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(modelConfig.getCatalog());
			if(catalog==null) {
				return this;
			}
			register(modelConfig.getCatalog(),catalog,modelConfig.getLocation());
		}
		return this;
	}
	
	
	
	private void register(String key, JdbcCatalog catalog,String fileLocation) {
		File currentPath=new File(PathUtil.getResourcesContextPath(),fileLocation);
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

	@Override
	protected void preregister(File key, JdbcCatalog value) {
		System.err.println("Jdbc catalog: "+key);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		download(catalogFile, catalog);
	}

}
