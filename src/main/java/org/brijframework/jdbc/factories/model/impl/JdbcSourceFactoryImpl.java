package org.brijframework.jdbc.factories.model.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.brijframework.asm.factories.AbstractFactory;
import org.brijframework.bean.factories.impl.BeanFactory;
import org.brijframework.bean.factories.json.JsonBeanInfoFactory;
import org.brijframework.bean.info.BeanInfo;
import org.brijframework.jdbc.config.ResourcesJdbcConfig;
import org.brijframework.jdbc.constants.JdbcConstants;
import org.brijframework.jdbc.factories.model.JdbcSourceFactory;
import org.brijframework.jdbc.source.JdbcSource;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.asserts.Assertion;
import org.brijframework.util.reflect.InstanceUtil;

public class JdbcSourceFactoryImpl extends AbstractFactory<String,JdbcSource> implements JdbcSourceFactory<String,JdbcSource>{
	
	private static JdbcSourceFactoryImpl factory;
	
	@Assignable
	public static JdbcSourceFactoryImpl getFactory() {
		if(factory==null) {
			factory=new JdbcSourceFactoryImpl();
		}
		return factory;
	}

	@SuppressWarnings("unchecked")
	public List<ResourcesJdbcConfig> configration() {
		Object resources=getContainer().getContext().getProperties().get(JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_SOURCE_BEANS_JSON);
		if (resources==null) {
			System.err.println("Jdbc source configration not found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_SOURCE_BEANS_JSON);
			return null;
		}
		System.err.println("Jdbc source configration found :"+JdbcConstants.DATASOURCE_BOOTSTRAP_CONFIG_SOURCE_BEANS_JSON+" | "+resources);
		if(resources instanceof List) {
			return build((List<Map<String, Object>>)resources);
		}else if(resources instanceof Map) {
			return build((Map<String, Object>)resources);
		}else {
			System.err.println("Invalid jdbc source configration : "+resources);
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
	public JdbcSource getJdbcSource(String jdbcSource) {
		return getCache().get(jdbcSource);
	}
	
	@Override
	public Connection getConnection(String datasource) throws SQLException {
		JdbcSource source=getJdbcSource(datasource);
		Assertion.notNull(source, "Source not found for given id :"+datasource);
		Assertion.notNull(source.getDataSource(), "Data source not found for given id :"+datasource);
		return source.getDataSource().getConnection();
	}

	@SuppressWarnings("unused")
	@Override
	public JdbcSourceFactoryImpl loadFactory() {
		List<ResourcesJdbcConfig> configs=configration();
		if(configs==null) {
			System.err.println("Invalid jdbc source configration : "+configs);
			return this;
		}
		for(ResourcesJdbcConfig modelConfig:configs) {
			List<BeanInfo> owners= JsonBeanInfoFactory.getFactory().getBeanInfoList(DataSource.class);
			for(BeanInfo owner:owners) {
				DataSource dataSource=BeanFactory.getFactory().getModel(owner);
				if(dataSource!=null) {
					register(owner.getId(),owner, dataSource);
				}	
			}
		}
		return this;
	}

	public void register(String id,BeanInfo owner, DataSource dataSource) {
		JdbcSource source=new JdbcSource();
		source.setId(id);
		source.setDataSource(dataSource);
		register(id, source);
	}

	@Override
	protected void preregister(String key, JdbcSource value) {
		System.err.println("Jdbc Source  : "+key);
	}

	@Override
	protected void postregister(String key, JdbcSource value) {
		
	}


}
