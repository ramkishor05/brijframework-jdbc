package org.brijframework.jdbc.factories.data.impl;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.data.JdbcDataFactory;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.model.Assignable;

public class JdbcMapTableFactory extends AbstractFactory<String,Map<String,String>> implements JdbcDataFactory {
	
	
	protected JdbcMapTableFactory() {
	}

	protected static JdbcMapTableFactory factory;

	@Assignable
	public static JdbcMapTableFactory getFactory() {
		if (factory == null) {
			factory = new JdbcMapTableFactory();
		}
		return factory;
	}

	@Override
	public Factory loadFactory() {
		JdbcCatalogFactoryImpl.getFactory().getCache().forEach((key,catalog)->{
			register(key, catalog);
		});
		return null;
	}

	private void register(String catalogKey, JdbcCatalog catalog) {
		String mapper_json_config=(String) getContainer().getContext().getProperties().get(JdbcConstants.APPLICATION_BOOTSTRAP_CONFIG_JDBC_DATA_MAPPER_JSON_LOCATION);
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
