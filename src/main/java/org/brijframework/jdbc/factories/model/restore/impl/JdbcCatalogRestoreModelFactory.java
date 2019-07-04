package org.brijframework.jdbc.factories.model.restore.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.config.ResourcesJdbcConfig;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.model.JdbcMetaFactory;
import org.brijframework.jdbc.factories.model.back.impl.JbdcCatalogBackupModelFactory;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.jdbc.factories.model.impl.JdbcSourceFactoryImpl;
import org.brijframework.jdbc.factories.model.impl.JsonJdbcCatalogMetaFactory;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.support.config.Assignable;
import org.brijframework.support.config.DepandOn;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.InstanceUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@DepandOn(depand=JbdcCatalogBackupModelFactory.class)
public class JdbcCatalogRestoreModelFactory extends JsonJdbcCatalogMetaFactory implements JdbcMetaFactory{

	protected JdbcCatalogRestoreModelFactory() {
		
	}

	protected static JdbcCatalogRestoreModelFactory factory;

	@Assignable
	public static JdbcCatalogRestoreModelFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogRestoreModelFactory();
		}
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_RESTORE_JSON);
		if (resources==null) {
			System.err.println("Jdbc restore model configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_RESTORE_JSON);
			return null;
		}
		System.err.println("Jdbc restore model configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_RESTORE_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc restore model configration : "+resources);
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
			System.err.println("Invalid jdbc restore model configration : "+configs);
			return this;
		}
		for(ResourcesJdbcConfig modelConfig:configs) {
			System.out.println(modelConfig.getLocation());
			if(!modelConfig.isEnable()) {
				System.err.println("Jdbc restore model configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			JdbcSource source=JdbcSourceFactoryImpl.getFactory().getJdbcSource(modelConfig.getSource());
			if(source==null) {
				System.err.println("Jdbc restore model configration source not found :"+modelConfig.getSource());
				return this;
			}
			register(source,modelConfig.getCatalog(),modelConfig.getLocation());
		}
		
		return this;
	}

	private void register(JdbcSource source, String catalogKey,String fileLocation) {
		File currentPath=new File(PathUtil.getResourcesContextPath(),fileLocation);
		File catalogFile=new File(currentPath,catalogKey+".json");
		if(catalogFile.exists()) {
			ObjectMapper mapper=new ObjectMapper();
			try {
				JdbcCatalog catalog=mapper.readValue(catalogFile, new TypeReference<JdbcCatalog>() {});
				catalog.setSourceCat(source.getId());
				catalog.setSource(source);
				register(catalogFile, catalog);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void preregister(File catalogFile, JdbcCatalog catalog) {
		System.err.println("Jdbc Catalog   : "+catalog.getTableCat());
		JdbcCatalogModelFactoryImpl.getFactory().register(catalog.getTableCat(), catalog);
	}

	@Override
	protected void postregister(File catalogFile, JdbcCatalog catalog) {
		upload(catalogFile, catalog);
	}
}