package org.brijframework.jdbc.factories.bean.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.bean.JdbcDataFactory;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.support.config.Assignable;

public class JdbcCatalogMapperFactory extends AbstractFactory<String,Map<String,String>> implements JdbcDataFactory {
	
	
	protected JdbcCatalogMapperFactory() {
	}

	protected static JdbcCatalogMapperFactory factory;

	@Assignable
	public static JdbcCatalogMapperFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogMapperFactory();
		}
		return factory;
	}

	@Override
	public Factory loadFactory() {
		JdbcCatalogModelFactoryImpl.getFactory().getCache().forEach((key,catalog)->{
			register(key, catalog);
		});
		return null;
	}

	private void register(String catalogKey, JdbcCatalog catalog) {
		String mapper_json_config=(String) getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_BEANS_MAPPER_JSON);
		if (mapper_json_config==null) {
			return ;
		}
		File catalogFile=new File(mapper_json_config,catalog.getTableCat());
		if(!catalogFile.exists()) {
			return;
		}
		catalog.getTables().forEach((tableKey,table)->{
			register(catalogFile, tableKey,table);
		});
	}
	
	private void register(File catalogFile, String tableKey, JdbcTable table) {
		File tableFile=new File(catalogFile,table.getTableName()+".json");
		if(!tableFile.exists()) {
			try {
				tableFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		/*
		 * try { final StringWriter out =new StringWriter(); final ObjectMapper mapper =
		 * new ObjectMapper(); ObjectWriter writer=
		 * mapper.writerWithDefaultPrettyPrinter(); String
		 * json=writer.writeValueAsString(table.getAllRows()); final byte[] bytes =
		 * json.getBytes(); Files.write(tableFile.toPath(), bytes,
		 * StandardOpenOption.WRITE); out.close(); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */
	}


	@Override
	protected void preregister(String key, Map<String, String> value) {
		
	}


	@Override
	protected void postregister(String key, Map<String, String> value) {
		
	}
	
}
