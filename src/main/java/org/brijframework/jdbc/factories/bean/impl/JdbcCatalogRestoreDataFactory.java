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
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.InstanceUtil;

@DepandOn(depand=JdbcCatalogBackupDataFactory.class)
public class JdbcCatalogRestoreDataFactory extends JsonJdbcCatalogDataFactory implements JdbcDataFactory {
	
	protected JdbcCatalogRestoreDataFactory() {
	}

	protected static JdbcCatalogRestoreDataFactory factory;

	@Assignable
	public static JdbcCatalogRestoreDataFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogRestoreDataFactory();
		}
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_RESTORE_JSON);
		if (resources==null) {
			System.err.println("Jdbc restore beans configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_RESTORE_JSON);
			return null;
		}
		System.err.println("Jdbc restore beans configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_RESTORE_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc restore beans configration : "+resources);
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
			System.err.println("Invalid jdbc restore beans configration : "+configs);
			return this;
		}
		for(ResourcesJdbcConfig modelConfig:configs) {
			System.out.println(modelConfig.getLocation());
			if(!modelConfig.isEnable()) {
				System.err.println("Jdbc restore configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			JdbcCatalog catalog=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(modelConfig.getCatalog());
			if(catalog==null) {
				System.err.println("Jdbc restore configration not found :"+modelConfig.getCatalog());
				continue;
			}
			register(modelConfig.getCatalog(), catalog,modelConfig.getLocation());
		}
		return this;
	}

	public void register(String catalogKey, JdbcCatalog catalog,String fileLocation) {
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
		catalog.getTables().forEach((tableKey,table)->{
			upload(catalogFile, tableKey, table);
		});
	}
}
