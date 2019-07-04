package org.brijframework.jdbc.factories.bean.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.util.JdbcBeanUtil;
import org.brijframework.util.location.StreamUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonJdbcCatalogDataFactory extends AbstractFactory<File,JdbcCatalog>{

	public void upload(File catalogFile, String tableKey, JdbcTable table) {
		if(!catalogFile.exists()) {
			return;
		}
		File tableFile=new File(catalogFile,table.getTableName()+".json");
		ObjectMapper mapper = new ObjectMapper();
		try (FileInputStream reader = new FileInputStream(tableFile)) {
			List<Map<String, Object>> lst = mapper.readValue(reader, new TypeReference<List<Map<String, Object>>>() {});
			lst.forEach(entityModel -> {
				try {
					JdbcBeanUtil.addObject(table,entityModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void download(File catalogFile, String tableKey, JdbcTable table) {
		File tableFile=new File(catalogFile,table.getTableName()+".json");
		if(!tableFile.exists()) {
			try {
				tableFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			StreamUtil.writeJsonToFile(tableFile.toPath(), JdbcBeanUtil.getAllRows(table));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
