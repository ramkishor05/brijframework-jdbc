package org.brijframework.jdbc.factories.data;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JbdcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.factories.meta.impl.JdbcCatalogFactoryImpl;
import org.brijframework.support.model.Assignable;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JdbcCatalogBackupFactory extends AbstractFactory<File,JbdcCatalog> implements JdbcDataFactory {
	
	private String pathname="E:\\backup";

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
		File catalogFile=new File(pathname,catalog.getTABLE_CAT());
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
			final StringWriter out =new StringWriter();
		    final ObjectMapper mapper = new ObjectMapper();
		    ObjectWriter writer= mapper.writerWithDefaultPrettyPrinter();
		    String json=writer.writeValueAsString(table.getAllRows());
		    final byte[] bytes = json.getBytes();
			Files.write(tableFile.toPath(), bytes, StandardOpenOption.WRITE);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	protected void preregister(File catalogFile, JbdcCatalog catalog) {
		
	}

	@Override
	protected void postregister(File catalogFile, JbdcCatalog catalog) {
		catalog.getTables().forEach((tableKey,table)->{
			writeFile(catalogFile, tableKey, table);
		});
	}
}
