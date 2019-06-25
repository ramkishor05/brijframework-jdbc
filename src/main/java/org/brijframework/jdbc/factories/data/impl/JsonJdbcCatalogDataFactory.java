package org.brijframework.jdbc.factories.data.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonJdbcCatalogDataFactory extends AbstractFactory<File,JdbcCatalog>{


	public void writeFile(File catalogFile, String tableKey, JdbcTable table) {
		File tableFile=new File(catalogFile,table.getTableName()+".json");
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

}
