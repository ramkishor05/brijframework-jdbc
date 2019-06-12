package org.brijframework.jdbc.context;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.container.Container;
import org.brijframework.context.Context;

public class JdbcContext implements Context{

	private Properties properties=new Properties();
	private Context context;
	
	private ConcurrentHashMap<Object, Container>  cache=new ConcurrentHashMap<>();
	
	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	@Override
	public void initialize(Context context) {
		this.context=context;
	}

	@Override
	public void startup() {
		
	}

	@Override
	public void destory() {
		
	}

	@Override
	public Context getParent() {
		return this.context;
	}

	@Override
	public ConcurrentHashMap<Object, Container> getContainers() {
		return cache;
	}
}
