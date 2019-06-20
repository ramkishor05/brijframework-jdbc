package org.brijframework.jdbc.group;

import java.util.concurrent.ConcurrentHashMap;

import org.brijframework.asm.group.DefaultGroup;
import org.brijframework.bean.BeanInfo;

public class JdbcGroup implements DefaultGroup {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object groupKey;
	private ConcurrentHashMap<String,BeanInfo> cache=new ConcurrentHashMap<>();
	
	public JdbcGroup(Object groupKey) {
		this.groupKey=groupKey;
	}

	@Override
	public Object getGroupKey() {
		return groupKey;
	}

	@Override
	public ConcurrentHashMap<String,BeanInfo> getCache() {
		return cache;
	}

	@Override
	public <T> T find(String parentID, Class<?> type) {
		return null;
	}
}