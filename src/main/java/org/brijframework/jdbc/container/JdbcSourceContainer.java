package org.brijframework.jdbc.container;

import org.brijframework.group.Group;
import org.brijframework.jdbc.factories.model.JdbcSourceFactory;
import org.brijframework.jdbc.group.JdbcGroup;
import org.brijframework.support.config.Assignable;
import org.brijframework.util.reflect.InstanceUtil;
import org.brijframework.util.reflect.ReflectionUtils;

public class JdbcSourceContainer extends JdbcContainer{

	private static JdbcSourceContainer container;

	@Assignable
	public static JdbcSourceContainer getContainer() {
		if (container == null) {
			container = new JdbcSourceContainer();
		}
		return container;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init() {
		try {
			ReflectionUtils.getClassListFromExternal().forEach(cls -> {
				if (JdbcSourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcSourceFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			ReflectionUtils.getClassListFromInternal().forEach(cls -> {
				if (JdbcSourceFactory.class.isAssignableFrom(cls) && InstanceUtil.isAssignable(cls)) {
					register((Class<? extends JdbcSourceFactory<?,?>>) cls);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public Group load(Object groupKey) {
		Group group = get(groupKey);
		if (group == null) {
			group = new JdbcGroup(groupKey);
			this.add(groupKey, group);
			System.err.println("Group        : " + groupKey);
		}
		return group;
	}
}
