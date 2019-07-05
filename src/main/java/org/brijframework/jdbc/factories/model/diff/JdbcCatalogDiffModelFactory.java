package org.brijframework.jdbc.factories.model.diff;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.brijframework.factories.Factory;
import org.brijframework.jdbc.JdbcCatalog;
import org.brijframework.jdbc.JdbcColumn;
import org.brijframework.jdbc.JdbcTable;
import org.brijframework.jdbc.config.ResourcesJdbcDiffConfig;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.model.JdbcMetaFactory;
import org.brijframework.jdbc.factories.model.impl.AbstractJdbcCatalogModelFactory;
import org.brijframework.jdbc.factories.model.impl.JdbcCatalogModelFactoryImpl;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.accessor.PropertyAccessorUtil;
import org.brijframework.util.location.PathUtil;
import org.brijframework.util.reflect.FieldUtil;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.support.Access;

public class JdbcCatalogDiffModelFactory extends AbstractJdbcCatalogModelFactory implements JdbcMetaFactory {

	protected JdbcCatalogDiffModelFactory() {
	}

	protected static JdbcCatalogDiffModelFactory factory;

	@Assignable
	public static JdbcCatalogDiffModelFactory getFactory() {
		if (factory == null) {
			factory = new JdbcCatalogDiffModelFactory();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcDiffConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_DIFF_JSON);
		if (resources==null) {
			System.err.println("Jdbc model diff configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_DIFF_JSON);
			return null;
		}
		System.err.println("Jdbc model diff configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_JDBC_MODEL_DIFF_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc model diff configration : "+resources);
			return null;
		}
	}
	
	private List<ResourcesJdbcDiffConfig> build(Map<String, Object> resource) {
		List<ResourcesJdbcDiffConfig> configs=new ArrayList<ResourcesJdbcDiffConfig>();
		configs.add(InstanceUtil.getInstance(ResourcesJdbcDiffConfig.class, resource));
		return configs;
	}

	private List<ResourcesJdbcDiffConfig> build(List<Map<String, Object>> resources) {
		List<ResourcesJdbcDiffConfig> configs=new ArrayList<ResourcesJdbcDiffConfig>();
		for(Map<String, Object> resource:resources) {
			configs.add(InstanceUtil.getInstance(ResourcesJdbcDiffConfig.class, resource));
		}
		return configs;
	}
	
	
	@Override
	public Factory loadFactory() {
		List<ResourcesJdbcDiffConfig> configs=configration();
		if(configs==null) {
			System.err.println("Invalid jdbc model diff configration : "+configs);
			return this;
		}
		for(ResourcesJdbcDiffConfig modelConfig:configs) {
			System.out.println(modelConfig.getLocation());
			if(!modelConfig.isEnable()) {
				System.err.println("Jdbc diff beans configration disabled found :"+modelConfig.getLocation());
				continue;
			}
			JdbcCatalog catalogLeft=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(modelConfig.getCatalogLeft());
			if(catalogLeft==null) {
				System.err.println("Jdbc diff invalid catalog left configration :"+modelConfig.getCatalogLeft());
				return this;
			}
			JdbcCatalog catalogRight=JdbcCatalogModelFactoryImpl.getFactory().getJbdcCatalog(modelConfig.getCatalogRight());
			if(catalogRight==null) {
				System.err.println("Jdbc diff invalid catalog rigth configration :"+modelConfig.getCatalogRight());
				return this;
			}
			register(catalogLeft.getId(), catalogLeft,catalogRight.getId(), catalogRight, modelConfig.getLocation());
		}
		return null;
	}


	private void register(String left, JdbcCatalog leftCatalog, String right, JdbcCatalog rightCatalog, String location) {
		JdbcCatalog catalog=new JdbcCatalog();
		catalog.setTableCat(rightCatalog.getTableCat());
		rightCatalog.getTables().forEach((rightTableKey,rightTable)->{
			JdbcTable leftTabe=leftCatalog.getTables().get(rightTableKey);
			if(left!=null) {
				boolean diffTable=false;
				JdbcTable jdbcTable=compareTable(diffTable, leftTabe, rightTable);
				if(diffTable) {
					catalog.getTables().put(jdbcTable.getTableName(), jdbcTable);
				}
			}
		});
		register(catalog.getTableCat(), catalog,location);
	}
	
	private JdbcTable compareTable(boolean diffTable,JdbcTable left,JdbcTable right) {
		JdbcTable jdbcTable=new JdbcTable();
		jdbcTable.setCatalog(right.getCatalog());
		jdbcTable.setTableName(right.getTableName());
		for(Entry<String, JdbcColumn> entry:right.getColumns().entrySet()) {
			JdbcColumn leftColm=left.getColumn(entry.getKey());
			if(leftColm!=null) {
				boolean diffColm=false;
				JdbcColumn colm=compareColmn(diffColm, leftColm, entry.getValue());
				if(diffColm) {
					jdbcTable.getColumns().put(colm.getColumnName(), colm);
					diffTable=true;
				}
			}else {
				diffTable=true;
				jdbcTable.getColumns().put(entry.getKey(), entry.getValue());
			}
		}
		return jdbcTable;
	}
	
	private JdbcColumn compareColmn(boolean diffCol,JdbcColumn leftColm,JdbcColumn rightColm) {
		JdbcColumn column=new JdbcColumn();
		column.setTableName(rightColm.getTableName());
		for(Field rightField:FieldUtil.getAllField(JdbcColumn.class,Access.PRIVATE_NO_STATIC_FINAL)) {
			Object leftValue =PropertyAccessorUtil.getProperty(leftColm, rightField);
			Object rightValue =PropertyAccessorUtil.getProperty(leftColm, rightField);
			if(leftValue!=null && rightValue!=null) {
				if(!leftValue.equals(rightValue)) {
					PropertyAccessorUtil.setProperty(column, rightField,rightValue);
					diffCol=true;
				}
			}else {
				if(leftValue==null && rightValue!=null) {
					PropertyAccessorUtil.setProperty(column, rightField,rightValue);
					diffCol=true;
				}
			}
		}
		return column;
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
