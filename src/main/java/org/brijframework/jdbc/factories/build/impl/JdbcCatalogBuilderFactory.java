package org.brijframework.jdbc.factories.build.impl;

import java.io.File;
import java.io.IOException;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.build.JdbcBuilderFactory;
import org.brijframework.jdbc.factories.source.impl.JdbcSourceFactoryImpl;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.support.Constants;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JdbcCatalogBuilderFactory extends AbstractFactory<File, JdbcCatalog> implements JdbcBuilderFactory {

	protected JdbcCatalogBuilderFactory() {
		
	}

	protected static JdbcCatalogBuilderFactory factory;

	@Assignable
	public static JdbcCatalogBuilderFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogBuilderFactory();
		}
		return factory;
	}
	
	@Override
	public Factory loadFactory() {
		String catalogenable=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_BUILD_JSON_ENABLE);
		if(catalogenable!=null && (catalogenable.equalsIgnoreCase("Y") || Boolean.valueOf(catalogenable) ==true || catalogenable.equalsIgnoreCase("true")|| catalogenable.equalsIgnoreCase("1"))) {
			String catalog=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_BUILD_JSON_CATALOG);
			if (catalog==null) {
				return this;
			}
			String sourceKey=catalog.split(Constants.SPLIT_DOT)[0];
			String catalogKey=catalog.split(Constants.SPLIT_DOT)[1];
			JdbcSource source=JdbcSourceFactoryImpl.getFactory().getJdbcSource(sourceKey);
			if(source==null) {
				return this;
			}
			register(source, catalogKey);
		}
		return null;
	}

	private void register(JdbcSource source, String catalogKey) {
		String catalog_build_json_config=getProperty(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_BUILD_JSON_LOCATION);
		if (catalog_build_json_config==null) {
			return ;
		}
		File currentPath=new File(PathUtil.getResourcesContextPath(),catalog_build_json_config);
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
	protected void preregister(File key, JdbcCatalog catalog) {
		System.err.println("Jdbc Catalog   : "+catalog.getTableCat());
	}

	@Override
	protected void postregister(File key, JdbcCatalog catalog) {
		try {
			catalog.makeCatalog();
			catalog.getTables().forEach((tableKey,table)->{
				try {
					table.setCatalog(catalog);
					if(table.getTableCat()==null) {
						table.setTableCat(catalog.getTableCat());
					}
					table.makeTable();
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}