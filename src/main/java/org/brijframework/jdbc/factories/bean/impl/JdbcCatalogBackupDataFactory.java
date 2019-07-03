package org.brijframework.jdbc.factories.bean.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.config.ResourcesJdbcConfig;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.bean.JdbcDataFactory;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.InstanceUtil;

public class JdbcCatalogBackupDataFactory extends JsonJdbcCatalogDataFactory implements JdbcDataFactory {
	
	protected JdbcCatalogBackupDataFactory() {
	}

	protected static JdbcCatalogBackupDataFactory factory;

	@Assignable
	public static JdbcCatalogBackupDataFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogBackupDataFactory();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_BACKUP_JSON);
		if (resources==null) {
			System.err.println("Jdbc back beans configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_BACKUP_JSON);
			return null;
		}
		System.err.println("Jdbc back beans configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_BACKUP_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc back beans configration : "+resources);
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
	public Factory loadFactory() {
		List<ResourcesJdbcConfig> configs=configration();
		if(configs==null) {
			System.err.println("Invalid jdbc back beans configration : "+configs);
			return this;
		}
		for(ResourcesJdbcConfig modelConfig:configs) {
			System.out.println(modelConfig.getLocation());
			if(!modelConfig.isEnable()) {
				System.err.println("Jdbc back beans configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			JdbcCatalog catalog=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(modelConfig.getCatalog());
			if(catalog==null) {
				return this;
			}
			register(catalog.getId(), catalog, modelConfig.getLocation());
		}
		return null;
	}


	private void register(String catalogKey, JdbcCatalog catalog,String fileLocation) {
		File currentPath=new File(PathUtil.getResourcesContextPath(),fileLocation);
		File catalogFile=new File(currentPath,catalog.getTableCat());
		if(!catalogFile.exists()) {
			catalogFile.mkdirs();
		}
		register(catalogFile, catalog);
	}

	

	@Override
	protected void preregister(File catalogFile, JdbcCatalog catalog) {
		System.err.println("Jdbc catalog   : "+catalogFile);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		try {
			catalog.makeCatalog();
		} catch (Exception e) {
			e.printStackTrace();
		}
		catalog.getTables().forEach((tableKey,table)->{
			download(catalogFile, tableKey, table);
		});
	}
}
